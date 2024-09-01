package aphorea.items.trinkets.ability_no;

import aphorea.other.olditemtype.AphoreaTrinketItem;

import necesse.engine.localization.Localization;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.trinketBuffs.TrinketBuff;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class UnstablePeriapt extends AphoreaTrinketItem {
    public UnstablePeriapt() {
        super(Rarity.UNCOMMON, 400);
    }

    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "unstableperiapt"));
        tooltips.add(Localization.translate("itemtooltip", "unstableperiapt2"));
        return tooltips;
    }

    public TrinketBuff[] getBuffs(InventoryItem item) {
        return new TrinketBuff[]{(TrinketBuff)BuffRegistry.getBuff("unstableperiaptbuff")};
    }
}
