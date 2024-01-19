# kfun

![version](https://img.shields.io/github/v/tag/Seggan/kfun?label=version)

kfun is a library for Kotlin that provides a useful base and utilities for writing 
[Slimefun](https://github.com/Slimefun/Slimefun4) addons. [Docs](https://seggan.github.io/kfun/).

## How to include

### Maven

First, add the JitPack repository to your `pom.xml`:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io/</url>
    </repository>
</repositories>
```

Then, add the dependency:
```xml
<dependency>
    <groupId>com.github.Seggan</groupId>
    <artifactId>kfun</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle

First, add the JitPack repository to your `build.gradle`:

#### Groovy DSL
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

#### Kotlin DSL
```kotlin
repositories {
    maven(url = "https://jitpack.io")
}
```

Then, add the dependency:

#### Groovy DSL
```groovy
dependencies {
    implementation 'com.github.Seggan:kfun:0.1.0'
}
```

#### Kotlin DSL
```kotlin
dependencies {
    implementation("com.github.Seggan:kfun:0.1.0")
}
```

## How to use

kfun provides `AbstractAddon` as a base class for you to extend from, as Kotlin does not allow
you to extend both `JavaPlugin` and `SlimefunAddon` at the same time.

kfun also provides a few convenience functions, such as destructuring and operator overloads for
`Location`, and `BlockStorage` serialization interface. See the [docs](https://seggan.github.io/kfun/)
for more information.