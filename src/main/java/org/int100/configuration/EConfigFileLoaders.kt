package org.int100.configuration

/**
 * Config file loaders enum class.
 */
enum class EConfigFileLoaders(
        val loader: ConfigFileLoaders
) {
    JSON(ConfigFileLoaders.JSON),
    YAML(ConfigFileLoaders.YAML)
}