package aphorea.buffs.Trinkets;

import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.trinketBuffs.TrinketBuff;

public class HeartRingBuff extends TrinketBuff {
    public HeartRingBuff() {
    }

    @Override
    public void init(ActiveBuff activeBuff, BuffEventSubscriber buffEventSubscriber) {
        activeBuff.addModifier(BuffModifiers.MAX_HEALTH_FLAT, 20);
    }
}
