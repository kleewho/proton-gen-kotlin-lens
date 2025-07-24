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