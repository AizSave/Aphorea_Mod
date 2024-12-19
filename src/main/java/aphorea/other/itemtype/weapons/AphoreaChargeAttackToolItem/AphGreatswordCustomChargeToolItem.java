package aphorea.other.itemtype.weapons.AphoreaChargeAttackToolItem;

import aphorea.other.vanillaitemtypes.weapons.AphGreatswordToolItem;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.attackHandler.GreatswordChargeLevel;

import java.awt.geom.Point2D;

public class AphGreatswordCustomChargeToolItem extends AphGreatswordToolItem {
    public boolean isCharging;

    public AphGreatswordCustomChargeToolItem(int enchantCost, GreatswordChargeLevel... chargeLevels) {
        super(enchantCost, chargeLevels);
        isCharging = false;
    }

    public void endChargeAttack(PlayerMob player, Point2D.Float dir, int charge) {
    }
}
