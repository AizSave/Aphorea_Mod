package aphorea.testing;

import aphorea.other.itemtype.healing.AphoreaHealingProjectileToolItem;
import aphorea.projectiles.base.AphoreaHealingToolItemProjectile;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.projectile.Projectile;
import necesse.inventory.InventoryItem;
import necesse.level.maps.Level;

import java.awt.*;

public class TestItem3 extends AphoreaHealingProjectileToolItem {

    public TestItem3() {
        super(200);
        rarity = Rarity.UNCOMMON;
        attackAnimTime.setBaseValue(800);

        this.attackRange.setBaseValue(500);
        this.velocity.setBaseValue(200);
        manaCost.setBaseValue(8.0F);

        attackXOffset += 10;
        attackYOffset += 15;

        magicHealing.setBaseValue(2).setUpgradedValue(1, 4);
    }

    public Projectile[] getProjectiles(Level level, int x, int y, PlayerMob player, InventoryItem item) {
        Projectile[] projectiles = new Projectile[2];

        float[] vector = new float[]{x - player.x, y - player.y};
        float[] perpendicularVector = new float[]{vector[1], -vector[0]};

        for (int i = 0; i < 2; i++) {
            float endX;
            float endY;

            if(i == 1) {
                endX = x + perpendicularVector[0] / 6;
                endY = y + perpendicularVector[1] / 6;
            } else {
                endX = x - perpendicularVector[0] / 6;
                endY = y - perpendicularVector[1] / 6;
            }

            projectiles[i] = new AphoreaHealingToolItemProjectile(new Color(0, 214, 0), this.getHealing(item), this, item, level, player,
                    player.x, player.y,
                    endX, endY,
                    getProjectileVelocity(item, player),
                    getAttackRange(item),
                    0.1F
            );
        }

        return projectiles;
    }

}
