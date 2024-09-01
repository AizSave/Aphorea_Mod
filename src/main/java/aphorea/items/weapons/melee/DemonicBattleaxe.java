package aphorea.items.weapons.melee;

import aphorea.other.itemtype.weapons.AphoreaBattleaxeToolItem;

import java.awt.*;

public class DemonicBattleaxe extends AphoreaBattleaxeToolItem {

    public DemonicBattleaxe() {
        super(500, getChargeLevel(2000, new Color(53, 46, 66)), getChargeLevel(1400, new Color(53, 46, 66)));
        rarity = Rarity.UNCOMMON;
        attackDamage.setBaseValue(140)
                .setUpgradedValue(1, 400);
        attackRange.setBaseValue(100);
        knockback.setBaseValue(150);
    }
}
