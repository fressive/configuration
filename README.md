# Configuration

Written by Kotlin, use for Kotlin.

## Install
### From Maven
```xml
<dependency>
    <groupid>org.int100</groupid>
    <artifactId>configuration</artifactId>
    <version>LATEST</version>
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
Configuration licensed under the MIT License.
