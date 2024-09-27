package aphorea.other.area;

import aphorea.other.magichealing.AphoreaMagicHealing;
import aphorea.other.magichealing.AphoreaMagicHealingToolItem;
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
            areas[i].antRange = i - 1 < 0 ? 0 : areas[i - 1].range;
            areas[i].currentRange = areas[i].range;
            areas[i].range = areas[i].range + areas[i].antRange;
        }
    }

    public void showAllAreaParticles(Mob mob, float rangeModifier) {
        Arrays.stream(areas).forEach((AphoreaArea area) -> area.showAreaParticles(mob, this, null, rangeModifier));
    }

    public void executeAreas(ToolItem toolItem, @Nullable AphoreaMagicHealingToolItem magicHealingToolItem, FloatUpgradeValue attackDamage, int x, int y, PlayerMob player, InventoryItem item, int seed, float rangeModifier) {
        ToolItemEvent event = new ToolItemEvent(player, seed, item, x, y, toolItem.getAttackAnimTime(item, player), 1000);

        if(this.areas[0].areaTypes.contains(AphoreaAreaType.HEALING)) {
            AphoreaArea area = this.areas[0];
            if(magicHealingToolItem == null) {
                AphoreaMagicHealing.healMob(player, player, area.getHealing(item));
            } else {
                magicHealingToolItem.healMob(player, player, area.getHealing(item), item);
            }
        }

        int range = Math.round(this.areas[this.areas.length - 1].range * rangeModifier);

        player.getLevel().entityManager.streamAreaMobsAndPlayers(player.x, player.y, range).forEach(
                (Mob target) -> {
                    for (AphoreaArea area : this.areas) {
                        int antRange = area.antRange * range;
                        if (target.getDistance(player) <= range && target.getDistance(player) > antRange) {
                            if (area.areaTypes.contains(AphoreaAreaType.DAMAGE) && target.isHostile) {
                                attackDamage.setBaseValue(area.getBaseDamage()).setUpgradedValue(1.0F, area.getTier1Damage());
                                toolItem.hitMob(player.attackingItem, event, player.getLevel(), target, player);
                            }
                            if (area.areaTypes.contains(AphoreaAreaType.HEALING) && ((target.isSameTeam(player) && target.isPlayer) || target.isHuman) && !target.isHostile) {
                                if (magicHealingToolItem == null) {
                                    AphoreaMagicHealing.healMob(player, target, area.getHealing(item));
                                } else {
                                    magicHealingToolItem.healMob(player, target, area.getHealing(item), item);
                                }
                            }
                        }
                    }
                }
        );
    }

    public void executeAreas(Mob healer, float rangeModifier) {
        if(this.areas[0].areaTypes.contains(AphoreaAreaType.HEALING)) {
            AphoreaArea area = this.areas[0];
            AphoreaMagicHealing.healMob(healer, healer, area.getHealing(null));

        }

        int range = Math.round(this.areas[this.areas.length - 1].range * rangeModifier);

        healer.getLevel().entityManager.streamAreaMobsAndPlayers(healer.x, healer.y, range).forEach(
                (Mob target) -> {
                    for (AphoreaArea area : this.areas) {
                        int antRange = area.antRange * range;
                        if (target.getDistance(healer) <= range && target.getDistance(healer) > antRange) {
                            if (area.areaTypes.contains(AphoreaAreaType.DAMAGE) && target.isHostile) {
                                target.isServerHit(new GameDamage(DamageTypeRegistry.NORMAL, area.getBaseDamage(), 100000, 0), target.x - healer.x, target.y - healer.y, 0, healer);
                            }
                            if (area.areaTypes.contains(AphoreaAreaType.HEALING) && ((target.isSameTeam(healer) && target.isPlayer) || target.isHuman) && !target.isHostile) {
                                AphoreaMagicHealing.healMob(healer, target, area.getHealing(null));
                            }
                        }
                    }
                }
        );
    }
}
