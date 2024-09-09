package aphorea.items.trinkets.ability_yes;

import aphorea.other.vanillaitemtypes.AphoreaTrinketItem;
import necesse.engine.localization.Localization;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.trinketBuffs.TrinketBuff;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class DemonicPeriapt extends AphoreaTrinketItem {
    public DemonicPeriapt() {
        super(Rarity.RARE, 400);
    }

    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "demonicperiapt"));
        tooltips.add(Localization.translate("itemtooltip", "demonicperiapt2"));
        return tooltips;
    }

    public TrinketBuff[] getBuffs(InventoryItem item) {
        return new TrinketBuff[]{(TrinketBuff)BuffRegistry.getBuff("demonicperiaptbuff")};
    }
}
