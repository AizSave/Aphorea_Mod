package aphorea.items.weapons.melee;

import aphorea.other.magichealing.AphoreaMagicHealing;
import aphorea.other.vanillaitemtypes.weapons.AphoreaGlaiveToolItem;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.levelEvent.toolItemEvent.ToolItemEvent;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;
import necesse.level.maps.Level;

public class WoodenRod extends AphoreaGlaiveToolItem {

    int attackCount = 0;

    public WoodenRod() {
        super(300);
        rarity = Item.Rarity.COMMON;
        attackAnimTime.setBaseValue(500);
        attackDamage.setBaseValue(5)
                .setUpgradedValue(1, 30);
        attackRange.setBaseValue(223);
        knockback.setBaseValue(40);

        attackXOffset = 86;
        attackYOffset = 86;
        width = 14.0F;
    }

    @Override
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "woodenrod", "healing", AphoreaMagicHealing.getMagicHealing(perspective, perspective, 3)));
        return tooltips;
    }

    @Override
    public void hitMob(InventoryItem item, ToolItemEvent event, Level level, Mob target, Mob attacker) {
        super.hitMob(item, event, level, target, attacker);

        attackCount++;
        if(attackCount >= 10) {
            attackCount = 0;
            AphoreaMagicHealing.healMob(attacker, attacker, 3);
        }
    }
}
