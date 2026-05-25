package org.jetby.libb;

import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DependencyLoader {

    @Getter
    private static URLClassLoader adventureLoader;
    private static Object gsonSerializer;
    private static Method gsonDeserialize;
    @Getter
    private static boolean nativeAdventure = false;

    private static final String VERSION = "4.17.0";
    private static final String REPO = "https://repo1.maven.org/maven2";

    private static final Map<String, String> ARTIFACTS = new LinkedHashMap<>();

    static {
        ARTIFACTS.put("adventure-api", VERSION);
        ARTIFACTS.put("adventure-text-minimessage", VERSION);
        ARTIFACTS.put("adventure-text-serializer-legacy", VERSION);
        ARTIFACTS.put("adventure-text-serializer-gson", VERSION);
        ARTIFACTS.put("adventure-text-serializer-json", VERSION);
        ARTIFACTS.put("option", "1.0.0");
    }

    public static void loadDependencies(Plugin plugin) {
        File libsFolder = new File(plugin.getDataFolder(), "libs");
        libsFolder.mkdirs();

        try {
            Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
            nativeAdventure = true;
            plugin.getLogger().info("Adventure found natively.");
            return;
        } catch (ClassNotFoundException ignored) {
        }

        List<URL> urls = new ArrayList<>();
        for (Map.Entry<String, String> entry : ARTIFACTS.entrySet()) {
            String artifactId = entry.getKey();
            String version = entry.getValue();
            String jarName = artifactId + "-" + version + ".jar";
            String fullUrl = REPO + "/net/kyori/" + artifactId + "/" + version + "/" + jarName;
            try {
                File jar = downloadJar(fullUrl, libsFolder, jarName, plugin);
                urls.add(jar.toURI().toURL());
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to download: " + fullUrl);
                e.printStackTrace();
            }
        }

        adventureLoader = new URLClassLoader(
                "libb-adventure",
                urls.toArray(new URL[0]),
                DependencyLoader.class.getClassLoader()
        );

        try {
            Class<?> gsonClass = adventureLoader.loadClass("net.kyori.adventure.text.serializer.gson.GsonComponentSerializer");
            Method gsonMethod = gsonClass.getMethod("gson");
            gsonSerializer = gsonMethod.invoke(null);
            gsonDeserialize = gsonClass.getMethod("serialize", adventureLoader.loadClass("net.kyori.adventure.text.Component"));
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to init gson bridge!");
            e.printStackTrace();
        }

        plugin.getLogger().info("Adventure classloader created with " + urls.size() + " jars.");
    }

    public static String componentToJson(Object component) {
        if (gsonSerializer == null || gsonDeserialize == null) return "{\"text\":\"\"}";
        try {
            return (String) gsonDeserialize.invoke(gsonSerializer, component);
        } catch (Exception e) {
            return "{\"text\":\"\"}";
        }
    }

    private static File downloadJar(String urlStr, File folder, String fileName, Plugin plugin) throws IOException {
        File output = new File(folder, fileName);
        if (output.exists()) return output;

        plugin.getLogger().info("Downloading: " + urlStr);
        try (InputStream in = new URL(urlStr).openStream();
             FileOutputStream fos = new FileOutputStream(output)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
        }
        return output;
    }
}