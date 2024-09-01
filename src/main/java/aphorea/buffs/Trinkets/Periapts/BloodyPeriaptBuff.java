package aphorea.buffs.Trinkets.Periapts;

import aphorea.other.buffclass.PeriaptActivableBuff;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.ParticleTypeSwitcher;
import necesse.entity.levelEvent.LevelEvent;
import necesse.entity.levelEvent.mobAbilityLevelEvent.MobHealthChangeEvent;
import necesse.entity.mobs.*;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.particle.Particle;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class BloodyPeriaptBuff extends PeriaptActivableBuff {

    boolean drenar;

    public BloodyPeriaptBuff() {
        super("bloodyperiaptactivebuff");
    }

    @Override
    public Color getColor() {
        return new Color(255, 0, 0);
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        drenar = false;
    }

    public void onItemAttacked(ActiveBuff buff, int targetX, int targetY, PlayerMob player, int attackHeight, @NotNull InventoryItem item, PlayerInventorySlot slot, int animAttack) {
        String itemID = item.item.getStringID();
        if(itemID.equals("bloodbolt") || itemID.equals("bloodvolley")) {
            drenar = true;
        } else if(drenar) {
            drenar = false;
        }
    }

    public void onHasAttacked(ActiveBuff buff, MobWasHitEvent event) {
        if(!event.wasPrevented && event.damageType.equals(DamageTypeRegistry.MAGIC)) {
            Mob owner = event.attacker.getAttackOwner();
            if(drenar || owner.buffManager.hasBuff("bloodyperiaptactivebuff")) {
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
                        owner.getLevel().entityManager.addParticle(owner, new ParticleTypeSwitcher(Particle.GType.CRITICAL, Particle.GType.IMPORTANT_COSMETIC, Particle.GType.COSMETIC).next()).movesFriction(dx, dy, 0.8F).color(new Color(255, 0, 0)).heightMoves(10.0F, 30.0F).lifeTime(500);
                    }
                }
            }

        }
    }

}