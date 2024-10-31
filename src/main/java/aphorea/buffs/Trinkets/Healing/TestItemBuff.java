package aphorea.buffs.Trinkets.Healing;

import aphorea.other.area.AphoreaArea;
import aphorea.other.area.AphoreaAreaList;
import aphorea.other.magichealing.AphoreaMagicHealing;
import aphorea.other.magichealing.AphoreaMagicHealingFunctions;
import aphorea.other.utils.AphoreaDistances;
import aphorea.projectiles.DamageProjectile;
import aphorea.projectiles.HealingToolItemProjectile;
import necesse.engine.util.GameRandom;
import necesse.engine.util.GameUtils;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.trinketBuffs.TrinketBuff;
import necesse.entity.projectile.Projectile;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolItem;
import necesse.level.maps.Level;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Comparator;
import java.util.Objects;

public class TestItemBuff extends TrinketBuff implements AphoreaMagicHealingFunctions {
    boolean showParticles = false;

    static int distance = 500;

    AphoreaAreaList areaDamage;

    int angle;
    boolean attack;

    public TestItemBuff() {
    }

    @Override
    public void init(ActiveBuff activeBuff, BuffEventSubscriber buffEventSubscriber) {
    }

    @Override
    public void tickEffect(ActiveBuff buff, Mob owner) {
        super.tickEffect(buff, owner);
        if(showParticles) {
            this.showParticles = false;
            this.areaDamage.showAllAreaParticles(buff.owner);
        }
    }

    @Override
    public void onMagicalHealingItemUsed(Mob mob, ToolItem toolItem, InventoryItem item) {
        float multiplier = (float) toolItem.getAttackAnimTime(item, mob) / 1000;
        if(multiplier > 1 || Objects.equals(toolItem.getStringID(), "magicalvial")) multiplier = 1;
        Projectile projectile;

        if(mob.isClient()) {
            angle = new GameRandom().getIntBetween(1, 360);
            attack = new GameRandom().getChance(0.5F);
        }

        int randomX = (int) (mob.x + Math.cos(angle) * 100);
        int randomY = (int) (mob.y + Math.sin(angle) * 100);

        if(attack) {
            Mob target = AphoreaDistances.findClosestMob(mob, m -> m.canBeTargeted(mob, ((PlayerMob) mob).getNetworkClient()), distance * 2);
            int damage = (int) (20 * multiplier);
            projectile = damageProjectile(mob.getLevel(), target != null ? (int) target.x : randomX, target != null ? (int) target.y : randomY, (PlayerMob) mob, damage);
        } else {
            Mob target = AphoreaDistances.findClosestMob(mob, m -> AphoreaMagicHealing.canHealMob(mob, m) && mob != m, distance * 2);
            int healing = (int) (5 * multiplier);
            projectile = healingProjectile(mob.getLevel(), target != null ? (int) target.x : randomX, target != null ? (int) target.y : randomY, (PlayerMob) mob, item, healing);

        }
        projectile.getUniqueID(new GameRandom(mob.getLevel().getSeed()));
        mob.getLevel().entityManager.projectiles.addHidden(projectile);
    }

    static public Projectile healingProjectile(Level level, int x, int y, PlayerMob player, InventoryItem item, int healing) {
        return new HealingToolItemProjectile(new Color(0, 214, 0), healing, null, item, level, player,
                player.x, player.y,
                x, y,
                200,
                distance,
                0.5F
        );
    }

    static public Projectile damageProjectile(Level level, int x, int y, PlayerMob player, int damage) {
        return new DamageProjectile(new Color(214, 0, 0), new GameDamage(damage), level, player,
                player.x, player.y,
                x, y,
                200,
                distance,
                0.5F
        );
    }
}
