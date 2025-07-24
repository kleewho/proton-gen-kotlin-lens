// Copyright 2025 Łukasz Klich
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.github.kleewho.lenspb

import arrow.optics.Lens
import com.google.protobuf.Descriptors
import com.google.protobuf.compiler.PluginProtos
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        println("I only know how to take input from stdin for now.")
        exitProcess(1)
    }

    val generatorRequest = try {
        System.`in`.buffered().use {
            PluginProtos.CodeGeneratorRequest.parseFrom(it)
        }
    } catch (failure: Exception) {
        throw RuntimeException("Failed to parse plugin request from stdin.", failure)
    }

    val response = generate(generatorRequest)

    System.out.buffered().use {
        response.writeTo(it)
    }
}

/**
 * key: proto file, value FileDescriptor
 * ref: https://github.com/grpc/grpc-kotlin/blob/ffdac372efe9a289ec504677cc0e58c9ed9f87f9/compiler/src/main/java/io/grpc/kotlin/generator/protoc/CodeGenerators.kt#L28-L42
 */
private fun buildFileDescriptorMap(request: PluginProtos.CodeGeneratorRequest): Map<String, Descriptors.FileDescriptor> {
    val descriptorsByName = mutableMapOf<String, Descriptors.FileDescriptor>()
    for (protoFile in request.protoFileList) {
        // we should have visited all the dependencies, so they should be present in the map
        val dependencies = protoFile.dependencyList.map(descriptorsByName::getValue)

        // build and link the descriptor for this file to its dependencies
        val fileDescriptor = Descriptors.FileDescriptor.buildFrom(protoFile, dependencies.toTypedArray())

        descriptorsByName[protoFile.name] = fileDescriptor
    }
    return descriptorsByName
}


fun generate(generatorRequest: PluginProtos.CodeGeneratorRequest): PluginProtos.CodeGeneratorResponse {
    val descriptorMap = buildFileDescriptorMap(generatorRequest)
    val files = generatorRequest.fileToGenerateList.mapNotNull {
        System.err.println("Generating for $it")
        descriptorMap[it]?.let(Descriptors.FileDescriptor::generateSingle)
    }.map(FileSpec::toResponseFileProto)


    return PluginProtos.CodeGeneratorResponse.newBuilder()
        .setSupportedFeatures(PluginProtos.CodeGeneratorResponse.Feature.FEATURE_PROTO3_OPTIONAL_VALUE.toLong())
        .addAllFile(files).build()
}

fun FileSpec.toResponseFileProto(): PluginProtos.CodeGeneratorResponse.File {
    return PluginProtos.CodeGeneratorResponse.File.newBuilder()
        .setName(relativePath)
        .setContent(toString())
        .build()
}

fun Descriptors.FileDescriptor.generateSingle(): FileSpec {
    val builder = FileSpec.builder(
        packageName = options.javaPackage,
        fileName = name.substringAfterLast("/").replace(".proto", "Lens")
    )

    this.messageTypes.map(Descriptors.Descriptor::generateMessageType).forEach { builder.addType(it) }

    return builder.build()
}

fun Descriptors.Descriptor.generateMessageType(): TypeSpec {
    val builder = TypeSpec.objectBuilder(name + "Lenses")
    fields.forEach { field ->
        if (field.isMapField) {
            //TODO
        }

        if (field.isRepeated) {
            //TODO
        }
        builder.addProperty(field.generateLensProperty(this))
    }

    nestedTypes.map(Descriptors.Descriptor::generateMessageType).forEach { builder.addType(it) }

    return builder.build()
}

fun Descriptors.FieldDescriptor.fieldJavaName(): String {
    return name.toCamelCase()
}

fun Descriptors.FieldDescriptor.builderSetterName(): String {
    return "set${name.toPascalCase()}"
}

fun Descriptors.FieldDescriptor.lensPropertyName(): String {
    return name.toCamelCase()
}

fun String.toPascalCase(): String {
    return split("_").joinToString("") { it.replaceFirstChar(Char::uppercaseChar) }
}

