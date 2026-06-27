package org.jetby.libb.gui.parser;

import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetby.libb.action.record.ActionBlock;

import java.util.*;

public class Item {


    private UUID uniqueKey;
    private @NotNull ItemStack itemStack;
    private @Nullable String type;
    private @Nullable String displayName;
    private @Nullable List<String> lore;
    private @NotNull Material material;
    private @NotNull List<Integer> slots = new ArrayList<>();
    private @Nullable Set<ItemFlag> flags;
    private @Nullable Map<Enchantment, Integer> enchantments;
    private @Nullable ConfigurationSection section;
    private @NotNull Map<ClickType, ActionBlock> onClick = new HashMap<>();
    private @NotNull List<String> viewRequirements = new ArrayList<>();
    private int priority = Integer.MAX_VALUE;
    private boolean enchanted;
    private Object customModelData;
    private int amount;

    public Item(@NotNull Material material) {
        this.material = material;
        this.itemStack = new ItemStack(material);
        this.uniqueKey = UUID.randomUUID();
    }

    public Item(@NotNull Material material, int amount) {
        this.material = material;
        this.amount = amount;
        this.itemStack = new ItemStack(material, amount);
        this.uniqueKey = UUID.randomUUID();
    }

    public Item(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
        ItemMeta meta = itemStack.getItemMeta();
        String name = meta.getDisplayName();
        this.displayName = name.isEmpty() ? null : name;
        this.lore = meta.getLore();
        this.amount = itemStack.getAmount();
        this.material = itemStack.getType();
        this.customModelData = meta.hasCustomModelData() ? meta.getCustomModelData() : 0;
        this.enchantments = itemStack.getEnchantments();
        this.flags = meta.getItemFlags();
        this.uniqueKey = UUID.randomUUID();
    }

    public @NotNull ItemStack itemStack() {
        return itemStack;
    }

    public void itemStack(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int amount() {
        return amount;
    }

    public void amount(int amount) {
        this.amount = amount;
    }

    public @Nullable String type() {
        return type;
    }

    public void type(@Nullable String type) {
        this.type = type;
    }

    public @Nullable String displayName() {
        return displayName;
    }

    public void displayName(@Nullable String displayName) {
        this.displayName = displayName;
    }

    public @Nullable List<String> lore() {
        return lore;
    }

    public void lore(@Nullable List<String> lore) {
        this.lore = lore;
    }

    public @NotNull Material material() {
        return material;
    }

    public void material(@NotNull Material material) {
        this.material = material;
    }

    public @NotNull List<Integer> slots() {
        return slots;
    }

    public void slots(@NotNull List<Integer> slots) {
        this.slots = slots;
    }

    public @Nullable Set<ItemFlag> flags() {
        return flags;
    }

    public void flags(@Nullable Set<ItemFlag> flags) {
        this.flags = flags;
    }

    /**
     * @deprecated  use {@link #flags(Set)} instead.
     */
    @Deprecated(since = "1.2.2")
    public void flags(@Nullable List<ItemFlag> flags) {
        this.flags = new HashSet<>(flags);
    }

    public @Nullable Map<Enchantment, Integer> enchantments() {
        return enchantments;
    }


    public void enchantments(@Nullable List<Enchantment> enchantments) {
        if (this.enchantments ==null) {
            this.enchantments = new HashMap<>();
        }
        for (Enchantment enchantment : enchantments) {
            this.enchantments.put(enchantment, 1);
        }
    }

    public void enchantments(@Nullable Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    public @NotNull Map<ClickType, ActionBlock> onClick() {
        return onClick;
    }

    public void onClick(@NotNull Map<ClickType, ActionBlock> onClick) {
        this.onClick = onClick;
    }

    public Object customModelData() {
        return customModelData;
    }

    public void customModelData(Object customModelData) {
        this.customModelData = customModelData;
    }

    public @Nullable ConfigurationSection section() {
        return section;
    }

    public void section(@Nullable ConfigurationSection section) {
        this.section = section;
    }

    public @NotNull List<String> viewRequirements() {
        return viewRequirements;
    }

    public void viewRequirements(@NotNull List<String> viewRequirements) {
        this.viewRequirements = viewRequirements;
    }

    /**
     * Lower number = higher priority. Default is Integer.MAX_VALUE (lowest).
     */
    public int priority() {
        return priority;
    }

    public void priority(int priority) {
        this.priority = priority;
    }

    public boolean enchanted() {
        return enchanted;
    }

    public void enchanted(boolean enchanted) {
        this.enchanted = enchanted;
    }

    public UUID uniqueKey() {
        return uniqueKey;
    }

    public Item clone() {
        Item copy = new Item(this.material);
        copy.displayName = this.displayName;
        copy.lore = this.lore == null ? null : new ArrayList<>(this.lore);
        copy.amount = this.amount;
        copy.customModelData = this.customModelData;
        copy.flags = this.flags == null ? null : new HashSet<>(this.flags);
        copy.enchanted = this.enchanted;
        copy.enchantments = this.enchantments == null ? null : new HashMap<>(this.enchantments);
        copy.itemStack = this.itemStack.clone();
        copy.uniqueKey = this.uniqueKey;
        copy.type = this.type;
        copy.slots = new ArrayList<>(this.slots);
        copy.section = this.section;
        copy.onClick = new HashMap<>(this.onClick);
        copy.viewRequirements = new ArrayList<>(this.viewRequirements);
        copy.priority = this.priority;
        return copy;
    }
}