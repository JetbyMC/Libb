package org.jetby.libb.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryLoader {

    private static final Map<String, URLClassLoader> CACHE = new HashMap<>();

    public static synchronized URLClassLoader load(Plugin plugin, String cacheKey, String repo, List<Dependency> dependencies) {
        URLClassLoader cached = CACHE.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        File libsFolder = new File("plugins" + File.separator + "Libb" + File.separator + "libs");
        libsFolder.mkdirs();

        List<URL> urls = new ArrayList<>();

        for (Dependency dependency : dependencies) {
            if (isAvailable(dependency.checkClass())) {
                plugin.getLogger().info("Found natively: " + dependency.checkClass());
                continue;
            }

            String groupPath = dependency.groupId().replace(".", "/");
            String jarName = dependency.artifactId() + "-" + dependency.version() + ".jar";
            String fullUrl = repo + groupPath + "/" + dependency.artifactId() + "/" + dependency.version() + "/" + jarName;

            try {
                File jar = downloadJar(fullUrl, libsFolder, jarName, plugin);
                urls.add(jar.toURI().toURL());
                plugin.getLogger().info("Loaded library " + fullUrl);
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to load: " + fullUrl);
                e.printStackTrace();
            }
        }

        URLClassLoader loader = new URLClassLoader(
                cacheKey,
                urls.toArray(new URL[0]),
                LibraryLoader.class.getClassLoader()
        );

        CACHE.put(cacheKey, loader);
        return loader;
    }

    private static boolean isAvailable(String className) {
        if (className == null) {
            return false;
        }

        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException ignored) {}

        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            String path = className.replace(".", "/") + ".class";
            if (p.getClass().getClassLoader().getResource(path) != null) {
                return true;
            }
        }

        return false;
    }

    private static File downloadJar(String link, File folder, String fileName, Plugin plugin) throws IOException {
        File output = new File(folder, fileName);
        if (output.exists()) return output;

        plugin.getLogger().info("Downloading Library: " + link);
        try (InputStream in = new URL(link).openStream();
             FileOutputStream fos = new FileOutputStream(output)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
        }
        return output;
    }

    public record Dependency(String groupId, String artifactId, String version, String checkClass) {}
}