fun String.toCamelCase(): String {
    val split = split("_")
    return split.first() + split.drop(1).joinToString("") { it.replaceFirstChar(Char::uppercaseChar) }
}

fun Descriptors.FieldDescriptor.kotlinPoetTypeName(): TypeName {
    return when (javaType) {
        Descriptors.FieldDescriptor.JavaType.INT -> INT
        Descriptors.FieldDescriptor.JavaType.LONG -> LONG
        Descriptors.FieldDescriptor.JavaType.FLOAT -> FLOAT
        Descriptors.FieldDescriptor.JavaType.DOUBLE -> DOUBLE
        Descriptors.FieldDescriptor.JavaType.STRING -> STRING
        Descriptors.FieldDescriptor.JavaType.BOOLEAN -> BOOLEAN
        Descriptors.FieldDescriptor.JavaType.MESSAGE -> messageType.resolveClassName()
        else -> ANY
    }
}

fun Descriptors.FieldDescriptor.generateLensPropertyForMapField(rootClassName: TypeName): PropertySpec {
    val keyTypeName = messageType.findFieldByNumber(1).kotlinPoetTypeName()
    val valueTypeName = messageType.findFieldByNumber(2).kotlinPoetTypeName()
    val mapType = Map::class.asClassName().parameterizedBy(keyTypeName, valueTypeName)
    val parametrizedLensType = Lens::class.asClassName().parameterizedBy(rootClassName, rootClassName, mapType, mapType)
    val propertySpecBuilder = PropertySpec.builder(name = lensPropertyName(), type = parametrizedLensType)
    propertySpecBuilder.initializer(
        CodeBlock.of(
            "%1T(get = { it.%2NMap }, set = { it, v -> it.newBuilderForType().clear%3N().putAll%3N(v).build() })",
            parametrizedLensType,
            fieldJavaName(),
            name.toPascalCase()
        )
    )
    return propertySpecBuilder.build()
}

fun Descriptors.FieldDescriptor.generateLensPropertyForRepeatedField(rootClassName: TypeName): PropertySpec {
    val fieldTypeName = kotlinPoetTypeName()
    val listType = List::class.asClassName().parameterizedBy(fieldTypeName)
    val parametrizedLensType =
        Lens::class.asClassName().parameterizedBy(rootClassName, rootClassName, listType, listType)
    val propertySpecBuilder = PropertySpec.builder(name = lensPropertyName(), type = parametrizedLensType)
    propertySpecBuilder.initializer(
        CodeBlock.of(
            "%1T(get = { it.%2NList }, set = { it, v -> it.newBuilderForType().clear%3N().addAll%3N(v).build() })",
            parametrizedLensType,
            fieldJavaName(),
            name.toPascalCase()
        )
    )
    return propertySpecBuilder.build()
}

fun Descriptors.FieldDescriptor.generateLensProperty(rootClassName: TypeName): PropertySpec {
    val fieldTypeName = kotlinPoetTypeName()
    val parametrizedLensType =
        Lens::class.asClassName().parameterizedBy(rootClassName, rootClassName, fieldTypeName, fieldTypeName)
    val propertySpecBuilder = PropertySpec.builder(name = lensPropertyName(), type = parametrizedLensType)
    propertySpecBuilder.initializer(
        CodeBlock.of(
            "%T(get = { it.%N }, set = { it, v -> it.newBuilderForType().%N(v).build() })",
            parametrizedLensType,
            fieldJavaName(),
            builderSetterName()
        )
    )
    return propertySpecBuilder.build()
}

fun Descriptors.FieldDescriptor.generateLensProperty(messageDescriptor: Descriptors.Descriptor): PropertySpec {
    val rootClassName = messageDescriptor.resolveClassName()

    return when {
        isMapField -> generateLensPropertyForMapField(rootClassName)
        isRepeated -> generateLensPropertyForRepeatedField(rootClassName)
        else -> generateLensProperty(rootClassName)
    }
}