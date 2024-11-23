package aphorea.projectiles.base;

import aphorea.other.area.AphoreaArea;
import aphorea.other.area.AphoreaAreaList;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolItem;
import necesse.inventory.item.upgradeUtils.FloatUpgradeValue;
import necesse.level.maps.Level;
import necesse.level.maps.LevelObjectHit;

import java.awt.*;

public class AphoreaDamageAreaToolItemProjectile extends AphoreaDamageToolItemProjectile {
    public AphoreaAreaList areaList;
    FloatUpgradeValue attackDamage;

    public AphoreaDamageAreaToolItemProjectile() {
    }

    public AphoreaDamageAreaToolItemProjectile(Color color, FloatUpgradeValue attackDamage, float areaDamage, ToolItem toolItem, InventoryItem item, Level level, Mob owner, float x, float y, float targetX, float targetY, float speed, int distance, float turnSpeed, int areaRange) {
        super(color, toolItem, item, level, owner, x, y, targetX, targetY, speed, distance, turnSpeed);
        this.attackDamage = attackDamage;
        areaList = new AphoreaAreaList(
                new AphoreaArea(areaRange, color).setDamageArea(areaDamage)
        );
    }

    @Override
    public void doHitLogic(Mob mob, LevelObjectHit object, float x, float y) {
        super.doHitLogic(mob, object, x, y);
        executeArea();
    }

    public void executeArea() {
        if (this.getOwner() != null) {
            if (toolItem != null) {
                areaList.executeAreas(toolItem, null, attackDamage, (int) x, (int) y, (PlayerMob) this.getOwner(), item, GameRandom.getNewUniqueID(), 1F, false);
            } else {
                areaList.executeAreas(this.getOwner(), 1, (int) x, (int) y, false);
            }
            areaList.showAllAreaParticles(this.getOwner(), x, y);
        }
    }

}
