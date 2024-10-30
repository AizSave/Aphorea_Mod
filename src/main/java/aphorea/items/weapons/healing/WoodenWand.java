package aphorea.items.weapons.healing;

import aphorea.other.itemtype.healing.AphoreaHealingProjectileToolItem;
import aphorea.projectiles.HealingToolItemProjectile;
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

        magicHealing.setBaseValue(2).setUpgradedValue(1, 4);
    }

    public Projectile getProjectile(Level level, int x, int y, PlayerMob player, InventoryItem item) {
        return new HealingToolItemProjectile(new Color(0, 214, 0), this, item, level, player,
                player.x, player.y,
                x, y,
                getProjectileVelocity(item, player),
                getAttackRange(item),
                0.05F
        );
    }
}
