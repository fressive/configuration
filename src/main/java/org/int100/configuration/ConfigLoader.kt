package org.int100.configuration

import java.io.File

/**
 * Config Loader
 *
 * @author int100
 */
class ConfigLoader (
        /** Config Path */
        private val path: String,
        /** Config Format */
        private val format: ConfigFileLoaders,
        /** Default Config */
        private val defaults: Map<String, Any>,
        /** refresh on read */
        private val refreshOnRead: Boolean,
        /** save on set values */
        private val saveOnSet: Boolean,
        /** callbacks */
        private val callback: IConfigLoader
) {
    /**
     * Config Loader Builder
     */
    class Builder {
        private var configPath = ""
        private var format = ConfigFileLoaders.JSON
        private var default = mapOf<String, Any>()
        private var refreshOnRead = false
        private var saveOnSet = true
        private var callback: IConfigLoader = object : IConfigLoader {}

        fun path(path: String) = this.apply { this.configPath = path }
        fun format(format: ConfigFileLoaders) = this.apply { this.format = format }
        fun default(default: Map<String, Any>) = this.apply { this.default = default }
        fun refreshOnRead(bool: Boolean) = this.apply { this.refreshOnRead = bool }
        fun saveOnSet(bool: Boolean) = this.apply { this.saveOnSet = bool }
        fun callback(callback: IConfigLoader) = this.apply { this.callback = callback }

        fun build() = ConfigLoader(configPath, format, default, refreshOnRead, saveOnSet, callback)
    }

    private val file by lazy { File(this.path) }
    val map by lazy { this.format.toObject(this.file.readText()).toMutableMap() }

    init {
        this.existsOrCreate()
        this.copyDefault()
    }

    /**
     * Copy default value into the config.
     */
    private fun copyDefault() {
        this.map.putAll(mutableMapOf<String, Any>().apply {
            this@ConfigLoader.defaults.keys
                    .filter { !this@ConfigLoader.map.keys.contains(it) }
                    .forEach { this[it] = defaults[it] as Any }
        })
    }

    /**
     * Create config on the config is not exists.
     */
    private fun existsOrCreate() {
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }

        if (!file.exists()) {
            if (callback.onConfigNotExists()) {
                file.createNewFile()
                file.writeText(this.format.default())
            }
        }
    }

    /**
     * Load config.
     */
    fun load() = this.map.apply { this.clear() }.apply { this.putAll(this@ConfigLoader.format.toObject(this@ConfigLoader.file.readText())) }

    /**
     * Save config.
     */
    fun save() = this.file.writeText(this.format.toSource(this.map))

    /**
     * Get config. Return [default] when config is null.
     */
    fun get(key: String, default: Any = Unit): Any {
        if (this.refreshOnRead)
            this.load()

        return map.getOrDefault(key, default)
    }

    /**
     * Set config.
     */
    fun set(key: String, value: Any) {
        map[key] = value

        if (this.saveOnSet)
            this.save()
    }


}