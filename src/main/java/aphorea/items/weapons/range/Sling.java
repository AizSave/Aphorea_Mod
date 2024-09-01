package aphorea.items.weapons.range;

import aphorea.other.itemtype.weapons.AphoreaSlingToolItem;
import aphorea.projectiles.StoneProjectile;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.projectile.Projectile;
import necesse.inventory.InventoryItem;
import necesse.level.maps.Level;

public class Sling extends AphoreaSlingToolItem {
    public Sling() {
        super(100, "stoneprojectile");
        this.rarity = Rarity.NORMAL;
        this.attackAnimTime.setBaseValue(1200);
        this.attackDamage.setBaseValue(25.0F).setUpgradedValue(1.0F, 130.0F);
        this.attackRange.setBaseValue(500);
        this.velocity.setBaseValue(100);
        this.attackXOffset = 8;
        this.attackYOffset = 20;
    }

    public Projectile getProjectile(Level level, int x, int y, PlayerMob player, InventoryItem item) {
        return new StoneProjectile(level, player,
                player.x, player.y,
                x, y,
                getProjectileVelocity(item, player),
                getAttackRange(item),
                getAttackDamage(item),
                getKnockback(item, player)
        );
    }
}
