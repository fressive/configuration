package org.int100.configuration.annotations

import org.int100.configuration.EConfigFileLoaders

/**
 * Annotation for config class.
 *
 * @author int100
 */
@Target(AnnotationTarget.CLASS)
annotation class Config (
        /** Config path */
        val configPath: String,
        /** Support config formats. If there are multiple supported formats, default use the first format. */
        val formats: Array<EConfigFileLoaders>,
        /** Default config node. Multiple nodes split with `.`. */
        val node: String = ""
)