package aphorea.items.weapons.melee;

import aphorea.other.itemtype.weapons.AphBattleaxeToolItem;

import java.awt.*;

public class DemonicBattleaxe extends AphBattleaxeToolItem {

    public DemonicBattleaxe() {
        super(500, getChargeLevel(2000, new Color(40, 10, 60)), getChargeLevel(1400, new Color(40, 10, 60)));
        rarity = Rarity.UNCOMMON;
        attackDamage.setBaseValue(140)
                .setUpgradedValue(1, 340);
        attackRange.setBaseValue(100);
        knockback.setBaseValue(150);
    }
}
