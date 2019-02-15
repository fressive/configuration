# Configuration

A simple, lightweight and useful configuration library.

## Install
### Add dependency (Maven)
Add the JitPack repository to your `pom.xml`:
```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://www.jitpack.io</url>
	</repository>
</repositories>
```
Add the dependency in `dependencies`.
```xml
<dependency>
    <groupId>com.github.int100</groupId>
    <artifactId>configuration</artifactId>
    <version>{LATEST_VERSION}</version>
</dependency>
```
## Example

First, generate a config file.

```yaml
# config.yml

foo: bar
```

Then, code the config class (Use object).

```kotlin
import org.int100.configuration.delegates.Property
import org.int100.configuration.ConfigFileLoaders

@Config(configPath="config.yml", name="default", formats=[ConfigFileLoaders.YAML])
object TestConfig {
    val foo by Property<String>()
    val number by Property(defaultValue = 123)
    val call by Property<Any> { success, it ->
        if (success) 
            println("Call me $it")
        else 
            println(":( failed call")
    }
}
```

Finally, load the config(s) in the main. 

```kotlin
fun main(args: Array<String>) {
    ConfigInjector.injectConfigs("") // or ConfigInjector.injectConfigs(setOf(TestConfig::class.java))
    println(TestConfig.foo) // or GlobalConfig["default_foo"]
    println(TestConfig.number)
}
```

Output:
```
:( failed call
bar
123
```
## License
Configuration licensed under **the MIT License**.

For more details, see [LICENSE](LICENSE).
