package org.itmo.invokerservice.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Deserializer
import kotlin.jvm.java
import kotlin.text.substringAfterLast

class CustomJsonDeserializer : Deserializer<Any> {
    private var objectMapper = ObjectMapper()
    private var basePackage = "org.itmo.invokerservice"

    constructor() {}

    constructor(objectMapper: ObjectMapper, basePackage: String) {
        this.objectMapper = objectMapper
        this.basePackage = basePackage
    }

    override fun deserialize(topic: String?, data: ByteArray?): Any? {
        val json = String(data ?: return null)
        val jsonMap = objectMapper.readValue(json, Map::class.java)

        val typeId = jsonMap["__TypeId__"] as? String
        if (typeId != null) {
            val className = typeId.substringAfterLast('.')
            val fullClassName = "$basePackage.config.dto.$className"

            try {
                val clazz = Class.forName(fullClassName)
                return objectMapper.readValue(json, clazz)
            } catch (ex: ClassNotFoundException) {
                println("Class not found: $fullClassName")
            }
        }

        return objectMapper.readValue(json, Any::class.java)
    }
}