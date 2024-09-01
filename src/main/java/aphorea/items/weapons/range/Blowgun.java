package aphorea.items.weapons.range;

import aphorea.other.itemtype.weapons.AphoreaBlowgunToolItem;
import aphorea.projectiles.SeedProjectile;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.projectile.Projectile;
import necesse.inventory.InventoryItem;
import necesse.level.maps.Level;

public class Blowgun extends AphoreaBlowgunToolItem {
    public Blowgun() {
        super(100, "seedprojectile", 200);
        this.rarity = Rarity.NORMAL;
        this.attackDamage.setBaseValue(6.0F).setUpgradedValue(1.0F, 22.0F);
        this.attackRange.setBaseValue(500);
        this.velocity.setBaseValue(100);
        this.attackXOffset = 8;
        this.attackYOffset = 10;
    }

    public Projectile getProjectile(Level level, float x, float y, PlayerMob player, InventoryItem item) {
        return new SeedProjectile(level, player,
                player.x, player.y - 14,
                x, y,
                getProjectileVelocity(item, player),
                getAttackRange(item),
                getAttackDamage(item),
                getKnockback(item, player)
        );
    }

}
