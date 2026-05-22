package org.jetby.libb.platform;

public class PlatformDetector {


    public static Platform detect() {

        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return Platform.PAPER; // Folia
        } catch (ClassNotFoundException ignored) {
        }

        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return Platform.PAPER;
        } catch (ClassNotFoundException ignored) {
        }

        return Platform.SPIGOT;
    }

    public static boolean isPaper() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true; // Folia
        } catch (ClassNotFoundException ignored) {
        }

        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true; // Paper
        } catch (ClassNotFoundException ignored) {
        }

        return false;
    }

    public static boolean hasAdventure() {
        try {
            Class.forName("net.kyori.adventure.text.Component");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
