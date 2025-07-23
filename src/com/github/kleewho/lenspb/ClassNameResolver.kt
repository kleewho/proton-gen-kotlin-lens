package com.github.kleewho.lenspb

import com.google.protobuf.Descriptors
import com.squareup.kotlinpoet.ClassName

fun Descriptors.Descriptor.resolveClassName(): ClassName {
    val resolvedPackage = if (file.options.hasJavaPackage()) file.options.javaPackage else file.`package`

    return when {
        file.options.javaMultipleFiles -> ClassName(resolvedPackage, name)
        file.options.hasJavaOuterClassname() -> ClassName(resolvedPackage, file.options.javaOuterClassname, name)
        else -> {
            val outerClassName = file.name.substringBefore(".proto").toPascalCase()
            when {
                file.messageTypes.any { it.name == outerClassName } -> ClassName(resolvedPackage, "${outerClassName}OuterClass", name)
                else -> ClassName(resolvedPackage, outerClassName, name)
            }
        }
    }
}