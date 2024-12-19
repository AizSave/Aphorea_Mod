package aphorea.buffs.SetBonus;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.setBonusBuffs.SetBonusBuff;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.gfx.gameTooltips.StringTooltips;
import necesse.inventory.item.ItemStatTip;

import java.util.LinkedList;

public class GoldHatSetBonusBuff extends SetBonusBuff {
    public GoldHatSetBonusBuff() {
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
    }

    public void addStatTooltips(LinkedList<ItemStatTip> list, ActiveBuff currentValues, ActiveBuff lastValues) {
        super.addStatTooltips(list, currentValues, lastValues);
        currentValues.getModifierTooltipsBuilder(false, false).addLastValues(lastValues).buildToStatList(list);
    }

    public ListGameTooltips getTooltip(ActiveBuff ab, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltip(ab, blackboard);
        tooltips.add(new StringTooltips(Localization.translate("itemtooltip", "goldhatsetbonus")));
        return tooltips;
    }

    public void clientTick(ActiveBuff buff) {
        super.clientTick(buff);
        Mob owner = buff.owner;

        if (owner.getCurrentSpeed() == 0) {
            if (buff.getModifier(BuffModifiers.RANGED_ATTACK_SPEED) != 0.6F)
                buff.setModifier(BuffModifiers.RANGED_ATTACK_SPEED, 0.6F);
        } else {
            if (buff.getModifier(BuffModifiers.RANGED_ATTACK_SPEED) != 0F)
                buff.setModifier(BuffModifiers.RANGED_ATTACK_SPEED, 0F);
        }
    }

}