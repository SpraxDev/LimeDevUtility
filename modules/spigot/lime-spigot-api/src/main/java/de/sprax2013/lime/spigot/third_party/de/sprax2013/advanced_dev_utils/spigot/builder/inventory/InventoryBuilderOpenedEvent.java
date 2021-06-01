package de.sprax2013.lime.spigot.third_party.de.sprax2013.advanced_dev_utils.spigot.builder.inventory;

import org.bukkit.entity.HumanEntity;

@Deprecated
public class InventoryBuilderOpenedEvent {
    private final HumanEntity entity;

    public InventoryBuilderOpenedEvent(HumanEntity entity) {
        this.entity = entity;
    }

    public HumanEntity getEntity() {
        return entity;
    }
}