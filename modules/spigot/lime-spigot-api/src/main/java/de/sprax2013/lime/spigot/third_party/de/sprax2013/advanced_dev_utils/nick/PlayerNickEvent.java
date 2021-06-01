//package de.sprax2013.lime.spigot.third_party.de.sprax2013.advanced_dev_utils.nick;
//
//import org.bukkit.entity.Player;
//import org.bukkit.event.Event;
//import org.bukkit.event.HandlerList;
//
///**
// * This event is called after a player got nicked
// */
//@Deprecated
//public class PlayerNickEvent extends Event {
//    public static HandlerList handlers = new HandlerList();
//
//    private final Player p;
//
//    public PlayerNickEvent(Player p) {
//        this.p = p;
//    }
//
//    /**
//     * @return The player who got nicked
//     */
//    public Player getPlayer() {
//        return p;
//    }
//
//    public static HandlerList getHandlerList() {
//        return handlers;
//    }
//
//    @Override
//    public HandlerList getHandlers() {
//        return handlers;
//    }
//}