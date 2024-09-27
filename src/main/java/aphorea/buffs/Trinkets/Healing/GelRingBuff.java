package aphorea.buffs.Trinkets.Healing;

import aphorea.other.AphoreaModifiers;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.trinketBuffs.TrinketBuff;

public class GelRingBuff extends TrinketBuff {
    public GelRingBuff() {
    }

    @Override
    public void init(ActiveBuff activeBuff, BuffEventSubscriber buffEventSubscriber) {
        activeBuff.addModifier(AphoreaModifiers.MAGIC_HEALING_RECEIVED, 0.3F);
    }
}
