package aphorea.items.weapons.melee;

import aphorea.other.itemtype.weapons.AphoreaSaberToolItem;

public class IronSaber extends AphoreaSaberToolItem {

    public IronSaber() {
        super(500, getChargeLevels());
        rarity = Rarity.COMMON;
        attackDamage.setBaseValue(14)
                .setUpgradedValue(1, 80);
        knockback.setBaseValue(150);
    }

}
