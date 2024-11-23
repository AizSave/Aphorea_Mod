package aphorea.items.healingtools;

import aphorea.other.itemtype.healing.AphoreaHealingProjectileToolItem;
import aphorea.projectiles.base.AphoreaHealingToolItemProjectile;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.projectile.Projectile;
import necesse.inventory.InventoryItem;
import necesse.level.maps.Level;

import java.awt.*;

public class WoodenWand extends AphoreaHealingProjectileToolItem {

    public WoodenWand() {
        super(200);
        rarity = Rarity.UNCOMMON;
        attackAnimTime.setBaseValue(800);

        this.attackRange.setBaseValue(500);
        this.velocity.setBaseValue(200);
        manaCost.setBaseValue(5.0F);

        attackXOffset += 10;
        attackYOffset += 15;

        magicHealing.setBaseValue(2).setUpgradedValue(1, 3);
    }

    public Projectile[] getProjectiles(Level level, int x, int y, PlayerMob player, InventoryItem item) {
        return new Projectile[] {
                new AphoreaHealingToolItemProjectile(new Color(0, 214, 0), this.getHealing(item), this, item, level, player,
                        player.x, player.y,
                        x, y,
                        getProjectileVelocity(item, player),
                        getAttackRange(item),
                        0.05F
                )
        };
    }
}
