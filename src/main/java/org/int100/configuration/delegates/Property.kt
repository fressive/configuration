package org.int100.configuration.delegates

import org.int100.configuration.exceptions.CannotModifyFieldValueException
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
        val canModify: Boolean = false
) {
    var value = defaultValue

    operator fun getValue(ref: Any, property: KProperty<*>): T {
        return value
    }

    @Suppress("UNCHECKED_CAST")
    inline operator fun <reified K> setValue(ref: Any, property: KProperty<*>, any: K) {
        if (canModify)
            value = any as T
        else
            throw CannotModifyFieldValueException(ref::class.java.simpleName, (any as Any)::class.java.simpleName, any as Any)
    }

    fun setValueByReflection(any: T) {
        value = any
    }

}
