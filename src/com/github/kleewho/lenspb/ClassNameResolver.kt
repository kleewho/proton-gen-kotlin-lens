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

    val names = mutableListOf<String>()
    var desc = this
    while (true) {
        names.add(desc.name)
        if (desc.containingType == null) {
            break
        }
        desc = desc.containingType
    }
    names.reverse()

    return when {
        file.options.javaMultipleFiles -> ClassName(resolvedPackage, names)
        file.options.hasJavaOuterClassname() -> ClassName(resolvedPackage, file.options.javaOuterClassname + names)
        else -> {
            val outerClassName = file.name.substringBefore(".proto").toPascalCase()
            when {
                file.messageTypes.any { it.name == outerClassName } -> ClassName(resolvedPackage, "${outerClassName}OuterClass" + names)
                else -> ClassName(resolvedPackage, outerClassName + names)
            }
        }
    }
}