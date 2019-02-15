package org.int100.configuration

import com.alibaba.fastjson.JSON as Json
import com.alibaba.fastjson.serializer.SerializerFeature
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.DumperOptions

/**
 * Config file loaders enum class.
 */
enum class ConfigFileLoaders(
        /** to object function */
        val toObject: (String) -> Map<String, Any>,
        /** to source function */
        val toSource: (Map<String, Any>) -> String,
        /** a function when the the config is empty */
        val default: () -> String
) {

    /**
     * JSON Format, using the `FastJson` Library
     */
    JSON(toObject = { Json.parseObject(it).toMap() },
            toSource = { Json.toJSONString(it, SerializerFeature.PrettyFormat) },
            default = { Json.toJSONString(mapOf<String, String>(), SerializerFeature.PrettyFormat) }
    ),
    /**
     * YAML Format, using the `ShakeYaml` Library
     */
    YAML(toObject = { yaml.load(it) as Map<String, Any> },
            toSource = { yaml.dump(it) },
            default = { yaml.dump(mapOf<String, String>()) }
    );

    companion object {
        private val yaml = Yaml(DumperOptions().apply { this.isPrettyFlow = true })
    }

}