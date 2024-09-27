package aphorea.buffs.Trinkets.Periapts;

import aphorea.other.buffs.trinkets.AphoreaPeriaptActivableBuff;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.ParticleTypeSwitcher;
import necesse.entity.levelEvent.LevelEvent;
import necesse.entity.levelEvent.mobAbilityLevelEvent.MobHealthChangeEvent;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.MobWasHitEvent;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.particle.Particle;

import java.awt.*;

public class DemonicPeriaptBuff extends AphoreaPeriaptActivableBuff {

    public DemonicPeriaptBuff() {
        super("demonicperiaptactivebuff");
    }

    @Override
    public Color getColor() {
        return new Color(204, 0, 0);
    }

    public void onHasAttacked(ActiveBuff buff, MobWasHitEvent event) {
        if(!event.wasPrevented) {
            Mob owner = event.attacker.getAttackOwner();
            if(event.damageType.equals(DamageTypeRegistry.MAGIC) || owner.buffManager.hasBuff("demonicperiaptactivebuff")) {
                int heal = (int) Math.ceil(event.damage * 0.1F);

                if(heal > 0) {
                    if (owner.isServer()) {
                        LevelEvent healEvent = new MobHealthChangeEvent(owner, heal);
                        owner.getLevel().entityManager.addLevelEvent(healEvent);
                    }

                    for(int i = 0; i < 20; i++) {
                        int angle = (int)(360.0F + GameRandom.globalRandom.nextFloat() * 360.0F);
                        float dx = (float)Math.sin(Math.toRadians(angle)) * (float)GameRandom.globalRandom.getIntBetween(30, 50);
                        float dy = (float)Math.cos(Math.toRadians(angle)) * (float)GameRandom.globalRandom.getIntBetween(30, 50);
                        owner.getLevel().entityManager.addParticle(owner, new ParticleTypeSwitcher(Particle.GType.CRITICAL, Particle.GType.IMPORTANT_COSMETIC, Particle.GType.COSMETIC).next()).movesFriction(dx, dy, 0.8F).color(new Color(204, 0, 0)).heightMoves(10.0F, 30.0F).lifeTime(500);
                    }
                }
            }

        }
    }

}