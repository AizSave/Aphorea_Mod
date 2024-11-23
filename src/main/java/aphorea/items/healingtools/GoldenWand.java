package aphorea.items.healingtools;

import aphorea.other.itemtype.healing.AphoreaHealingProjectileToolItem;
import aphorea.projectiles.base.AphoreaHealingAreaToolItemProjectile;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.projectile.Projectile;
import necesse.inventory.InventoryItem;
import necesse.level.maps.Level;

import java.awt.*;

public class GoldenWand extends AphoreaHealingProjectileToolItem {

    public GoldenWand() {
        super(200);
        rarity = Rarity.UNCOMMON;
        attackAnimTime.setBaseValue(800);

        this.attackRange.setBaseValue(500);
        this.velocity.setBaseValue(200);
        manaCost.setBaseValue(5.0F);

        attackXOffset += 10;
        attackYOffset += 15;

        magicHealing.setBaseValue(3).setUpgradedValue(1, 4);
    }

    public Projectile[] getProjectiles(Level level, int x, int y, PlayerMob player, InventoryItem item) {
        return new Projectile[] {
                new AphoreaHealingAreaToolItemProjectile(new Color(0, 214, 0), this.getHealing(item), this, item, level, player,
                        player.x, player.y,
                        x, y,
                        getProjectileVelocity(item, player),
                        getAttackRange(item),
                        0.05F, 100
                )
        };
    }
}
