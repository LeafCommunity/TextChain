/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bukkit.internal;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BukkitVersion
{
    public static final Pattern PACKAGE_VERSION_PATTERN =
        Pattern.compile(
            "(?<package>org\\.bukkit\\.craftbukkit\\.(?<version>v(?<release>\\d+)_(?<major>\\d+)_R(?<revision>\\d+)))\\."
        );
    
    private static final ThrowsOr<BukkitVersion> INSTANCE =
        ThrowsOr.result(() -> new BukkitVersion(Bukkit.getServer().getClass().getCanonicalName()));
    
    public static BukkitVersion server() { return INSTANCE.getOrThrow(); }
    
    private final String craftBukkitPackage;
    private final String packageVersion;
    private final int release;
    private final int major;
    private final int revision;
    
    public BukkitVersion(String versionClassPath)
    {
        Matcher matcher = PACKAGE_VERSION_PATTERN.matcher(versionClassPath);
        
        if (!matcher.find())
        {
            throw new IllegalArgumentException("Invalid version (unsupported server): " + versionClassPath);
        }
        
        this.craftBukkitPackage = matcher.group("package");
        this.packageVersion = matcher.group("version");
        this.release = Integer.parseInt(matcher.group("release"));
        this.major = Integer.parseInt(matcher.group("major"));
        this.revision = Integer.parseInt(matcher.group("revision"));
    }
    
    @Override
    public String toString() { return packageVersion; }
    
    public String craftBukkitPackage() { return craftBukkitPackage; }
    
    public String packageVersion() { return packageVersion; }
    
    public int release() { return release; }
    
    public int major() { return major; }
    
    public int revision() { return revision; }
    
    public boolean isAtLeast(int release, int major)
    {
        return this.release >= release && this.major >= major;
    }
    
    private String versionedClassPath(String prefix, String className)
    {
        return String.join(".", prefix, packageVersion, className);
    }
    
    public String craftbukkit(String className)
    {
        return versionedClassPath("org.bukkit.craftbukkit", className);
    }
    
    public String nms(String className)
    {
        return (isAtLeast(1, 17))
            ? String.join(".", "net.minecraft", className)
            : versionedClassPath("net.minecraft.server", className);
    }
}
