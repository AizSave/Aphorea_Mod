package aphorea.items.weapons.melee;

import aphorea.other.itemtype.weapons.AphoreaSaberToolItem;
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

public class UnstableGelSaber extends AphoreaSaberToolItem {

    public UnstableGelSaber() {
        super(500, getChargeLevels());
        rarity = Rarity.UNCOMMON;
        attackDamage.setBaseValue(16)
                .setUpgradedValue(1, 85);
        attackRange.setBaseValue(80);
        attackRange.setBaseValue(80);
        knockback.setBaseValue(100);
    }

    @Override
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "stikybuff1"));
        return tooltips;
    }

    @Override
    public void hitMob(InventoryItem item, ToolItemEvent event, Level level, Mob target, Mob attacker) {
        super.hitMob(item, event, level, target, attacker);
        ActiveBuff buff = new ActiveBuff(BuffRegistry.getBuff("stickybuff"), target, 1000, event.mob);
        target.addBuff(buff, true);
    }
}
