package aphorea.buffs.Banners;

import aphorea.registry.AphModifiers;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.VicinityBuff;
import necesse.gfx.GameColor;
import necesse.gfx.gameTooltips.ListGameTooltips;

public class StrikeBannerBuff extends VicinityBuff {
    public StrikeBannerBuff() {
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        float bannerEffect = 1F;
        if(buff.getAttacker() != null && buff.getAttacker().getAttackOwner() != null) {
            bannerEffect = buff.getAttacker().getAttackOwner().buffManager.getModifier(AphModifiers.BANNER_EFFECT);
        }
        buff.setModifier(BuffModifiers.HEALTH_REGEN, 0.10F * bannerEffect);
        buff.setModifier(BuffModifiers.COMBAT_HEALTH_REGEN, 0.10F * bannerEffect);
    }

    public void updateLocalDisplayName() {
        this.displayName = new LocalMessage("item", this.getStringID());
    }

    @Override
    public ListGameTooltips getTooltip(ActiveBuff ab, GameBlackboard blackboard) {
        float bannerEffect = 1F;
        if(ab.getAttacker() != null && ab.getAttacker().getAttackOwner() != null) {
            bannerEffect = ab.getAttacker().getAttackOwner().buffManager.getModifier(AphModifiers.BANNER_EFFECT);
        }
        ListGameTooltips tooltips = super.getTooltip(ab, blackboard);
        tooltips.add(BuffModifiers.HEALTH_REGEN.getTooltip(0.10F * bannerEffect, 0F).tip.toTooltip(GameColor.GREEN.color.get(), GameColor.RED.color.get(), GameColor.YELLOW.color.get(), false));
        tooltips.add(BuffModifiers.COMBAT_HEALTH_REGEN.getTooltip(0.10F * bannerEffect, 0F).tip.toTooltip(GameColor.GREEN.color.get(), GameColor.RED.color.get(), GameColor.YELLOW.color.get(), false));
        return tooltips;
    }
}
