package org.int100.configuration.delegates

import kotlin.reflect.KProperty

/**
 * Config property delegate.
 *
 * @author int100
 */
class Property <T> (
        /** the name of the property in config file*/
        val name: String,
        /** default value */
        defaultValue: T,
        /** can modify by `setValue`*/
        val canModify: Boolean = false,
        /** call after loaded */
        val onLoaded: (T) -> (Unit) = {}
) {
    var value = defaultValue

    operator fun getValue(ref: Any, property: KProperty<*>): T {
        return value
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <K> setValue(ref: Any, property: KProperty<*>, any: K) {
        if (canModify)
            value = any as T
        else
            throw IllegalAccessException()
    }

    fun setValueByReflection(any: T) {
        value = any
    }

}
