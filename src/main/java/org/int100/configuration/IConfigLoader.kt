package org.int100.configuration

/**
 * Config loader callback.
 *
 * @author int100
 */
interface IConfigLoader {

    /**
     * Calls on config file isn't exist
     */
    fun onConfigNotExists(): Boolean = true

}