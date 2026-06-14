package org.jetby.libb.gui.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetby.libb.color.Serializer;
import org.jetby.libb.platform.PlatformMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ItemWrapper {


    private static final Class<?> COMPONENT_CLASS;
    private static final Method GET_COMPONENT;
    private static final Method SET_COMPONENT;
    private static final Method SET_STRINGS;
    private static final boolean NEW_API;

    static {
        Class<?> cc = null;
        Method get = null, set = null, setStr = null;
        boolean supported = false;

        try {
            cc = Class.forName("org.bukkit.inventory.meta.components.CustomModelDataComponent");
            get = ItemMeta.class.getMethod("getCustomModelDataComponent");
            set = ItemMeta.class.getMethod("setCustomModelDataComponent", cc);
            setStr = cc.getMethod("setStrings", List.class);

            get.setAccessible(true);
            set.setAccessible(true);
            setStr.setAccessible(true);

            supported = true;
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
        }

        COMPONENT_CLASS = cc;
        GET_COMPONENT = get;
        SET_COMPONENT = set;
        SET_STRINGS = setStr;
        NEW_API = supported;
    }


    @Nullable
    private String key;

    private List<Integer> slots;
    private ItemStack itemStack;
    private Consumer<InventoryClickEvent> onClick;
    private Serializer serializer;

    public Serializer serializer() {
        return serializer;
    }

    public void serializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public ItemWrapper(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemWrapper(@NotNull Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemWrapper(@NotNull Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    private void applyMeta(Consumer<ItemMeta> editor) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;
        editor.accept(meta);
        itemStack.setItemMeta(meta);
    }

    public Consumer<InventoryClickEvent> onClick() {
        return onClick;
    }

    public void onClick(Consumer<InventoryClickEvent> onClick) {
        this.onClick = onClick;
    }

    public List<Integer> slots() {
        return slots;
    }

    public void slots(@Range(from = 0, to = 54) Integer... slot) {
        this.slots = Arrays.asList(slot);
    }

    public ItemStack itemStack() {
        return itemStack;
    }

    public void itemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Material material() {
        return itemStack.getType();
    }

    public void material(Material material) {
        itemStack.setType(material);
    }

    public Component displayName() {
        ItemMeta meta = itemStack.getItemMeta();
        return meta != null ? PlatformMeta.getDisplayName(meta) : null;
    }

    public void displayName(Component displayName) {
        applyMeta(meta -> PlatformMeta.setDisplayName(meta, displayName));
    }

    public void setDisplayName(String text) {
        if (text == null) return;
        displayName(serializer == null ? Component.text(text) : serializer.deserialize(text));
    }

    public void displayName(String text) {
        setDisplayName(text);
    }

    public List<Component> lore() {
        ItemMeta meta = itemStack.getItemMeta();
        return meta != null ? PlatformMeta.getLore(meta) : null;
    }

    public void lore(List<Component> lore) {
        applyMeta(meta -> PlatformMeta.setLore(meta, lore));
    }

    public void setLore(String... lines) {
        List<Component> list = new ArrayList<>();
        for (String line : lines) {
            list.add(serializer == null ? Component.text(line) : serializer.deserialize(line));
        }
        lore(list);
    }

    public void setLore(List<String> lines) {
        List<Component> list = new ArrayList<>();
        for (String line : lines) {
            list.add(serializer == null ? Component.text(line) : serializer.deserialize(line));
        }
        lore(list);
    }

    public Object customModelDataComponent() {
        if (!NEW_API) return null;
        try {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null) return null;
            return GET_COMPONENT.invoke(meta);
        } catch (Exception e) {
            return null;
        }
    }

    public void customModelDataComponent(Object component) {
        if (!NEW_API || !COMPONENT_CLASS.isInstance(component)) return;
        applyMeta(meta -> {
            try {
                SET_COMPONENT.invoke(meta, component);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public int customModelData() {
        ItemMeta meta = itemStack.getItemMeta();
        return meta != null && meta.hasCustomModelData() ? meta.getCustomModelData() : 0;
    }


    public void customModelData(Object customModelData) {
        if (customModelData == null) return;

        if (!NEW_API) {
            if (customModelData instanceof Integer i) {
                applyMeta(meta -> meta.setCustomModelData(i));
            }
            return;
        }

        if (customModelData instanceof Integer i) {
            applyMeta(meta -> meta.setCustomModelData(i));
        } else if (COMPONENT_CLASS.isInstance(customModelData)) {
            applyMeta(meta -> {
                try {
                    SET_COMPONENT.invoke(meta, customModelData);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        } else if (customModelData instanceof String s) {
            applyMeta(meta -> {
                try {
                    Object component = GET_COMPONENT.invoke(meta);
                    SET_STRINGS.invoke(component, List.of(s));
                    SET_COMPONENT.invoke(meta, component);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    public boolean enchanted() {
        ItemMeta meta = itemStack.getItemMeta();
        return meta != null && meta.hasEnchants();
    }

    public void enchanted(boolean enchanted) {
        if (enchanted) {
            applyMeta(meta -> {
                meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            });
        }
    }

    public Map<Enchantment, Integer> enchantments() {
        return itemStack.getEnchantments();
    }

    public void enchantments(Map<Enchantment, Integer> enchantments) {
        itemStack.addUnsafeEnchantments(enchantments);

    }

    public List<ItemFlag> flags() {
        ItemMeta meta = itemStack.getItemMeta();
        return meta != null ? new ArrayList<>(meta.getItemFlags()) : new ArrayList<>();
    }

    public void flags(ItemFlag... flags) {
        applyMeta(meta -> meta.addItemFlags(flags));
    }

    public int amount() {
        return itemStack.getAmount();
    }

    public void amount(int amount) {
        itemStack.setAmount(amount);
    }

    public static Builder builder(@NotNull Material material) {
        return new Builder(material);
    }

    public @Nullable String key() {
        return key;
    }

    public void key(@Nullable String key) {
        this.key = key;
    }

    public static class Builder {
        private final Material material;
        private ItemStack itemStack;
        private List<Integer> slots;
        private int amount = 1;
        private Component displayName;
        private List<Component> lore;
        private Object customModelData;
        private boolean enchanted;
        private Map<Enchantment, Integer> enchantments;
        private List<ItemFlag> flags;
        private Consumer<InventoryClickEvent> onClick;
        private Serializer serializer;
        private String key;

        private Builder(@NotNull Material material) {
            this.material = material;
        }

        public Builder key(@Nullable String key) {
            this.key = key;
            return this;
        }

        public Builder enchantments(Map<Enchantment, Integer> enchantments) {
            this.enchantments = enchantments;
            return this;
        }

        public Builder serializer(Serializer serializer) {
            this.serializer = serializer;
            return this;
        }

        public Builder itemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
            return this;
        }

        public Builder slots(@Range(from = 0, to = 54) Integer... slot) {
            this.slots = Arrays.asList(slot);
            return this;
        }

        public Builder displayName(Component displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder setDisplayName(String text) {
            displayName(serializer == null ? Component.text(text) : serializer.deserialize(text));
            return this;
        }

        public Builder lore(List<Component> lore) {
            this.lore = lore;
            return this;
        }

        public Builder setLore(List<String> lines) {
            List<Component> list = new ArrayList<>();
            for (String line : lines) {
                list.add(serializer == null ? Component.text(line) : serializer.deserialize(line));
            }
            this.lore = list;
            return this;
        }

        public Builder customModelData(Object customModelData) {
            this.customModelData = customModelData;
            return this;
        }

        public Builder enchanted(boolean enchanted) {
            this.enchanted = enchanted;
            return this;
        }

        public Builder flags(ItemFlag... flags) {
            this.flags = Arrays.asList(flags);
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder onClick(Consumer<InventoryClickEvent> onClick) {
            this.onClick = onClick;
            return this;
        }

        public ItemWrapper build() {
            ItemWrapper wrapper = new ItemWrapper(
                    itemStack != null ? itemStack : new ItemStack(material, amount)
            );

            wrapper.key(key);
            wrapper.serializer(serializer);
            wrapper.slots = slots;
            wrapper.onClick = onClick;

            if (displayName != null) wrapper.displayName(displayName);
            if (lore != null) wrapper.lore(lore);
            if (customModelData != null) wrapper.customModelData(customModelData);
            if (enchanted) wrapper.enchanted(true);
            if (enchantments != null) wrapper.enchantments(enchantments);
            if (flags != null) wrapper.flags(flags.toArray(new ItemFlag[0]));

            return wrapper;
        }
    }
}