package org.itmo.invokerservice.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.header.Headers
import org.apache.kafka.common.serialization.Deserializer
import org.springframework.stereotype.Component
import kotlin.jvm.java
import kotlin.text.substringAfterLast

@Component
class CustomJsonDeserializer : Deserializer<Any> {
    private val objectMapper: ObjectMapper = ObjectMapper()
    private val basePackage = "org.itmo.invokerservice"

    override fun deserialize(p0: String?, p1: ByteArray?): Any? {
        return null
    }

    override fun deserialize(topic: String?, headers: Headers, data: ByteArray?): Any? {
        val json = String(data ?: return null)

        val typeIdHeader = headers.lastHeader("__TypeId__")

        if (typeIdHeader != null) {
            val typeId = String(typeIdHeader.value())
            val className = typeId.substringAfterLast('.')
            val fullClassName = "$basePackage.config.dto.$className"

            try {
                val clazz = Class.forName(fullClassName)
                return objectMapper.readValue(json, clazz)
            } catch (ex: ClassNotFoundException) {
                println("Class not found: $fullClassName")
            }
        }

        return objectMapper.readValue(json, Map::class.java)
    }
}
