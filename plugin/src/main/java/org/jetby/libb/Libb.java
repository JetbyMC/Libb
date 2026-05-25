package org.jetby.libb;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetby.libb.color.HashedSerializer;
import org.jetby.libb.color.SerializerType;
import org.jetby.libb.configuration.GuisConfiguration;
import org.jetby.libb.gui.AdvancedGui;
import org.jetby.libb.gui.GuiListener;
import org.jetby.libb.plugin.LibbPlugin;
import org.jetby.libb.util.Logger;

import java.util.HashSet;
import java.util.Set;

@Getter
public final class Libb extends LibbPlugin implements LibbApi {


    public static final Set<Plugin> HOOKED_PLUGINS = new HashSet<>();

    public GuisConfiguration guisConfiguration;

    public static Libb INSTANCE;

    @Override
    public void onLoad() {
        DependencyLoader.loadDependencies(this);
        if (!DependencyLoader.isNativeAdventure()) {
            try {
                AdventureReflect.init(DependencyLoader.getAdventureLoader());
            } catch (Exception e) {
                getLogger().severe("Failed to init AdventureReflect!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEnable() {

        LibbApi.init();
        INSTANCE = this;
        saveDefaultConfig();

        try {
            Settings.CONFIG_COLORIZER = (new HashedSerializer(
                    SerializerType.valueOf(getConfig().getString("serializer.type", "UNIFIED").toUpperCase()),
                    getConfig().getBoolean("serializer.cache.enabled", true), getConfig().getInt("serializer.cache.max-size", 500)
            ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Logger.info(this, "<#1CFB00>╔");
        Logger.info(this, "<#1CFB00>║  <#0AD7FB>Libb <red>v" + getVersion());
        Logger.info(this, "<#1CFB00>║");
        setBStats(this, 30288);
        setVersionUtil("https://raw.githubusercontent.com/MrJetby/Libb/refs/heads/master/VERSION");
        new LibbCommand(this).register();
        guisConfiguration = new GuisConfiguration(this);
        guisConfiguration.load();
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        Logger.info(this, "<#1CFB00>║  <#0AD7FB>" + LibbApi.Settings.PARSED_GUIS.size() + " guis loaded");
        Logger.info(this, "<#1CFB00>║");
        Logger.info(this, "<#1CFB00>║  <#1CFB00>Plugin enabled");
        Logger.info(this, "<#1CFB00>╚");

    }

    @Override
    public void onDisable() {
        Logger.info(this, "<#FB0000>╔");
        Logger.info(this, "<#FB0000>║  <#0AD7FB>Libb <red>v" + getVersion());

        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory topInventory = player.getOpenInventory().getTopInventory();
            if (!(topInventory instanceof AdvancedGui)) continue;
            topInventory.close();
        }
        Logger.info(this, "<#FB0000>║  <#FB0000>Plugin disabled");
        Logger.info(this, "<#FB0000>╚");
    }
}
