//package de.sprax2013.lime.spigot.third_party.de.sprax2013.advanced_dev_utils.fake_player;
//
//import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
//import org.bukkit.entity.Player;
//
//@Deprecated
//public class FakePlayerInteractEvent {
//    private final FakePlayer npc;
//
//    private final Player p;
//
//    private final EntityUseAction action;
//
//    public FakePlayerInteractEvent(FakePlayer npc, Player p, EntityUseAction action) {
//        this.npc = npc;
//        this.p = p;
//        this.action = action;
//    }
//
//    /**
//     * @return The PlayerNPC
//     */
//    public FakePlayer getNPC() {
//        return npc;
//    }
//
//    /**
//     * @return The player
//     */
//    public Player getPlayer() {
//        return p;
//    }
//
//    /**
//     * @return The action
//     */
//    public InteractAction getAction() {
//        return InteractAction.getByProtocolLib(action);
//    }
//}