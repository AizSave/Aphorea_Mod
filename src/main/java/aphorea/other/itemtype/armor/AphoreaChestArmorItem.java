package aphorea.other.itemtype.armor;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.armorItem.ChestArmorItem;

abstract public class AphoreaChestArmorItem extends ChestArmorItem {
    public AphoreaChestArmorItem(int armorValue, int enchantCost, Rarity rarity, String bodyTextureName, String armsTextureName) {
        super(armorValue, enchantCost, rarity, bodyTextureName, armsTextureName);
    }

    @Override
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("global", "aphorea"));
        return tooltips;
    }
}
