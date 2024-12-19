package aphorea.buffs.SetBonus;

import aphorea.registry.AphModifiers;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.setBonusBuffs.SetBonusBuff;
import necesse.inventory.item.ItemStatTip;

import java.util.LinkedList;

public class SwampMaskSetBonusBuff extends SetBonusBuff {
    public SwampMaskSetBonusBuff() {
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        buff.addModifier(AphModifiers.MAGIC_HEALING_FLAT, 1);
    }

    public void addStatTooltips(LinkedList<ItemStatTip> list, ActiveBuff currentValues, ActiveBuff lastValues) {
        super.addStatTooltips(list, currentValues, lastValues);
        currentValues.getModifierTooltipsBuilder(true, true).addLastValues(lastValues).buildToStatList(list);
    }
}