package aphorea.projectiles.base;

import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.projectile.followingProjectile.FollowingProjectile;
import necesse.entity.trails.Trail;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawables.EntityDrawable;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolItem;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.List;

public class AphoreaDamageToolItemProjectile extends AphoreaDamageProjectile {
    ToolItem toolItem;
    InventoryItem item;

    public AphoreaDamageToolItemProjectile() {
    }

    public AphoreaDamageToolItemProjectile(Color color, ToolItem toolItem, InventoryItem item, Level level, Mob owner, float x, float y, float targetX, float targetY, float speed, int distance, float turnSpeed) {
        super(color, toolItem.getAttackDamage(item), level, owner, x, y, targetX, targetY, speed, distance, turnSpeed);

        this.toolItem = toolItem;
        this.item = item;
    }
}
