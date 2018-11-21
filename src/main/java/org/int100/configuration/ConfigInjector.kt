package org.int100.configuration

import com.esotericsoftware.reflectasm.MethodAccess
import org.int100.configuration.annotations.Config
import org.int100.configuration.delegates.Property
import org.reflections.Reflections
import java.io.File

/**
 * Config Injector.
 *
 * @author int100
 */
object ConfigInjector {

    /**
     * Inject all configs(from [classLoader]) into classes under [package].
     */
    @Suppress("UNCHECKED_CAST")
    fun injectConfigs(`package`: String, classLoader: ClassLoader = ClassLoader.getSystemClassLoader()) {
        val ref = Reflections(`package`, classLoader)
        ref.getTypesAnnotatedWith(Config::class.java).forEach { it ->
            val annotation = it.getAnnotation(Config::class.java)

            var configFile: File? = null
            var format = ConfigFileLoaders.JSON
            for (i in annotation.formats) {
                File("${annotation.configPath}${i.loader.extension}").takeIf { it.exists() }.apply { configFile = this; }
                format = i.loader
                if (configFile != null) { break }
            }

            configFile ?: return

            var config = ConfigLoader.Builder().path(configFile!!.path).format(format).build().map

            if (!annotation.node.isEmpty()) {
                annotation.node.split(".").forEach {
                    val c = (config[it] as? Map<String, Any>) ?: return
                    config = c.toMutableMap()
                }
            }

            // 缓存对象的实例，提高性能
            val instancesMap = mutableMapOf<String, Any>()

            // kotlin 会在委托变量后增加 $delegate
            it.declaredFields.filter { it.name.endsWith("${"$"}delegate") }.forEach fe@{ f ->
                f.isAccessible = true
                // 设置可访问性
                val instance = instancesMap.getOrPut(it.name){ it.declaredFields.filter { it.name == "INSTANCE" }[0] }
                val ins = f.get(instance) as Property<*>
                // 获取委托实例

                config[ins.name] ?: return@fe
                // 如果没有该属性，跳过以免覆盖默认值

                if (!ins.value!!::class.isInstance(config[ins.name])) throw TypeCastException("Cannot cast ${ins.value!!::class.simpleName} to ${config[ins.name]!!::class.simpleName}")
                // 判断类型是否相等

                MethodAccess.get(Property::class.java).invoke(ins, "setValueByReflection", config[ins.name])
                // 更改委托值
            }
        }


    }
}