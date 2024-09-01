package aphorea.other;

import necesse.engine.util.GameRandom;
import necesse.entity.ParticleTypeSwitcher;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.particle.Particle;
import necesse.inventory.InventoryItem;
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

    public AphoreaArea setHealingArea(IntUpgradeValue healing) {
        this.areaTypes.add(AphoreaAreaType.HEALING);
        this.healing = healing;

        return this;
    }

    public void showAreaParticles(PlayerMob player, AphoreaAreaList areaList, Color forcedColor, float rangeModifier) {
        int range = Math.round(this.range * rangeModifier);
        int antRange = Math.round(this.antRange * rangeModifier);
        if(color != null || forcedColor != null) {
            int particles = Math.round((float) (360 * range) / 400);

            for (int i = 0; i < particles; i++) {
                float angle = (float) i / particles * 360;
                float dx = (float) Math.sin(Math.toRadians(angle)) * (float) range;
                float dy = (float) Math.cos(Math.toRadians(angle)) * (float) range;
                player.getLevel().entityManager.addParticle(player.x + dx, player.y + dy, new ParticleTypeSwitcher(Particle.GType.CRITICAL, Particle.GType.IMPORTANT_COSMETIC, Particle.GType.COSMETIC).next()).movesFriction(GameRandom.globalRandom.getIntBetween(-5, 5), GameRandom.globalRandom.getIntBetween(-5, 5), GameRandom.globalRandom.getFloatBetween(0.05F, 0.1F)).color(forcedColor != null ? forcedColor : color).heightMoves(GameRandom.globalRandom.getFloatBetween(0F, 3F), GameRandom.globalRandom.getFloatBetween(5F, 10F)).lifeTime(1000);
            }

            if(color != null) {
                int innerParticles = Math.round((float) (360 * range) / 400) - (antRange == 0 ? 0 : Math.round((float) (360 * antRange) / 400));

                if (0.1F * range + antRange < range * 0.9F) {
                    for (int i = 0; i < innerParticles / 5; i++) {
                        float angle = GameRandom.globalRandom.getIntBetween(0, 359);
                        float d = GameRandom.globalRandom.getFloatBetween(0.1F * range + antRange, 0.9F * range);
                        float dx = (float) Math.sin(Math.toRadians(angle)) * d;
                        float dy = (float) Math.cos(Math.toRadians(angle)) * d;
                        player.getLevel().entityManager.addParticle(player.x + dx, player.y + dy, new ParticleTypeSwitcher(Particle.GType.CRITICAL, Particle.GType.IMPORTANT_COSMETIC, Particle.GType.COSMETIC).next()).movesFriction(GameRandom.globalRandom.getIntBetween(-5, 5), GameRandom.globalRandom.getIntBetween(-5, 5), GameRandom.globalRandom.getFloatBetween(0.05F, 0.1F)).color(forcedColor != null ? forcedColor : color).heightMoves(GameRandom.globalRandom.getFloatBetween(0F, 3F), GameRandom.globalRandom.getFloatBetween(5F, 10F)).lifeTime(1000);
                    }
                }

                if(position > 0) {
                    AphoreaArea antArea = areaList.areas[position - 1];

                    if(antArea.color == null) {
                        antArea.showAreaParticles(player, areaList, color, rangeModifier);
                    }
                }
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
}
