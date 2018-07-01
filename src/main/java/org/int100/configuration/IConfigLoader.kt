package org.int100.configuration

/**
 * Config loader callback.
 *
 * @author int100
 */
interface IConfigLoader {

    fun onConfigNotExists(): Boolean = true

}