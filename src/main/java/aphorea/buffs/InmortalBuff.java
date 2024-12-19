package aphorea.buffs;

import necesse.engine.util.GameRandom;
import necesse.entity.ParticleTypeSwitcher;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.MobBeforeHitEvent;
import necesse.entity.mobs.MobWasHitEvent;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.entity.particle.Particle;

import java.awt.*;

public class InmortalBuff extends Buff {
    public InmortalBuff() {
        this.isImportant = true;
        this.canCancel = false;
        this.isVisible = true;
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        buff.addModifier(BuffModifiers.UNTARGETABLE, true);
    }

    public void clientTick(ActiveBuff buff) {
        super.clientTick(buff);
        Mob owner = buff.owner;
        if (owner.isVisible() && owner.isPlayer) {
            for (int i = 0; i < 3; i++) {
                int angle = (int) (360.0F + GameRandom.globalRandom.nextFloat() * 360.0F);
                float dx = (float) Math.sin(Math.toRadians(angle)) * (float) GameRandom.globalRandom.getIntBetween(-30, 30);
                float dy = (float) Math.cos(Math.toRadians(angle)) * (float) GameRandom.globalRandom.getIntBetween(-30, 30);
                owner.getLevel().entityManager.addParticle(dx, dy, new ParticleTypeSwitcher(Particle.GType.CRITICAL, Particle.GType.IMPORTANT_COSMETIC, Particle.GType.COSMETIC).next()).movesFriction(0, 0, 0.8F).color(new Color(255, 255, 0)).heightMoves(30.0F, 30.0F).lifeTime(100);
            }
        }
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
