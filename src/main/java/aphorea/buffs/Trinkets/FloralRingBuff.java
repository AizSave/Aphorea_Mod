package aphorea.buffs.Trinkets;

import aphorea.other.magichealing.AphoreaMagicHealing;
import necesse.entity.mobs.PlayerMob;
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
            AphoreaMagicHealing.healMob((PlayerMob) buff.owner, buff.owner, 1);
            count = 60;
        }
        count--;
    }
}
