package com.github.kleewho.lenspb

import com.github.kleewho.gen.test.Action
import com.github.kleewho.gen.test.Internal
import com.github.kleewho.gen.test.OtherTarget
import com.github.kleewho.gen.test.Source
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class Teest {
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
                "val repeatedInt: arrow.optics.PLens<com.github.kleewho.gen.test.Internal, com.github.kleewho.gen.test.Internal, kotlin.collections.List<kotlin.Long>, kotlin.collections.List<kotlin.Long>> = arrow.optics.PLens<com.github.kleewho.gen.test.Internal, com.github.kleewho.gen.test.Internal, kotlin.collections.List<kotlin.Long>, kotlin.collections.List<kotlin.Long>>(get = { it.repeatedIntList }, set = { it, v -> it.newBuilderForType().clearRepeatedInt().addAllRepeatedInt(v).build() })\n"
    }


    @Test
    fun addPropertyForMapField() {
        val fieldDescriptor = OtherTarget.getDescriptor().findFieldByName("field_map")

        val generatedProperty = fieldDescriptor.generateLensProperty(OtherTarget.getDescriptor()).toString()

        generatedProperty shouldBe
                "val fieldMap: arrow.optics.PLens<com.github.kleewho.gen.test.OtherTarget, com.github.kleewho.gen.test.OtherTarget, kotlin.collections.Map<kotlin.String, kotlin.Long>, kotlin.collections.Map<kotlin.String, kotlin.Long>> = arrow.optics.PLens<com.github.kleewho.gen.test.OtherTarget, com.github.kleewho.gen.test.OtherTarget, kotlin.collections.Map<kotlin.String, kotlin.Long>, kotlin.collections.Map<kotlin.String, kotlin.Long>>(get = { it.fieldMapMap }, set = { it, v -> it.newBuilderForType().clearFieldMap().putAllFieldMap(v).build() })\n"
    }

    @Test
    fun generateNestedMessages() {
        val descriptor = Action.getDescriptor()

        val generatedObject = descriptor.generateMessageType().toString()

        generatedObject shouldBe """public object ActionLenses {
  public val metadata: arrow.optics.PLens<com.github.kleewho.gen.test.Action, com.github.kleewho.gen.test.Action, com.github.kleewho.gen.test.Action.Metadata, com.github.kleewho.gen.test.Action.Metadata> = arrow.optics.PLens<com.github.kleewho.gen.test.Action, com.github.kleewho.gen.test.Action, com.github.kleewho.gen.test.Action.Metadata, com.github.kleewho.gen.test.Action.Metadata>(get = { it.metadata }, set = { it, v -> it.newBuilderForType().setMetadata(v).build() })

  public val `data`: arrow.optics.PLens<com.github.kleewho.gen.test.Action, com.github.kleewho.gen.test.Action, com.github.kleewho.gen.test.Action.Data, com.github.kleewho.gen.test.Action.Data> = arrow.optics.PLens<com.github.kleewho.gen.test.Action, com.github.kleewho.gen.test.Action, com.github.kleewho.gen.test.Action.Data, com.github.kleewho.gen.test.Action.Data>(get = { it.`data` }, set = { it, v -> it.newBuilderForType().setData(v).build() })

  public object MetadataLenses {
    public val id: arrow.optics.PLens<com.github.kleewho.gen.test.Action.Metadata, com.github.kleewho.gen.test.Action.Metadata, kotlin.String, kotlin.String> = arrow.optics.PLens<com.github.kleewho.gen.test.Action.Metadata, com.github.kleewho.gen.test.Action.Metadata, kotlin.String, kotlin.String>(get = { it.id }, set = { it, v -> it.newBuilderForType().setId(v).build() })
  }

  public object DataLenses {
    public val action: arrow.optics.PLens<com.github.kleewho.gen.test.Action.Data, com.github.kleewho.gen.test.Action.Data, com.github.kleewho.gen.test.Action.Config, com.github.kleewho.gen.test.Action.Config> = arrow.optics.PLens<com.github.kleewho.gen.test.Action.Data, com.github.kleewho.gen.test.Action.Data, com.github.kleewho.gen.test.Action.Config, com.github.kleewho.gen.test.Action.Config>(get = { it.action }, set = { it, v -> it.newBuilderForType().setAction(v).build() })

    public val event: arrow.optics.PLens<com.github.kleewho.gen.test.Action.Data, com.github.kleewho.gen.test.Action.Data, com.github.kleewho.gen.test.Action.Event, com.github.kleewho.gen.test.Action.Event> = arrow.optics.PLens<com.github.kleewho.gen.test.Action.Data, com.github.kleewho.gen.test.Action.Data, com.github.kleewho.gen.test.Action.Event, com.github.kleewho.gen.test.Action.Event>(get = { it.event }, set = { it, v -> it.newBuilderForType().setEvent(v).build() })
  }

  public object ConfigLenses {
    public val topic: arrow.optics.PLens<com.github.kleewho.gen.test.Action.Config, com.github.kleewho.gen.test.Action.Config, kotlin.String, kotlin.String> = arrow.optics.PLens<com.github.kleewho.gen.test.Action.Config, com.github.kleewho.gen.test.Action.Config, kotlin.String, kotlin.String>(get = { it.topic }, set = { it, v -> it.newBuilderForType().setTopic(v).build() })
  }

  public object EventLenses {
    public val type: arrow.optics.PLens<com.github.kleewho.gen.test.Action.Event, com.github.kleewho.gen.test.Action.Event, kotlin.String, kotlin.String> = arrow.optics.PLens<com.github.kleewho.gen.test.Action.Event, com.github.kleewho.gen.test.Action.Event, kotlin.String, kotlin.String>(get = { it.type }, set = { it, v -> it.newBuilderForType().setType(v).build() })
  }
}
"""
    }
}
