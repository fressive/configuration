package org.int100.configuration.delegates

import kotlin.reflect.KProperty

/**
 * Config property delegate.
 *
 * @author int100
 */
class Property <T> (
        /** the name of the property in config file*/
        val name: String? = null,
        /** default value */
        defaultValue: T? = null,
        /** can modify by `setValue`*/
        private val canModify: Boolean = false,
        /** call after loaded */
        val onLoaded: (Boolean, T?) -> (Unit) = { _, _ ->  }
) {
    var value = defaultValue

    operator fun getValue(ref: Any, property: KProperty<*>): T? {
        return value
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <K> setValue(ref: Any, property: KProperty<*>, any: K) {
        if (canModify)
            value = any as T?
        else
            throw IllegalAccessException()
    }

    /**
     * Set the value in [org.int100.configuration.ConfigInjector.injectConfigs].
     * @hide
     */
    fun setValueByReflection(any: T?) {
        value = any
    }

}
