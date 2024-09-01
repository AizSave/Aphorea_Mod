package aphorea.items.weapons.healing;

import aphorea.other.AphoreaArea;
import aphorea.other.AphoreaAreaList;
import aphorea.other.itemtype.weapons.AphoreaMagicAreaToolItem;
import necesse.gfx.gameTooltips.GameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.enchants.ToolItemModifiers;
import necesse.inventory.item.ItemInteractAction;
import necesse.inventory.item.upgradeUtils.IntUpgradeValue;

import java.awt.*;

public class HealingStaff extends AphoreaMagicAreaToolItem implements ItemInteractAction {

    static IntUpgradeValue healingArea0 = new IntUpgradeValue().setBaseValue(6).setUpgradedValue(1.0F, 7);
    static IntUpgradeValue healingArea1 = new IntUpgradeValue().setBaseValue(2).setUpgradedValue(1.0F, 3);

    public HealingStaff() {
        super(500, false, true,
            new AphoreaAreaList(
                new AphoreaArea(100, new Color(191, 0, 255)).setHealingArea(healingArea0),
                new AphoreaArea(100, new Color(255, 0, 191)).setHealingArea(healingArea1)
            )
        );
        rarity = Rarity.UNCOMMON;
        attackAnimTime.setBaseValue(1400);

        manaCost.setBaseValue(6.0F);

        attackXOffset = 12;
        attackYOffset = 22;

        attackDamage.setBaseValue(1)
                .setUpgradedValue(1, 2);
    }
}
