package aphorea.buffs.TrinketsActive;

import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.Buff;

public class BloodyPeriaptActiveBuff extends Buff {
    public BloodyPeriaptActiveBuff() {
        this.isVisible = false;
        this.canCancel = false;
        this.shouldSave = false;
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        buff.addModifier(BuffModifiers.SPEED, 0.5F);
        buff.addModifier(BuffModifiers.ATTACK_SPEED, 0.3F);
    }
}

