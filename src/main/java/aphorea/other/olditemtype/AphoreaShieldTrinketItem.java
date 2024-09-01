package aphorea.other.olditemtype;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.trinketItem.ShieldTrinketItem;

abstract public class AphoreaShieldTrinketItem extends ShieldTrinketItem {
    public AphoreaShieldTrinketItem(Rarity rarity, int armorValue, float minSlowModifier, int msToDepleteStamina, float staminaUsageOnBlock, int damageTakenPercent, float angleCoverage, int enchantCost) {
        super(rarity, armorValue, minSlowModifier, msToDepleteStamina, staminaUsageOnBlock, damageTakenPercent, angleCoverage, enchantCost);
    }

    @Override
    public ListGameTooltips getPostEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPostEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("global", "aphorea"));
        return tooltips;
    }
}
