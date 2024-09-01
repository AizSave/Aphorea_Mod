package aphorea.other.itemtype.weapons.AphoreaChargeAttackToolItem;

import aphorea.other.olditemtype.weapons.AphoreaGreatswordToolItem;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.attackHandler.GreatswordChargeLevel;

import java.awt.geom.Point2D;

public class AphoreaGreatswordCustomChargeToolItem extends AphoreaGreatswordToolItem {
    public boolean isCharging;

    public AphoreaGreatswordCustomChargeToolItem(int enchantCost, GreatswordChargeLevel... chargeLevels) {
        super(enchantCost, chargeLevels);
        isCharging = false;
    }

    public void endChargeAttack(PlayerMob player, Point2D.Float dir, int charge) {
    }
}
