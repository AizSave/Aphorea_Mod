package aphorea.other.area;

import aphorea.other.magichealing.AphoreaMagicHealing;
import aphorea.other.itemtype.healing.AphoreaMagicHealingToolItem;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.entity.levelEvent.toolItemEvent.ToolItemEvent;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.gameDamageType.DamageType;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolItem;
import necesse.inventory.item.upgradeUtils.FloatUpgradeValue;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class AphoreaAreaList {
    public AphoreaArea[] areas;

    public AphoreaAreaList(AphoreaArea... areas) {
        this.areas = areas;

        for (int i = 0; i < areas.length; i++) {
            areas[i].position = i;
            areas[i].antRange = i == 0 ? 0 : areas[i - 1].range;
            areas[i].currentRange = areas[i].range;
            areas[i].range = areas[i].range + areas[i].antRange;
        }
    }

    public void showAllAreaParticles(Mob mob, float x, float y, float rangeModifier, float borderParticleModifier, float innerParticleModifier, int particleTime) {
        Arrays.stream(areas).forEach((AphoreaArea area) -> area.showAreaParticles(mob, x, y, this, null, rangeModifier, borderParticleModifier, innerParticleModifier, particleTime));
    }

    public void showAllAreaParticles(Mob mob, float x, float y, float rangeModifier, float particleModifier, int particleTime) {
        showAllAreaParticles(mob, x, y, rangeModifier, particleModifier, particleModifier, particleTime);
    }

    public void showAllAreaParticles(Mob mob, float x, float y, float rangeModifier, int particleTime) {
        showAllAreaParticles(mob, x, y, rangeModifier, 1, 0.2F, particleTime);
    }

    public void showAllAreaParticles(Mob mob, float x, float y, float rangeModifier) {
        showAllAreaParticles(mob, x, y, rangeModifier, 1, 0.2F, (int) (Math.random() * 200) + 900);
    }

    public void showAllAreaParticles(Mob mob, float x, float y) {
        showAllAreaParticles(mob, x, y, 1);
    }

    public void showAllAreaParticles(Mob mob, float rangeModifier) {
        showAllAreaParticles(mob, mob.x, mob.y, rangeModifier);
    }

    public void showAllAreaParticles(Mob mob) {
        showAllAreaParticles(mob, mob.x, mob.y, 1);
    }

    public void executeAreas(ToolItem toolItem, @Nullable AphoreaMagicHealingToolItem magicHealingToolItem, FloatUpgradeValue attackDamage, int x, int y, PlayerMob attacker, InventoryItem item, int seed, float rangeModifier) {
        executeAreas(toolItem, magicHealingToolItem, attackDamage, x, y, attacker, item, seed, rangeModifier, true);
    }

    public void executeAreas(ToolItem toolItem, @Nullable AphoreaMagicHealingToolItem magicHealingToolItem, FloatUpgradeValue attackDamage, int x, int y, PlayerMob attacker, InventoryItem item, int seed, float rangeModifier, boolean centerIsAttacker) {
        if(attacker.isServer()) {

            ToolItemEvent event = new ToolItemEvent(attacker, seed, item, x, y, toolItem.getAttackAnimTime(item, attacker), 1000);

            int range = Math.round(this.areas[this.areas.length - 1].range * rangeModifier);
            float centerX = centerIsAttacker ? attacker.x : x;
            float centerY = centerIsAttacker ? attacker.y : y;

            attacker.getLevel().entityManager.streamAreaMobsAndPlayers(centerX, centerY, range).forEach(
                    (Mob target) -> {
                        for (AphoreaArea area : this.areas) {
                            area.execute(attacker, target, rangeModifier, toolItem, magicHealingToolItem, attackDamage, item, event, x, y, centerIsAttacker);
                        }
                    }
            );

        }
    }

    public void executeAreas(Mob attacker) {
        executeAreas(attacker, 1);
    }

    public void executeAreas(Mob attacker, float rangeModifier) {
        executeAreas(attacker, rangeModifier, 0, 0, true);
    }

    public void executeAreas(Mob attacker, float rangeModifier, int x, int y, boolean centerIsAttacker) {
        if(attacker.isServer()) {

            int range = Math.round(this.areas[this.areas.length - 1].range * rangeModifier);
            float centerX = centerIsAttacker ? attacker.x : x;
            float centerY = centerIsAttacker ? attacker.y : y;

            attacker.getLevel().entityManager.streamAreaMobsAndPlayers(centerX, centerY, range).forEach(
                    (Mob target) -> {
                        for (AphoreaArea area : this.areas) {
                            area.execute((PlayerMob) attacker, target, rangeModifier, x, y, centerIsAttacker);
                        }
                    }
            );

        }
    }

    public boolean someType(AphoreaAreaType type) {
        return Arrays.stream(areas).anyMatch(a -> a.areaTypes.contains(type));
    }

    public AphoreaAreaList setDamageType(DamageType damageType) {
        Arrays.stream(this.areas).forEach(area -> area.damageType = damageType);
        return this;
    }
}
