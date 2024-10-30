package aphorea.other.area;

import aphorea.other.itemtype.healing.AphoreaMagicHealingToolItem;
import aphorea.other.magichealing.AphoreaMagicHealing;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.ParticleTypeSwitcher;
import necesse.entity.levelEvent.toolItemEvent.ToolItemEvent;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.particle.Particle;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolItem;
import necesse.inventory.item.upgradeUtils.FloatUpgradeValue;
import necesse.inventory.item.upgradeUtils.IntUpgradeValue;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class AphoreaArea {
    public int range;
    public int antRange;
    public int currentRange;
    public Color color;
    public int position;
    public Set<AphoreaAreaType> areaTypes = new HashSet<>();

    public FloatUpgradeValue damage;
    public IntUpgradeValue healing;

    public AphoreaArea(int range, Color color) {
        this.range = range;
        this.color = color;
    }

    public AphoreaArea setDamageArea(FloatUpgradeValue damage) {
        this.areaTypes.add(AphoreaAreaType.DAMAGE);
        this.damage = damage;

        return this;
    }

    public AphoreaArea setDamageArea(float damage) {
        return setDamageArea(new FloatUpgradeValue(0, 0.2F).setBaseValue(damage));
    }

    public AphoreaArea setDamageArea(float damage, float tier1Damage) {
        return setDamageArea(new FloatUpgradeValue(0, 0.2F).setBaseValue(damage).setUpgradedValue(1, tier1Damage));
    }

    public AphoreaArea setHealingArea(IntUpgradeValue healing) {
        this.areaTypes.add(AphoreaAreaType.HEALING);
        this.healing = healing;

        return this;
    }

    public AphoreaArea setHealingArea(int healing) {
        return setHealingArea(new IntUpgradeValue(0, 0.2F).setBaseValue(healing));
    }

    public AphoreaArea setHealingArea(int healing, int tier1Healing) {
        return setHealingArea(new IntUpgradeValue(0, 0.2F).setBaseValue(healing).setUpgradedValue(1, tier1Healing));
    }


    public void showAreaParticles(Mob mob, AphoreaAreaList areaList, Color forcedColor, float rangeModifier, float borderParticleModifier, float innerParticleModifier, int particleTime) {
        int range = Math.round(this.range * rangeModifier);
        int antRange = Math.round(this.antRange * rangeModifier);
        if (color != null || forcedColor != null) {
            float initialParticleCount = (float) (360 * range) / 400;
            float initialAnteriorParticleCount = antRange == 0 ? 0 : (float) (360 * antRange) / 400;

            int particles = Math.round(initialParticleCount * borderParticleModifier);
            int innerParticles = Math.round((initialParticleCount - initialAnteriorParticleCount) * innerParticleModifier);

            for (int i = 0; i < particles; i++) {
                float angle = (float) i / particles * 360;
                float dx = (float) Math.sin(Math.toRadians(angle)) * (float) range;
                float dy = (float) Math.cos(Math.toRadians(angle)) * (float) range;
                mob.getLevel().entityManager.addParticle(mob.x + dx, mob.y + dy, new ParticleTypeSwitcher(Particle.GType.CRITICAL, Particle.GType.IMPORTANT_COSMETIC, Particle.GType.COSMETIC).next()).movesFriction(GameRandom.globalRandom.getIntBetween(-5, 5), GameRandom.globalRandom.getIntBetween(-5, 5), GameRandom.globalRandom.getFloatBetween(0.05F, 0.1F)).color(forcedColor != null ? forcedColor : color).heightMoves(GameRandom.globalRandom.getFloatBetween(0F, 3F), GameRandom.globalRandom.getFloatBetween(5F, 10F)).lifeTime(particleTime);
            }

            if (0.1F * range + antRange < range * 0.9F) {
                for (int i = 0; i < innerParticles; i++) {
                    float angle = GameRandom.globalRandom.getIntBetween(0, 359);
                    float d = GameRandom.globalRandom.getFloatBetween(0.1F * range + antRange, 0.9F * range);
                    float dx = (float) Math.sin(Math.toRadians(angle)) * d;
                    float dy = (float) Math.cos(Math.toRadians(angle)) * d;
                    mob.getLevel().entityManager.addParticle(mob.x + dx, mob.y + dy, new ParticleTypeSwitcher(Particle.GType.CRITICAL, Particle.GType.IMPORTANT_COSMETIC, Particle.GType.COSMETIC).next()).movesFriction(GameRandom.globalRandom.getIntBetween(-5, 5), GameRandom.globalRandom.getIntBetween(-5, 5), GameRandom.globalRandom.getFloatBetween(0.05F, 0.1F)).color(forcedColor != null ? forcedColor : color).heightMoves(GameRandom.globalRandom.getFloatBetween(0F, 3F), GameRandom.globalRandom.getFloatBetween(5F, 10F)).lifeTime(particleTime);
                }
            }

            if (position > 0) {
                AphoreaArea antArea = areaList.areas[position - 1];

                antArea.showAreaParticles(mob, areaList, antArea.color == null ? forcedColor : color, rangeModifier, borderParticleModifier, innerParticleModifier, particleTime);
            }

        }
    }

    public float getBaseDamage() {
        return damage.getValue(0);
    }

    public float getTier1Damage() {
        return damage.getValue(1);
    }

    public float getDamage(@Nullable InventoryItem item) {
        return item == null ? damage.getValue(0) : damage.getValue(item.item.getUpgradeTier(item));
    }

    public int getHealing(@Nullable InventoryItem item) {
        return item == null ? healing.getValue(0) : healing.getValue(item.item.getUpgradeTier(item));
    }

    public void execute(PlayerMob attacker, Mob target, float modRange) {
        if (target.getDistance(attacker) <= (range * modRange) && target.getDistance(attacker) > (antRange * modRange)) {
            if (this.areaTypes.contains(AphoreaAreaType.DAMAGE) && target.canBeTargeted(attacker, attacker.getNetworkClient())) {
                target.isServerHit(new GameDamage(DamageTypeRegistry.NORMAL, this.getBaseDamage(), 100000, 0), target.x - attacker.x, target.y - attacker.y, 0, attacker);
            }
            if (this.areaTypes.contains(AphoreaAreaType.HEALING) && AphoreaMagicHealing.canHealMob(attacker, target)) {
                AphoreaMagicHealing.healMob(attacker, target, this.getHealing(null));
            }
        }

    }

    public void execute(PlayerMob attacker, Mob target, float modRange, ToolItem toolItem, @Nullable AphoreaMagicHealingToolItem magicHealingToolItem, FloatUpgradeValue attackDamage, InventoryItem item, ToolItemEvent event) {
        if (target.getDistance(attacker) <= (range * modRange) && target.getDistance(attacker) > (antRange * modRange)) {
            if (this.areaTypes.contains(AphoreaAreaType.DAMAGE) && target.canBeTargeted(attacker, attacker.getNetworkClient())) {
                attackDamage.setBaseValue(this.getBaseDamage()).setUpgradedValue(1.0F, this.getTier1Damage());
                toolItem.hitMob(attacker.attackingItem, event, attacker.getLevel(), target, attacker);
            }
            if (this.areaTypes.contains(AphoreaAreaType.HEALING) && AphoreaMagicHealing.canHealMob(attacker, target)) {
                if (magicHealingToolItem == null) {
                    AphoreaMagicHealing.healMob(attacker, target, this.getHealing(item), toolItem, item);
                } else {
                    magicHealingToolItem.healMob(attacker, target, this.getHealing(item), item);
                }
            }

        }

    }
}
