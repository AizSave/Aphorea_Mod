package aphorea.buffs.Trinkets.Healing;

import aphorea.other.magichealing.AphoreaMagicHealing;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.trinketBuffs.TrinketBuff;

public class FloralRingBuff extends TrinketBuff {
    int count;

    public FloralRingBuff() {
    }

    @Override
    public void init(ActiveBuff activeBuff, BuffEventSubscriber buffEventSubscriber) {
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
