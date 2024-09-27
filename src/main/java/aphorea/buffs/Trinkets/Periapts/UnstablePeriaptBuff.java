package aphorea.buffs.Trinkets.Periapts;

import aphorea.other.buffs.trinkets.AphoreaSummoningTrinketBuff;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.buffs.ActiveBuff;

public class UnstablePeriaptBuff extends AphoreaSummoningTrinketBuff {

    public UnstablePeriaptBuff() {
        super("unstableperiaptbuff", "babyunstablegelslime", 2, new GameDamage(DamageTypeRegistry.SUMMON, 8));
    }

    public void serverTick(ActiveBuff buff) {
        super.serverTick(buff);

        if(buff.owner.buffManager.hasBuff("sticky")) {
            buff.owner.buffManager.removeBuff("sticky", true);
        }
    }
}