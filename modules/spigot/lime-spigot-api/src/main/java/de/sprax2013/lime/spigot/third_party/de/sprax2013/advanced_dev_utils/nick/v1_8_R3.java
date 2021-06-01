//package de.sprax2013.lime.spigot.third_party.de.sprax2013.advanced_dev_utils.nick;
//
//import com.mojang.authlib.GameProfile;
//import de.sprax2013.advanced_dev_utils.nick.PlayerNick;
//import de.sprax2013.advanced_dev_utils.spigot.Main;
//import de.sprax2013.advanced_dev_utils.spigot.utils.MCPacketUtils;
//import net.minecraft.server.v1_8_R3.EntityPlayer;
//import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
//import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
//import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
//import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
//import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
//import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
//import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
//import net.minecraft.server.v1_8_R3.PacketPlayOutRespawn;
//import org.bukkit.Bukkit;
//import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
//import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
//import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
//import org.bukkit.entity.Player;
//import org.bukkit.plugin.java.JavaPlugin;
//
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Deprecated
//public class v1_8_R3 {
//    /**
//     * @see PlayerNick#nickPlayer(JavaPlugin, Player, UUID, Runnable)
//     */
//    static void nickPlayer(Player p, GameProfile publicGP, GameProfile selfGP) {
//        EntityPlayer eP = ((CraftPlayer) p).getHandle();
//
//        PacketPlayOutPlayerInfo removeInfo = createRemovePlayerInfoPacket(eP);
//
//        PacketPlayOutPlayerInfo addInfo = createAddPlayerInfoPacket(eP, publicGP);
//
//        PlayerNick.playersNick.put(p.getUniqueId(), publicGP);
//
//        Set<? extends Player> onlinePlayerNotP = Bukkit.getOnlinePlayers().stream()
//                .filter(online -> online != p && online.canSee(p)).collect(Collectors.toSet());
//
//        MCPacketUtils.sendPacket(onlinePlayerNotP, new PacketPlayOutEntityDestroy(p.getEntityId()));
//        MCPacketUtils.sendPacket(onlinePlayerNotP, removeInfo);
//
//        PlayerNick.sleep(125);
//
//        MCPacketUtils.sendPacket(onlinePlayerNotP, addInfo);
//        MCPacketUtils.sendPacket(onlinePlayerNotP, new PacketPlayOutNamedEntitySpawn(eP));
//
//        // Update Head-Rotation
//        MCPacketUtils.sendPacket(onlinePlayerNotP,
//                new PacketPlayOutEntityHeadRotation(eP, MCPacketUtils.getFixAngle(p.getLocation().getYaw())));
//
//        // if (updateSelf) {
//        MCPacketUtils.sendPacket(p, removeInfo);
//
//        PlayerNick.sleep(125);
//
//        MCPacketUtils.sendPacket(p, createAddPlayerInfoPacket(eP, (selfGP == null ? publicGP : selfGP)));
//
//        PlayerNick.sleep(125);
//
//        // Respawn Player
//        MCPacketUtils.sendPacket(p, createPlayerRespawnPacket(eP));
//
//        // Avoid Client stuck in void & wrong movement false-positive (with fake
//        // PacketID -> Server won't check it for false movement)
//        MCPacketUtils.sendPacket(p, new PacketPlayOutPosition(p.getLocation().getX(), p.getLocation().getY(),
//                p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch(), new HashSet<>()));
//
//        // Prevent Client-Side Bugs
//        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
//            // Update Client-Side EXP
//            p.setExp(p.getExp());
//
//            // Update Client-Side Abilities
//            p.setWalkSpeed(p.getWalkSpeed());
//
//            // Update Client-Side
//            p.setHealthScale(p.getHealthScale());
//
//            // Update Client-Side Inventory-Content
//            p.updateInventory();
//
//            // Update Client-Side Item-In-Hand
//            Object packet = new PacketPlayOutEntityEquipment(p.getEntityId(), 0,
//                    CraftItemStack.asNMSCopy(p.getInventory().getItemInHand()));
//            for (Player inWorld : p.getWorld().getPlayers()) {
//                if (p != inWorld) {
//                    MCPacketUtils.sendPacket(inWorld, packet);
//                }
//            }
//        });
//        // }
//    }
//
//    /**
//     * @see PlayerNick#unNickPlayer(JavaPlugin, Player, Runnable)
//     */
//    static void unNickPlayer(Player p) {
//        EntityPlayer eP = ((CraftPlayer) p).getHandle();
//
//        PacketPlayOutPlayerInfo removeInfo = createRemovePlayerInfoPacket(eP);
//
//        PacketPlayOutPlayerInfo addInfo = createAddPlayerInfoPacket(eP, eP.getProfile());
//
//        PlayerNick.playersNick.remove(p.getUniqueId());
//
//        Set<? extends Player> onlinePlayerNotP = Bukkit.getOnlinePlayers().stream()
//                .filter(online -> online != p && online.canSee(p)).collect(Collectors.toSet());
//
//        MCPacketUtils.sendPacket(onlinePlayerNotP, new PacketPlayOutEntityDestroy(p.getEntityId()));
//        MCPacketUtils.sendPacket(onlinePlayerNotP, removeInfo);
//
//        PlayerNick.sleep(125);
//
//        MCPacketUtils.sendPacket(onlinePlayerNotP, addInfo);
//        MCPacketUtils.sendPacket(onlinePlayerNotP, new PacketPlayOutNamedEntitySpawn(eP));
//
//        // Update Head-Rotation
//        MCPacketUtils.sendPacket(onlinePlayerNotP,
//                new PacketPlayOutEntityHeadRotation(eP, MCPacketUtils.getFixAngle(p.getLocation().getYaw())));
//
//        // if (updateSelf) {
//        MCPacketUtils.sendPacket(p, removeInfo);
//
//        PlayerNick.sleep(125);
//
//        MCPacketUtils.sendPacket(p, addInfo);
//
//        PlayerNick.sleep(125);
//
//        // Respawn Player
//        MCPacketUtils.sendPacket(p, createPlayerRespawnPacket(eP));
//
//        // Avoid Client stuck in void & wrong movement false-positive (with fake
//        // PacketID -> Server won't check it for false movement)
//        MCPacketUtils.sendPacket(p, new PacketPlayOutPosition(p.getLocation().getX(), p.getLocation().getY(),
//                p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch(), new HashSet<>()));
//
//        // Prevent Client-Side Bugs
//        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
//            // Update Client-Side EXP
//            p.setExp(p.getExp());
//
//            // Update Client-Side Abilities
//            p.setWalkSpeed(p.getWalkSpeed());
//
//            // Update Client-Side
//            p.setHealthScale(p.getHealthScale());
//
//            // Update Client-Side Inventory-Content
//            p.updateInventory();
//
//            // Update Client-Side Item-In-Hand
//            Object packet = new PacketPlayOutEntityEquipment(p.getEntityId(), 0,
//                    CraftItemStack.asNMSCopy(p.getInventory().getItemInHand()));
//            for (Player inWorld : p.getWorld().getPlayers()) {
//                if (p != inWorld) {
//                    MCPacketUtils.sendPacket(inWorld, packet);
//                }
//            }
//        });
//        // }
//    }
//
//    // Packets
//    private static PacketPlayOutPlayerInfo createRemovePlayerInfoPacket(EntityPlayer eP) {
//        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
//        MCPacketUtils.setValue(packet, "a", EnumPlayerInfoAction.REMOVE_PLAYER);
//        MCPacketUtils.setValue(packet, "b", Collections.singletonList(packet.new PlayerInfoData(eP.getProfile(), -1, null, null)));
//
//        return packet;
//    }
//
//    private static PacketPlayOutPlayerInfo createAddPlayerInfoPacket(EntityPlayer eP, GameProfile publicGP) {
//        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
//        MCPacketUtils.setValue(packet, "a", EnumPlayerInfoAction.ADD_PLAYER);
//        MCPacketUtils.setValue(packet, "b", Collections.singletonList(packet.new PlayerInfoData(publicGP, eP.ping,
//                eP.playerInteractManager.getGameMode(), CraftChatMessage.fromString(publicGP.getName())[0])));
//
//        return packet;
//    }
//
//    @SuppressWarnings("deprecation")
//    private static PacketPlayOutRespawn createPlayerRespawnPacket(EntityPlayer eP) {
//        return new PacketPlayOutRespawn(eP.getWorld().getWorld().getEnvironment().getId(),
//                eP.getWorld().getDifficulty(), eP.getWorld().getWorldData().getType(),
//                eP.playerInteractManager.getGameMode());
//    }
//}