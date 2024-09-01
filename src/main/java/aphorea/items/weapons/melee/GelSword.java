package aphorea.items.weapons.melee;

import aphorea.other.olditemtype.weapons.AphoreaSwordToolItem;
import necesse.engine.localization.Localization;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.entity.levelEvent.toolItemEvent.ToolItemEvent;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;
import necesse.level.maps.Level;

public class GelSword extends AphoreaSwordToolItem {

    public GelSword() {
        super(400);
        rarity = Item.Rarity.COMMON;
        attackAnimTime.setBaseValue(300);
        attackDamage.setBaseValue(18)
                .setUpgradedValue(1, 80);
        attackRange.setBaseValue(55);
        knockback.setBaseValue(5);
        attackXOffset += 10;
        attackYOffset += 15;
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
