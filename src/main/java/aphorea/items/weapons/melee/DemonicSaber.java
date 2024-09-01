package aphorea.items.weapons.melee;

import aphorea.other.itemtype.weapons.AphoreaSaberToolItem;

public class DemonicSaber extends AphoreaSaberToolItem {

    public DemonicSaber() {
        super(500, getChargeLevels());
        rarity = Rarity.UNCOMMON;
        attackDamage.setBaseValue(20)
                .setUpgradedValue(1, 120);
        knockback.setBaseValue(150);
    }

}
