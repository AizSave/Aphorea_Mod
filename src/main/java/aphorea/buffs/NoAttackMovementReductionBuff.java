package aphorea.buffs;

import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.Buff;

public class NoAttackMovementReductionBuff extends Buff {
    public NoAttackMovementReductionBuff() {
        this.isImportant = false;
        this.canCancel = false;
        this.isVisible = false;
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        buff.setModifier(BuffModifiers.ATTACK_MOVEMENT_MOD, 0.0F);
    }

}
