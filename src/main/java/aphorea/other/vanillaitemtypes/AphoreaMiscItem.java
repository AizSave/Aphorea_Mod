package aphorea.other.vanillaitemtypes;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;
import necesse.inventory.item.miscItem.CraftingGuideBookItem;
import necesse.inventory.item.mountItem.WoodBoatMountItem;
import necesse.inventory.item.placeableItem.consumableItem.ConsumableItem;

abstract public class AphoreaMiscItem extends Item {
    public AphoreaMiscItem(int stackSize) {
        super(stackSize);
    }

    @Override
    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("global", "aphorea"));
        return tooltips;
    }

}
