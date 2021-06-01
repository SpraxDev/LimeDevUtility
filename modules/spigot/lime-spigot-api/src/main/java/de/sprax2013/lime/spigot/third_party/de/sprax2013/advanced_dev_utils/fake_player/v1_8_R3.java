//package de.sprax2013.lime.spigot.third_party.de.sprax2013.advanced_dev_utils.fake_player;
//
//import com.mojang.authlib.GameProfile;
//import de.sprax2013.advanced_dev_utils.spigot.utils.MCPacketUtils;
//import de.sprax2013.lime.spigot.reflections.BukkitReflectionUtils;
//import net.minecraft.server.v1_8_R3.DataWatcher;
//import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutEntityLook;
//import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
//import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
//import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
//import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
//import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;
//import org.bukkit.Location;
//import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
//
//import java.util.Collections;
//import java.util.UUID;
//
//@Deprecated
//public class v1_8_R3 {
//    static PacketPlayOutNamedEntitySpawn createSpawnPacket(int entityID, UUID uuid, Location loc) {
//        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
//
//        // IDs
//        BukkitReflectionUtils.setValue(packet, "a", entityID);
//        BukkitReflectionUtils.setValue(packet, "b", uuid);
//
//        // Location
//        BukkitReflectionUtils.setValue(packet, "c", BukkitReflectionUtils.getFixPoint(loc.getX()));
//        BukkitReflectionUtils.setValue(packet, "d", BukkitReflectionUtils.getFixPoint(loc.getY()));
//        BukkitReflectionUtils.setValue(packet, "e", BukkitReflectionUtils.getFixPoint(loc.getZ()));
//        BukkitReflectionUtils.setValue(packet, "f", BukkitReflectionUtils.getFixAngle(loc.getYaw()));
//        BukkitReflectionUtils.setValue(packet, "g", BukkitReflectionUtils.getFixAngle(loc.getPitch()));
//
//        // Current Item
//        BukkitReflectionUtils.setValue(packet, "h", 0);
//
//        // MetaData
//        DataWatcher dW = new DataWatcher(null);
//
//        dW.a(6, 20F);
//        dW.a(10, (byte) 127);
//
//        BukkitReflectionUtils.setValue(packet, "i", dW);
//
//        return packet;
//    }
//
//    static PacketPlayOutPlayerInfo createAddPlayerInfoPacket(GameProfile gp) {
//        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER);
//        BukkitReflectionUtils.setValue(packet, "b", Collections.singletonList(
//                packet.new PlayerInfoData(gp, 1, EnumGamemode.NOT_SET, CraftChatMessage.fromString(gp.getName())[0])));
//
//        return packet;
//    }
//
//    static PacketPlayOutPlayerInfo createRemovePlayerInfoPacket(GameProfile gp) {
//        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER);
//        BukkitReflectionUtils.setValue(packet, "b", Collections.singletonList(
//                packet.new PlayerInfoData(gp, 1, EnumGamemode.NOT_SET, CraftChatMessage.fromString(gp.getName())[0])));
//
//        return packet;
//    }
//
//    static PacketPlayOutEntityLook createEntityLookPacket(int entityID, float yaw, float pitch) {
//        return new PacketPlayOutEntityLook(entityID, BukkitReflectionUtils.getFixAngle(yaw), BukkitReflectionUtils.getFixAngle(pitch),
//                true);
//    }
//
//    static PacketPlayOutEntityHeadRotation createEntityHeadRotationPacket(int entityID, float yaw) {
//        PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation();
//        BukkitReflectionUtils.setValue(packet, "a", entityID);
//        BukkitReflectionUtils.setValue(packet, "b", BukkitReflectionUtils.getFixAngle(yaw));
//
//        return packet;
//    }
//}