package org.int100.configuration

import com.esotericsoftware.reflectasm.MethodAccess
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.int100.configuration.annotations.Config
import org.int100.configuration.delegates.Property
import org.reflections.Reflections
import java.io.File

/**
 * Config Injector.
 *
 * @author int100
 */
@Suppress("UNCHECKED_CAST")
object ConfigInjector {

    /**
     * Inject configs into [classes].
     */
    fun injectConfigs(classes: Collection<Class<*>>) {
        classes.forEach {
            val annotation = it.getAnnotation(Config::class.java)

            var configFile: File? = null
            var format = ConfigFileLoaders.JSON

            for (i in annotation.formats) {
                File(annotation.configPath).takeIf { file -> file.exists() }.apply { configFile = this; }
                format = i
                if (configFile != null) break
            }

            val instancesMap = mutableMapOf<String, Any>()

            if (configFile != null) {

                var config = ConfigLoader.Builder().path(configFile!!.path).format(format).build().map

                if (!annotation.node.isEmpty()) {
                    annotation.node.split(".").forEach { str ->
                        val c = config[str] as Map<String, Any>
                        config = c.toMutableMap()
                    }
                }

                // kotlin 会在委托变量后增加 $delegate
                it.declaredFields.filter { field -> field.name.endsWith("${"$"}delegate") }.forEach fe@{ f ->

                    f.isAccessible = true
                    // 设置可访问性
                    val instance = instancesMap.getOrPut(it.name) { it.declaredFields.filter { field -> field.name == "INSTANCE" }[0] }

                    val ins = f.get(instance) as Property<*>
                    // 获取委托实例

                    val name = ins.name ?: f.name.split("${"$"}delegate")[0]

                    if (config[name] != null) {
                        if (!ins.value!!::class.isInstance(config[name])) throw TypeCastException("Cannot cast ${ins.value!!::class.simpleName} to ${config[name]!!::class.simpleName}")
                        // 判断类型是否相等

                        MethodAccess.get(Property::class.java).invoke(ins, "setValueByReflection", config[name])
                        // 更改委托值
                    }

                    (ins.onLoaded as (Boolean, Any?) -> Unit)(config[name] != null, ins.value)
                    GlobalConfig["${annotation.name}_$name"] = ins.value
                }
            } else {
                it.declaredFields.filter { field -> field.name.endsWith("${"$"}delegate") }.forEach fe@{ f ->

                    f.isAccessible = true
                    val instance = instancesMap.getOrPut(it.name) { it.declaredFields.filter { field -> field.name == "INSTANCE" }[0] }

                    val ins = f.get(instance) as Property<*>

                    val name = ins.name ?: f.name.split("${"$"}delegate")[0]

                    (ins.onLoaded as (Boolean, Any?) -> Unit)(false, ins.value)
                    GlobalConfig["${annotation.name}_$name"] = ins.value
                }
            }
        }
    }

    /**
     * Inject configs into config classes from [package]. You can specify the [classLoader] for class searching.
     */
    fun injectConfigs(`package`: String, classLoader: ClassLoader = ClassLoader.getSystemClassLoader()) {
        val ref = Reflections(`package`, classLoader)
        ConfigInjector.injectConfigs(ref.getTypesAnnotatedWith(Config::class.java))
    }

}