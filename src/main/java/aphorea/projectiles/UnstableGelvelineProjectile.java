package aphorea.projectiles;

import aphorea.projectiles.base.AphoreaDamageAreaToolItemProjectile;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolItem;
import necesse.inventory.item.upgradeUtils.FloatUpgradeValue;
import necesse.level.maps.Level;

import java.awt.*;
import java.util.Arrays;

public class UnstableGelvelineProjectile extends AphoreaDamageAreaToolItemProjectile {

    public UnstableGelvelineProjectile() {
    }

    public UnstableGelvelineProjectile(FloatUpgradeValue attackDamage, ToolItem toolItem, InventoryItem item, Level level, Mob owner, float x, float y, float targetX, float targetY, float speed, int distance, float turnSpeed, int areaRange) {
        super(new Color(191, 102, 255), attackDamage, (float) toolItem.getAttackDamageValue(item, owner) * 0.5F, toolItem, item, level, owner, x, y, targetX, targetY, speed, distance, turnSpeed, areaRange);
        this.areaList.setDamageType(DamageTypeRegistry.MELEE);
        this.givesLight = false;
        this.setWidth(0, 20);


    }

    @Override
    public void addHit(Mob target) {
        super.addHit(target);
        ActiveBuff buff = new ActiveBuff(BuffRegistry.getBuff("stickybuff"), target, 2000, this);
        target.addBuff(buff, true);
    }
}
