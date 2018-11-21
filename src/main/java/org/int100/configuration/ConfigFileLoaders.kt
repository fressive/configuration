package org.int100.configuration

import com.alibaba.fastjson.JSON as Json
import com.alibaba.fastjson.serializer.SerializerFeature
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.DumperOptions

/**
 * Config formats.
 *
 * @author int100
 */
class ConfigFileLoaders (
        /** extension name */
        val extension: String,
        /** to object function */
        val toObject: (String) -> Map<String, Any>,
        /** to source function */
        val toSource: (Map<String, Any>) -> String,
        /** a function when the the config is empty */
        val default: () -> String
) {
    companion object {

        private val yaml = Yaml(DumperOptions().apply { this.isPrettyFlow = true })

        /**
         * JSON Format, using the `FastJson` Library
         */
        val JSON = ConfigFileLoaders(extension = ".json", toObject = { Json.parseObject(it).toMap() }, toSource = { Json.toJSONString(it, SerializerFeature.PrettyFormat) }, default = { Json.toJSONString(mapOf<String, String>(), SerializerFeature.PrettyFormat) })
        /**
         * YAML Format, using the `ShakeYaml` Library
         */
        val YAML = ConfigFileLoaders(extension = ".yml", toObject = @Suppress("UNCHECKED_CAST") { yaml.load(it) as Map<String, Any> }, toSource = { yaml.dump(it) }, default = { yaml.dump(mapOf<String, String>()) })

    }

}