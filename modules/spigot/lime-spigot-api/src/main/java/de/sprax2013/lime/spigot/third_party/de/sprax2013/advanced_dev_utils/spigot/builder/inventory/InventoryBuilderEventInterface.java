package de.sprax2013.lime.spigot.third_party.de.sprax2013.advanced_dev_utils.spigot.builder.inventory;

@Deprecated
public interface InventoryBuilderEventInterface {
    void onOpen(InventoryBuilderOpenedEvent event);

    void onClick(InventoryBuilderClickEvent event);

    void onClose(InventoryBuilderCloseEvent event);
}