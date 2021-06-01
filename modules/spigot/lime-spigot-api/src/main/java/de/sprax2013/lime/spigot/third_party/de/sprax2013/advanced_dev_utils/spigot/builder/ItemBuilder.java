package de.sprax2013.lime.spigot.third_party.de.sprax2013.advanced_dev_utils.spigot.builder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.sprax2013.lime.spigot.reflections.BukkitReflectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * With this class you can create an ItemStack with custom MetaData within one
 * line!<br>
 * <br>
 * Example: <i>new
 * ItemBuilder(Material.SKULL_ITEM).setSkullOwner("Sprax2013").build();</i><br>
 * <br>
 * Will create a SKULL_ITEM setting the SkullOwner to <i>Sprax2013</i> resulting
 * in automatically get it's subID set to <i>3</i>.
 */
@Deprecated
public class ItemBuilder {
    private ItemStack item;
    private ItemMeta meta;

    /**
     * Instantiates a new ItemBuilder.
     *
     * @param type the material for the item
     */
    public ItemBuilder(Material type) {
        if (type == null) {
            throw new IllegalArgumentException("Can not create an ItemBuilder-Instance with 'type' being null");
        }

        item = new ItemStack(type);
    }

    /**
     * Instantiates a new ItemBuilder.
     *
     * @param type   the material for the item
     * @param amount the amount of the resulting ItemStack
     */
    public ItemBuilder(Material type, int amount) {
        if (type == null) {
            throw new IllegalArgumentException("Can not create an ItemBuilder-Instance with 'type' being null");
        }

        item = new ItemStack(type, amount);
    }

    /**
     * Instantiates a new ItemBuilder.
     *
     * @param item an existing ItemStack that should be edited
     */
    public ItemBuilder(ItemStack item) {
        if (item == null) {
            throw new IllegalArgumentException("Can not create an ItemBuilder-Instance with 'item' being null");
        }

        this.item = item.clone();
    }

