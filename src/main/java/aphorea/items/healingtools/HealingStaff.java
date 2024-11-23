package aphorea.items.healingtools;

import aphorea.other.area.AphoreaArea;
import aphorea.other.area.AphoreaAreaList;
import aphorea.other.itemtype.weapons.AphoreaMagicAreaToolItem;
import necesse.inventory.item.ItemInteractAction;

import java.awt.*;

public class HealingStaff extends AphoreaMagicAreaToolItem implements ItemInteractAction {

    public HealingStaff() {
        super(500, false, true,
            new AphoreaAreaList(
                new AphoreaArea(100, new Color(191, 0, 255)).setHealingArea(6, 7),
                new AphoreaArea(100, new Color(255, 0, 191)).setHealingArea(2, 3)
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
