package aphorea.other.area;

import aphorea.other.magichealing.AphoreaMagicHealing;
import aphorea.other.itemtype.healing.AphoreaMagicHealingToolItem;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.entity.levelEvent.toolItemEvent.ToolItemEvent;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
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

    public void showAllAreaParticles(Mob mob, float rangeModifier, float borderParticleModifier, float innerParticleModifier, int particleTime) {
        Arrays.stream(areas).forEach((AphoreaArea area) -> area.showAreaParticles(mob, this, null, rangeModifier, borderParticleModifier, innerParticleModifier, particleTime));
    }

    public void showAllAreaParticles(Mob mob, float rangeModifier, float particleModifier, int particleTime) {
        showAllAreaParticles(mob, rangeModifier, particleModifier, particleModifier, particleTime);
    }

    public void showAllAreaParticles(Mob mob, float rangeModifier, int particleTime) {
        showAllAreaParticles(mob, rangeModifier, 1, 0.2F, particleTime);
    }

    public void showAllAreaParticles(Mob mob, float rangeModifier) {
        showAllAreaParticles(mob, rangeModifier, 1, 0.2F, (int) (Math.random() * 200) + 900);
    }

    public void showAllAreaParticles(Mob mob) {
        showAllAreaParticles(mob, 1);
    }

    public void executeAreas(ToolItem toolItem, @Nullable AphoreaMagicHealingToolItem magicHealingToolItem, FloatUpgradeValue attackDamage, int x, int y, PlayerMob attacker, InventoryItem item, int seed, float rangeModifier) {
        if(attacker.isServer()) {
            ToolItemEvent event = new ToolItemEvent(attacker, seed, item, x, y, toolItem.getAttackAnimTime(item, attacker), 1000);

            if (this.areas[0].areaTypes.contains(AphoreaAreaType.HEALING)) {
                AphoreaArea area = this.areas[0];
                if (magicHealingToolItem == null) {
                    AphoreaMagicHealing.healMob(attacker, attacker, area.getHealing(item));
                } else {
                    magicHealingToolItem.healMob(attacker, attacker, area.getHealing(item), item);
                }
            }

            int range = Math.round(this.areas[this.areas.length - 1].range * rangeModifier);

            attacker.getLevel().entityManager.streamAreaMobsAndPlayers(attacker.x, attacker.y, range).forEach(
                    (Mob target) -> {
                        for (AphoreaArea area : this.areas) {
                            area.execute(attacker, target, rangeModifier, toolItem, magicHealingToolItem, attackDamage, item, event);
                        }
                    }
            );
        }
    }

    public void executeAreas(Mob attacker) {
        executeAreas(attacker, 1);
    }

    public void executeAreas(Mob attacker, float rangeModifier) {
        if(attacker.isServer()) {

            if (this.areas[0].areaTypes.contains(AphoreaAreaType.HEALING)) {
                AphoreaArea area = this.areas[0];
                AphoreaMagicHealing.healMob(attacker, attacker, area.getHealing(null));
            }

            int range = Math.round(this.areas[this.areas.length - 1].range * rangeModifier);

            attacker.getLevel().entityManager.streamAreaMobsAndPlayers(attacker.x, attacker.y, range).forEach(
                    (Mob target) -> {
                        for (AphoreaArea area : this.areas) {
                            area.execute((PlayerMob) attacker, target, rangeModifier);
                        }
                    }
            );
        }
    }

    public boolean someType(AphoreaAreaType type) {
        return Arrays.stream(areas).anyMatch(a -> a.areaTypes.contains(type));
    }
}
