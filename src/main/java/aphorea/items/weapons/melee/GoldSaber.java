package aphorea.items.weapons.melee;

import aphorea.other.itemtype.weapons.AphoreaSaberToolItem;

public class GoldSaber extends AphoreaSaberToolItem {

    public GoldSaber() {
        super(500, getChargeLevels());
        rarity = Rarity.COMMON;
        attackDamage.setBaseValue(16)
                .setUpgradedValue(1, 85);
        knockback.setBaseValue(150);
    }

}