    /**
     * Sets the material of the item.<br>
     * <b>This could remove some changes that depend on the material (for example
     * the set color)</b>
     *
     * @param mat The Material
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder setMaterial(Material mat) {
        item.setType(mat);

        return this;
    }

    /**
     * Sets the display name of the item.
     *
     * @param displayname the display name
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder setDisplayName(String displayname) {
        getItemMeta().setDisplayName(displayname);

        return this;
    }

    /**
     * Sets the lore.
     *
     * @param lore the lore
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder setLore(String lore) {
        if (lore != null) {
            getItemMeta().setLore(Collections.singletonList("§r" + lore));
        } else {
            getItemMeta().setLore(null);
        }

        return this;
    }

    /**
     * Sets the lore.
     *
     * @param lore     The lore
     * @param moreLore More of the lore
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder setLore(String lore, String... moreLore) {
        if (lore != null) {
            List<String> list = new ArrayList<>(moreLore.length + 1);
            list.add("§r" + lore);

            for (String s : moreLore) {
                list.add("§r" + s);
            }

            getItemMeta().setLore(list);
        } else {
            getItemMeta().setLore(null);
        }

        return this;
    }

    /**
     * Sets the lore of an Item.
     *
     * @param lore the lore
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder setLore(List<String> lore) {
        getItemMeta().setLore(lore);

        return this;
    }

    /**
     * Will add a String to an existing lore or add a new lore.
     *
     * @param lore the lore of the item
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder addLore(String lore) {
        if (lore != null) {
            if (getItemMeta().hasLore()) {
                List<String> lores = getItemMeta().getLore();
                lores.add("§r" + lore);

                getItemMeta().setLore(lores);
            } else {
                setLore(lore);
            }
        }

        return this;
    }

    public ItemBuilder addLore(String lore, int atIndex) {
        if (lore != null) {
            if (getItemMeta().hasLore()) {
                List<String> lores = getItemMeta().getLore();
                lores.add(atIndex, "§r" + lore);

                getItemMeta().setLore(lores);
            } else {
                setLore(lore);
            }
        }

        return this;
    }

    /**
     * Clears the lore of the item.
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder clearLore() {
        getItemMeta().setLore(null);

        return this;
    }

    /**
     * Sets the amount of the item.
     *
     * @param amount the desired amount
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);

        return this;
    }

    /**
     * Sets the remaining durability of the item. (type.getMaxDurability() -
     * durability)<br>
     * <b>Not to be confused with {@link #setDamage(Integer) setDamage} !</b>
     *
     * @param durability the durability
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder setDurabilityLeft(Integer durability) {
        item.setDurability((short) (item.getType().getMaxDurability() - durability.shortValue()));

        return this;
    }

    /**
     * Sets the damage the item has<br>
     * <b>Not to be confused with {@link #setDurabilityLeft(Integer)
     * setDurabilityLeft} !</b>
     *
     * @param damage the damage of the item
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder setDamage(Integer damage) {
        setDamage(damage.shortValue());

        return this;
    }

    /**
     * Sets the damage the item has<br>
     * <b>Not to be confused with {@link #setDurabilityLeft(Integer)
     * setDurabilityLeft} !</b>
     *
     * @param damage the damage of the item
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder setDamage(Short damage) {
        item.setDurability(damage);

        return this;
    }

    /**
     * Adds an enchantment.
     *
     * @param type  Enchantment type
     * @param level level of the enchantment
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder addEnchantment(Enchantment type, int level) {
        getItemMeta().addEnchant(type, level, true);

        return this;
    }

    /**
     * Adds an enchantment. Will remove it (or won't set one) if
     * <i>removeEnchantmentWhenLevelSmaler1</i> = true
     *
     * @param type                              Enchantment type
     * @param level                             level of the enchantment
     * @param removeEnchantmentWhenLevelSmaler1 if true, level &lt; 1 will result in
     *                                          removing or not setting an
     *                                          enchantment
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder addEnchantment(Enchantment type, int level, boolean removeEnchantmentWhenLevelSmaler1) {
        if (removeEnchantmentWhenLevelSmaler1 && level < 1) {
            getItemMeta().removeEnchant(type);
        } else {
            getItemMeta().addEnchant(type, level, true);
        }

        return this;
    }

    /**
     * Adds the given ItemFlags.
     *
     * @param flags the flags
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder addItemFlag(ItemFlag... flags) {
        getItemMeta().addItemFlags(flags);

        return this;
    }

    /**
     * Removes the given ItemFlags.
     *
     * @param flags the flags
     *
     * @return the instantiated ItemBuilder
     */
    public ItemBuilder removeItemFlag(ItemFlag... flags) {
        getItemMeta().removeItemFlags(flags);

        return this;
    }

//    /**
//     * Sets the SkullOwner.<br>
//     * Will automatically set the items subID to <i>3</i>
//     *
//     * @param owner the name of the player
//     *
//     * @return the instantiated ItemBuilder
//     */
//    @SuppressWarnings("deprecation")
//    public ItemBuilder setSkullOwner(String owner) {
//        if (getItemMeta() instanceof SkullMeta) {
//            ((SkullMeta) getItemMeta()).setOwner(owner == null ? "" : owner);
//
//            setSubID(3);
//        }
//
//        return this;
//    }

//    /**
//     * Sets the SkullOwner.<br>
//     * Will automatically set the items subID to <i>3</i>
//     *
//     * @param owner the player
//     *
//     * @return the instantiated ItemBuilder
//     */
//    @SuppressWarnings("deprecation")
//    public ItemBuilder setSkullOwner(OfflinePlayer owner) {
//        if (getItemMeta() instanceof SkullMeta) {
//            try {
//                ((SkullMeta) getItemMeta()).setOwningPlayer(owner);
//            } catch (@SuppressWarnings("unused") NoSuchMethodError ex) {
//                ((SkullMeta) getItemMeta()).setOwner(owner.getName());
//            }
//
//            setSubID(3);
//        }
//
//        return this;
//    }

    public ItemBuilder setSkullSkin(String skinURL) {
        if (getItemMeta() instanceof SkullMeta) {
            try {
                GameProfile profile = null;

                if (skinURL != null) {
                    profile = new GameProfile(UUID.randomUUID(), null);

                    profile.getProperties().put("textures", new Property("textures", Base64.getEncoder()
                            .encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"" + skinURL + "\"}}}").getBytes())));
                }

                try {
                    Method profileMethod = BukkitReflectionUtils.getNMSClass("org.bukkit.craftbukkit.?.inventory.CraftMetaSkull")
                            .getDeclaredMethod("setProfile", GameProfile.class);
                    profileMethod.setAccessible(true);

                    profileMethod.invoke(meta, profile);
                } catch (NoSuchMethodException ex) {
                    Field profileField = BukkitReflectionUtils.getNMSClass("org.bukkit.craftbukkit.?.inventory.CraftMetaSkull")
                            .getDeclaredField("profile");
                    profileField.setAccessible(true);

                    profileField.set(meta, profile);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return this;
    }

    /**
     * Builds and returns a copy of the ItemStack.<br>
     * Returning a copy of it makes it possible to use this ItemBuilder instance
     * again if wanted.
     *
     * @return the resulting ItemStack
     */
    public ItemStack build() {
        item.setItemMeta(getItemMeta());

        return new ItemStack(item);
    }

    private ItemMeta getItemMeta() {
        if (meta == null && item != null) {
            meta = item.getItemMeta();
        }

        return meta;
    }
}