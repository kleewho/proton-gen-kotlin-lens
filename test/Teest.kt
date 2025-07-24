package com.github.kleewho.lenspb

import com.github.kleewho.gen.test.Internal
import com.github.kleewho.gen.test.OtherTarget
import com.github.kleewho.gen.test.Source
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class Teest {
    val fieldString1Lens: arrow.optics.PLens<com.github.kleewho.gen.test.Source, com.github.kleewho.gen.test.Source, kotlin.String, kotlin.String> = arrow.optics.PLens<com.github.kleewho.gen.test.Source, com.github.kleewho.gen.test.Source, kotlin.String, kotlin.String>(get = {
        it.fieldString1
    }, set = { r, f -> r.newBuilderForType().setFieldString1(f).build()})

    @Test
    fun addPropertyLensTest() {
        val fieldDescriptor = Source.getDescriptor().findFieldByName("field_string_1")

        val generatedProperty = fieldDescriptor.generateLensProperty(Source.getDescriptor()).toString()

        generatedProperty shouldBe
                "val fieldString1: arrow.optics.PLens<com.github.kleewho.gen.test.Source, com.github.kleewho.gen.test.Source, kotlin.String, kotlin.String> = arrow.optics.PLens<com.github.kleewho.gen.test.Source, com.github.kleewho.gen.test.Source, kotlin.String, kotlin.String>(get = { it.fieldString1 }, set = { it, v -> it.newBuilderForType().setFieldString1(v).build() })\n"
    }


    @Test
    fun addPropertyLensForMessageField() {
        val fieldDescriptor = Source.getDescriptor().findFieldByName("internal")

        val generatedProperty = fieldDescriptor.generateLensProperty(Source.getDescriptor()).toString()

        generatedProperty shouldBe
                "val `internal`: arrow.optics.PLens<com.github.kleewho.gen.test.Source, com.github.kleewho.gen.test.Source, com.github.kleewho.gen.test.Internal, com.github.kleewho.gen.test.Internal> = arrow.optics.PLens<com.github.kleewho.gen.test.Source, com.github.kleewho.gen.test.Source, com.github.kleewho.gen.test.Internal, com.github.kleewho.gen.test.Internal>(get = { it.`internal` }, set = { it, v -> it.newBuilderForType().setInternal(v).build() })\n"
    }


    @Test
    fun addPropertyForRepeatedField() {
        val fieldDescriptor = Internal.getDescriptor().findFieldByName("repeated_int")

        val generatedProperty = fieldDescriptor.generateLensProperty(Internal.getDescriptor()).toString()

        generatedProperty shouldBe
                "val repeatedInt: arrow.optics.PLens<com.github.kleewho.gen.test.Internal, com.github.kleewho.gen.test.Internal, kotlin.collections.List<kotlin.Long>, kotlin.collections.List<kotlin.Long>> = arrow.optics.PLens<com.github.kleewho.gen.test.Internal, com.github.kleewho.gen.test.Internal, kotlin.collections.List<kotlin.Long>, kotlin.collections.List<kotlin.Long>>(get = { it.repeatedInt }, set = { it, v -> it.newBuilderForType().setRepeatedInt(v).build() })\n"
    }


    @Test
    fun addPropertyForMapField() {
        val fieldDescriptor = OtherTarget.getDescriptor().findFieldByName("field_map")

        val generatedProperty = fieldDescriptor.generateLensProperty(OtherTarget.getDescriptor()).toString()

        generatedProperty shouldBe
                "val fieldMap: arrow.optics.PLens<com.github.kleewho.gen.test.OtherTarget, com.github.kleewho.gen.test.OtherTarget, kotlin.collections.Map<kotlin.String, kotlin.Long>, kotlin.collections.Map<kotlin.String, kotlin.Long>> = arrow.optics.PLens<com.github.kleewho.gen.test.OtherTarget, com.github.kleewho.gen.test.OtherTarget, kotlin.collections.Map<kotlin.String, kotlin.Long>, kotlin.collections.Map<kotlin.String, kotlin.Long>>(get = { it.fieldMap }, set = { it, v -> it.newBuilderForType().setFieldMap(v).build() })\n"
    }
//    @Test
//    fun aaaaa() {
//        val newVal = SourceLenses.fieldString1Lens.set(Source.newBuilder().build(), "aaa")
//        println(newVal)
//    }

}