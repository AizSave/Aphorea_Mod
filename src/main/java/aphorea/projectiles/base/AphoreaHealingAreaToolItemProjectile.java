package aphorea.projectiles.base;

import aphorea.other.area.AphoreaArea;
import aphorea.other.area.AphoreaAreaList;
import aphorea.other.itemtype.healing.AphoreaHealingProjectileToolItem;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.InventoryItem;
import necesse.level.maps.Level;
import necesse.level.maps.LevelObjectHit;

import java.awt.*;

public class AphoreaHealingAreaToolItemProjectile extends AphoreaHealingToolItemProjectile {
    AphoreaAreaList areaList;

    public AphoreaHealingAreaToolItemProjectile() {
    }

    public AphoreaHealingAreaToolItemProjectile(Color color, int healing, AphoreaHealingProjectileToolItem toolItem, InventoryItem item, Level level, Mob owner, float x, float y, float targetX, float targetY, float speed, int distance, float turnSpeed, int areaRange) {
        super(color, healing, toolItem, item, level, owner, x, y, targetX, targetY, speed, distance, turnSpeed);
        areaList = new AphoreaAreaList(
                new AphoreaArea(areaRange, color).setHealingArea(healing, healing)
        );
    }

    @Override
    public void doHitLogic(Mob mob, LevelObjectHit object, float x, float y) {
        executeArea();
    }

    public void executeArea() {
        if (this.getOwner() != null) {
            if (toolItem != null) {
                areaList.executeAreas(toolItem, toolItem, null, (int) x, (int) y, (PlayerMob) this.getOwner(), item, GameRandom.getNewUniqueID(), 1F, false);
            } else {
                areaList.executeAreas(this.getOwner(), 1, (int) x, (int) y, false);
            }
            areaList.showAllAreaParticles(this.getOwner(), x, y);
        }
    }

}
