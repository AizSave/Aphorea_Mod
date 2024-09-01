package aphorea.items.weapons.melee;

import aphorea.other.itemtype.weapons.AphoreaSaberToolItem;

public class CopperSaber extends AphoreaSaberToolItem {

    public CopperSaber() {
        super(500, getChargeLevels());
        rarity = Rarity.COMMON;
        attackDamage.setBaseValue(12)
                .setUpgradedValue(1, 75);
        knockback.setBaseValue(150);
    }

}
