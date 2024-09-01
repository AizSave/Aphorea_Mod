package aphorea.items.weapons.melee;

import aphorea.other.itemtype.weapons.AphoreaBattleaxeToolItem;
import necesse.engine.localization.Localization;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.entity.levelEvent.toolItemEvent.ToolItemEvent;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.level.maps.Level;

import java.awt.*;

public class UnstableGelBattleaxe extends AphoreaBattleaxeToolItem {

    public UnstableGelBattleaxe() {
        super(500, getChargeLevel(2000, new Color(191, 102, 255)), getChargeLevel(1400, new Color(191, 102, 255)));
        rarity = Rarity.UNCOMMON;
        attackDamage.setBaseValue(120)
                .setUpgradedValue(1, 300);
        attackRange.setBaseValue(90);
        knockback.setBaseValue(150);
    }

    @Override
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "stikybuff3"));
        return tooltips;
    }

    @Override
    public void hitMob(InventoryItem item, ToolItemEvent event, Level level, Mob target, Mob attacker) {
        super.hitMob(item, event, level, target, attacker);
        ActiveBuff buff = new ActiveBuff(BuffRegistry.getBuff("stickybuff"), target, 3000, event.mob);
        target.addBuff(buff, true);
    }
}
