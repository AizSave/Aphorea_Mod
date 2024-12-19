package aphorea.buffs;

import necesse.engine.util.GameRandom;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.MobBeforeHitEvent;
import necesse.entity.mobs.MobWasHitEvent;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.entity.particle.Particle.GType;

import java.awt.*;

public class StopBuff extends Buff {
    public StopBuff() {
        this.isImportant = false;
        this.canCancel = false;
        this.isVisible = false;
    }

    public void clientTick(ActiveBuff buff) {
        super.clientTick(buff);
        Mob owner = buff.owner;
        if (owner.isVisible() && GameRandom.globalRandom.nextInt(2) == 0) {
            owner.getLevel().entityManager.addParticle(owner.x + (float) (GameRandom.globalRandom.nextGaussian() * 6.0), owner.y + (float) (GameRandom.globalRandom.nextGaussian() * 8.0), GType.IMPORTANT_COSMETIC).movesConstant(owner.dx / 10.0F, owner.dy / 10.0F).color(new Color(0, 153, 255)).height(16.0F);
        }

    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        buff.addModifier(BuffModifiers.SLOW, 10F);
        buff.addModifier(BuffModifiers.SPEED, -10F);
        buff.addModifier(BuffModifiers.PARALYZED, true);
        buff.addModifier(BuffModifiers.UNTARGETABLE, true);
    }

    public void onBeforeHit(ActiveBuff buff, MobBeforeHitEvent event) {
        event.prevent();
        event.damage = new GameDamage(0);
    }

    public void onWasHit(ActiveBuff buff, MobWasHitEvent event) {
        if (!event.wasPrevented && event.damage != 0) {
            event.target.setHealth(event.target.getHealth() + event.damage);
        }
    }
}
