package com.github.kleewho.lenspb

import com.github.kleewho.gen.test.Internal
import com.github.kleewho.gen.test.Source
import com.github.kleewho.gen.test.SourceLenses
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

//    @Test
//    fun aaaaa() {
//        val newVal = SourceLenses.fieldString1Lens.set(Source.newBuilder().build(), "aaa")
//        println(newVal)
//    }

}