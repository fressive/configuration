# Configuration

Configure some configurations.

## Install
### From Maven(JitPack)
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

```yaml
# config.yml

test: 123
```

```kotlin
// com/example/config/Config.kt
package com.example.config

import org.int100.configuration.delegates.Property
import org.int100.configuration.EConfigFileLoaders

@Config(configPath="config.yml", formats=[EConfigFileLoaders.YAML])
object Config {
    val test by Property(name="test", defaultValue=123)
}
```

```kotlin
// com/example/Main.kt

import com.example.config.Config
import org.int100.configuration.ConfigInjector

fun main(args: Array<String>) {
    ConfigInjector.injectConfigs("com.example")
    println(Config.test)
}
```

## License
Configuration licensed under **the MIT License**.

For more details, see [LICENSE](LICENSE).
