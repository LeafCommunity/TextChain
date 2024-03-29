 # ⛓️ TextChain

[![](https://jitpack.io/v/community.leaf/textchain.svg)](https://jitpack.io/#community.leaf/textchain "Get maven artifacts on JitPack")
[![](https://img.shields.io/badge/License-MPL--2.0-blue)](./LICENSE "Project license: MPL-2.0")
[![](https://img.shields.io/badge/Java-11-orange)](#java-version "This project targets Java 11")
[![](https://img.shields.io/badge/View-Javadocs-%234D7A97)](https://leafcommunity.github.io/TextChain/ "View the javadocs")

TextChain is a streamlined way to build linear 
[Kyori Adventure](https://github.com/KyoriPowered/adventure) components.
It also offers many quality of life features for targeting specific platforms
(like Bukkit). This library was *originally* written for BungeeCord chat components,
so it should feel comfortable for those approaching Adventure from that perspective.

![](https://i.imgur.com/ubjbb9S.png "Hello world!")

<details id="example-hello-world">
<summary><b>Example:</b> <i>Hello World</i></summary>

> [ℹ️](#example-hello-world) 
> ```java
> TextChain.chain()
>    .then("Hello")
>        .underlined()
>    .then(" ")
>    .then("world!")
>        .bold()
>        .italic()
>    .send(audience);
> ```

</details>


## Contents

- **[Maven](#maven)** → _(How to **get** TextChain)_
    - [Modules](#modules)
    - [Versions](#versions)
    - [Shading](#shading)
- **[Rationale](#rationale)** → _(Why TextChain?)_
- **[Usage](#usage)** → _(How to **use** TextChain)_

See also:

- ***[Example Plugins](./examples)*** → 🔰
- [TextChain Javadocs](https://leafcommunity.github.io/TextChain/)
- [Kyori Adventure Documentation](https://docs.adventure.kyori.net/)


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
    <artifactId><!--module--></artifactId>
    <version><!--release--></version>
</dependency>
```

### Modules

- **`textchain-adventure`** → 📑
    - The standalone, platform-independent version of TextChain.
      It **only** requires Kyori Adventure, so this module runs
      wherever Adventure runs.
- **`textchain-platform-bukkit`** → 🚰
    - TextChain with additional Bukkit-specific features like
      sending chains directly to players and helpful utilities
      like creating components for entities, items, and converting
      from Bukkit objects to Adventure-equivalent objects.
- **`textchain-platform-bungeecord`** → 🟠
    - TextChain with additional Bungee-specific features like
      sending chains directly to proxied players.

### Versions

Since we use JitPack to distribute this library, the versions available 
are the same as the "tags" found on the [releases page](https://github.com/LeafCommunity/TextChain/releases)
of this repository.

### Shading

You will have to provide TextChain with a
[Kyori Adventure Platform](https://github.com/KyoriPowered/adventure-platform)
implementation since none are included by default (effectively decoupling
this library from any *specific* version of Adventure).

<details id="example-bukkit-plugin-maven-dependencies">
<summary><b>Example:</b> <i>Bukkit Plugin Maven Dependencies</i></summary>

> [ℹ️](#example-bukkit-plugin-maven-dependencies) 
> Since you're writing a plugin, you should already have a Bukkit/Spigot/Paper
> dependency defined. The following example will allow you to depend on both
> Kyori Adventure (required) and TextChain:
> 
> ```xml
> <repositories>
>     <repository>
>         <id>sonatype-oss</id>
>         <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
>     </repository>
>     <repository>
>         <id>jitpack.io</id>
>         <url>https://jitpack.io</url>
>     </repository>
> </repositories>
> 
> <dependencies>
>     <!-- Kyori Adventure Bukkit Platform (via sonatype-oss) -->
>     <dependency>
>         <groupId>net.kyori</groupId>
>         <artifactId>adventure-platform-bukkit</artifactId>
>         <version>4.0.0-SNAPSHOT</version>
>     </dependency>
>     <!-- TextChain Bukkit (via jitpack.io) -->
>     <dependency>
>         <groupId>community.leaf.textchain</groupId>
>         <artifactId>textchain-bukkit</artifactId>
>         <version><!--release--></version>
>     </dependency>
> </dependencies>
> ```

</details>

When shading this library, please remember to **relocate** the packages
so other projects may use it without conflict. This library also utilizes
nullness annotations, which may be undesirable in a shaded uber-jar.
To exclude them, check the example below.

<details id="example-maven-shade-configuration">
<summary><b>Example:</b> <i>Maven Shade Configuration</i></summary>

> [ℹ️](#example-maven-shade-configuration) 
> Set the `shade.relocation` property to your project's package
> and add the following to the **maven shade plugin**'s configuration:
> 
> ```xml
> <configuration>
>     <relocations>
>         <!-- TextChain -->
>         <relocation>
>             <pattern>community.leaf.textchain</pattern>
>             <shadedPattern>${shade.relocation}.community.leaf.textchain</> shadedPattern>
>         </relocation>
>         <!-- Kyori Adventure -->
>         <relocation>
>             <pattern>net.kyori</pattern>
>             <shadedPattern>${shade.relocation}.net.kyori</shadedPattern>
>         </relocation>
>     </relocations>
>     <artifactSet>
>         <!-- Exclude annotations from built jar -->
>         <excludes>
>             <exclude>com.google.code.findbugs:jsr305</exclude>
>             <exclude>org.checkerframework:checker-qual</exclude>
>             <exclude>org.jetbrains:annotations</exclude>
>             <exclude>org.jetbrains.kotlin:kotlin-annotations-jvm</exclude>
>             <exclude>pl.tlinkowski.annotation:pl.tlinkowski.annotation.basic</exclude>
>         </excludes>
>     </artifactSet>
> </configuration>
> ```

</details>


## Rationale

TextChain aims to be a simple, easy-to-understand tool to manipulate and send 
Minecraft text components. It achieves that goal by editing components one
at a time as-needed in a linear fashion. TextChain is designed such that
users are only ever concerned with the most-recently created component
in the chain, as opposed to managing a tree-like structure directly
(as you do in standard Adventure).

Now, TextChain doesn't *replace* Adventure - it's an addition to it: 
a complimentary, alternative way to create Adventure components.
In fact, by leveraging Adventure's platform-agnostic library, 
TextChain runs wherever Adventure runs.

It wasn't always like that, however. When this library was originally created
(closed-source, then called "Tellable" in reference to the [`/tellraw`](https://minecraft.gamepedia.com/Commands/tellraw) 
command), it used BungeeCord's chat API. That wasn't *ideal* because it was
directly tied to a specific platform (i.e. Spigot and its derivatives),
but it got the job done as writing for other platforms wasn't necessarily
on the horizon. 

Much of the core functionality from that old library is intact as TextChain. 
However, in the process of migrating that legacy code, most of names for
things (such as method names, and of course, the library name itself)
have been updated to stay consistent with Adventure. It is a goal of this
project to always stay compatible with the latest-release version of
Adventure and match its overall naming conventions for component
actions and styles. 

### Java Version

This project targets JDK 11 because the Minecraft server community is
overwhelmingly headed in that direction. It hasn't quite received
universal adoption yet, but it has been sufficiently supported that
there's no real reason to target legacy versions like JDK 8 anymore.


## Usage

Coming soon... 😄
