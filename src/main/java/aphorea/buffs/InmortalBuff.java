package aphorea.buffs;

import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.MobBeforeHitEvent;
import necesse.entity.mobs.MobWasHitEvent;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.staticBuffs.Buff;

public class InmortalBuff extends Buff {
    public InmortalBuff() {
        this.isImportant = false;
        this.canCancel = false;
        this.isVisible = false;
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
    }

    public void onBeforeHit(ActiveBuff buff, MobBeforeHitEvent event) {
        event.prevent();
        event.damage = new GameDamage(0);
    }

    public void onWasHit(ActiveBuff buff, MobWasHitEvent event) {
        if(!event.wasPrevented && event.damage != 0) {
            event.target.setHealth(event.target.getHealth() + event.damage);
        }
    }
}
