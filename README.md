# TextChain

[![](https://jitpack.io/v/community.leaf/textchain.svg)](https://jitpack.io/#community.leaf/textchain) [![](https://img.shields.io/badge/License-MIT-blue)](./LICENSE)

TextChain is a streamlined way to build linear [Kyori Adventure](https://github.com/KyoriPowered/adventure) components. This library was originally made for BungeeCord chat components, so it should feel very comfortable for those who are approaching Adventure from that perspective. 

## Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>community.leaf.textchain</groupId>
    <artifactId>textchain-adventure</artifactId>
    <version><!--release--></version>
</dependency>
```

### Versions

Since we use JitPack to distribute this library, the versions available are the same as the tags on the [releases page](https://github.com/LeafCommunity/TextChain/releases) of this repository. The same applies for `artifactId`'s: simply use the name of the desired module (like `textchain-adventure` or `textchain-bukkit`).

### Shading

You will have to provide this library with a [Kyori Adventure Platform](https://github.com/KyoriPowered/adventure-platform) implementation since none are included as a transitive dependency, effectively decoupling TextChain from any specific version of Adventure.

Note: if you are using `textchain-bukkit`, it will include `textchain-adventure` automatically.

<details>
<summary><b>Example:</b> bukkit plugin maven dependencies</summary>

Since you're writing a plugin, you should already have a Bukkit/Spigot/Paper dependency defined. The following example will allow you to depend on both Kyori Adventure (required) and TextChain:

```xml
<repositories>
    <repository>
        <id>sonatype-oss</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!--
    Kyori Adventure Bukkit Platform (via sonatype-oss)
    -->
    <dependency>
        <groupId>net.kyori</groupId>
        <artifactId>adventure-platform-bukkit</artifactId>
        <version>4.0.0-SNAPSHOT</version>
    </dependency>
    <!--
    TextChain Bukkit (via jitpack.io)
    -->
    <dependency>
        <groupId>community.leaf.textchain</groupId>
        <artifactId>textchain-bukkit</artifactId>
        <version><!--release--></version>
    </dependency>
</dependencies>
```
</details>

When shading this library, please remember to **relocate** the packages so other projects may use it without conflict. This library also utilizes nullness annotations, which may be undesirable in a shaded uber-jar. To exclude them, check the spoiler below.

<details>
<summary><b>Example:</b> maven shade configuration</summary>

Set the `shade.relocation` property to your project's package and add the following to the **maven shade plugin**'s configuration:

```xml
<configuration>
    <relocations>
        <!-- TextChain -->
        <relocation>
            <pattern>community.leaf.textchain</pattern>
            <shadedPattern>${shade.relocation}.community.leaf.textchain</shadedPattern>
        </relocation>
        <!-- Kyori Adventure -->
        <relocation>
            <pattern>net.kyori</pattern>
            <shadedPattern>${shade.relocation}.net.kyori</shadedPattern>
        </relocation>
    </relocations>
    <artifactSet>
        <!-- Exclude annotations from built jar -->
        <excludes>
            <exclude>org.checkerframework:checker-qual</exclude>
            <exclude>org.jetbrains:annotations</exclude>
            <exclude>org.jetbrains.kotlin:kotlin-annotations-jvm</exclude>
            <exclude>com.google.code.findbugs:jsr305</exclude>
            <exclude>pl.tlinkowski.annotation:pl.tlinkowski.annotation.basic</exclude>
        </excludes>
    </artifactSet>
</configuration>
```
</details>