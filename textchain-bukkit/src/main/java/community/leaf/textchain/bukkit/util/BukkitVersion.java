package community.leaf.textchain.bukkit.util;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BukkitVersion
{
    public static final Pattern PACKAGE_VERSION_PATTERN =
        Pattern.compile(
            "org\\.bukkit\\.craftbukkit\\.(?<version>v(?<release>\\d+)_(?<major>\\d+)_R(?<revision>\\d+))\\."
        );
    
    private static final MaybeExceptional<BukkitVersion> INSTANCE =
        MaybeExceptional.of(() -> new BukkitVersion(Bukkit.getServer().getClass().getCanonicalName()));
    
    public static BukkitVersion getServerVersion() { return INSTANCE.getOrRethrow(); }
    
    public final String packageVersion;
    public final int release;
    public final int major;
    public final int revision;
    
    public BukkitVersion(String versionClassPath)
    {
        Matcher matcher = PACKAGE_VERSION_PATTERN.matcher(versionClassPath);
        
        if (!matcher.find())
        {
            throw new IllegalArgumentException("Invalid version (unsupported server): " + versionClassPath);
        }
    
        this.packageVersion = matcher.group("version");
        this.release = Integer.parseInt(matcher.group("release"));
        this.major = Integer.parseInt(matcher.group("major"));
        this.revision = Integer.parseInt(matcher.group("revision"));
    }
    
    @Override
    public String toString() { return packageVersion; }
    
    private String classPath(String prefix, String className)
    {
        return String.join(".", prefix, packageVersion, className);
    }
    
    public String nms(String className) { return classPath("net.minecraft.server", className); }
    
    public String craftbukkit(String className) { return classPath("org.bukkit.craftbukkit", className); }
}
