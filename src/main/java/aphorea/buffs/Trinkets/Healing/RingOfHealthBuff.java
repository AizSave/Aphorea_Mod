package aphorea.buffs.Trinkets.Healing;

import aphorea.other.AphoreaModifiers;
import aphorea.other.magichealing.AphoreaMagicHealing;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.trinketBuffs.TrinketBuff;

public class RingOfHealthBuff extends TrinketBuff {
    int count;

    public RingOfHealthBuff() {
    }

    @Override
    public void init(ActiveBuff activeBuff, BuffEventSubscriber buffEventSubscriber) {
        activeBuff.addModifier(AphoreaModifiers.MAGIC_HEALING_RECEIVED, 0.3F);
        activeBuff.addModifier(BuffModifiers.MAX_HEALTH_FLAT, 20);
        count = 60;
    }

    @Override
    public void serverTick(ActiveBuff buff) {
        super.serverTick(buff);
        if(count == 0) {
            AphoreaMagicHealing.healMob(buff.owner, buff.owner, 3);
            count = 60;
        }
        count--;
    }
}
