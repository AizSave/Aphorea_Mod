package aphorea.items.trinkets.ability_no;

import aphorea.other.vanillaitemtypes.AphoreaTrinketItem;
import necesse.engine.localization.Localization;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.trinketBuffs.TrinketBuff;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class HeartRing extends AphoreaTrinketItem {
    public HeartRing() {
        super(Rarity.COMMON, 300);
        healingEnchantments = true;
    }

    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "heartring"));
        return tooltips;
    }

    public TrinketBuff[] getBuffs(InventoryItem item) {
        return new TrinketBuff[]{(TrinketBuff) BuffRegistry.getBuff("heartringbuff")};
    }
}
