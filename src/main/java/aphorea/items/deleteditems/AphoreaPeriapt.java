package aphorea.items.deleteditems;

import aphorea.other.itemtype.AphoreaDeletedItem;
import necesse.engine.registries.ItemRegistry;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.InventoryItem;

import java.util.ArrayList;

public class AphoreaPeriapt extends AphoreaDeletedItem {
    public AphoreaPeriapt() {
        super();
    }

    public ArrayList<InventoryItem> items(PlayerMob player) {
        ArrayList<InventoryItem> itemsList = new ArrayList<>();
        itemsList.add(ItemRegistry.getItem("unstableperiapt").getDefaultItem(player, 1));
        itemsList.add(ItemRegistry.getItem("frozenperiapt").getDefaultItem(player, 1));
        itemsList.add(ItemRegistry.getItem("demonicperiapt").getDefaultItem(player, 1));
        return itemsList;
    }

}
