package aphorea.buffs.Trinkets.Periapts;

import aphorea.other.buffs.trinkets.AphoreaPeriaptActivableBuff;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;

import java.awt.*;

public class RockyPeriaptBuff extends AphoreaPeriaptActivableBuff {

    public RockyPeriaptBuff() {
        super("rockyperiaptactivebuff");
    }

    @Override
    public Color getColor() {
        return new Color(153, 153, 153);
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        buff.addModifier(BuffModifiers.SPEED, -0.05F);
        buff.addModifier(BuffModifiers.ARMOR_FLAT, 5);
    }
}