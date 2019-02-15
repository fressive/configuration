package org.int100.configuration.annotations

import org.int100.configuration.ConfigFileLoaders

/**
 * Annotation for config class.
 *
 * @author int100
 */
@Target(AnnotationTarget.CLASS)
annotation class Config (
        /** Config path */
    val configPath: String,
        /** Config name */
    val name: String,
        /** Support config formats. If there are multiple supported formats, default use the first format. */
    val formats: Array<ConfigFileLoaders>,
        /** Default config node. Multiple nodes split with `.`. */
    val node: String = ""
)