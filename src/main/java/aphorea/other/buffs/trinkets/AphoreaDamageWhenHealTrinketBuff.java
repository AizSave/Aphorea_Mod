package aphorea.other.buffs.trinkets;

import aphorea.other.area.AphoreaArea;
import aphorea.other.area.AphoreaAreaList;
import aphorea.other.magichealing.AphoreaMagicHealingFunctions;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.trinketBuffs.TrinketBuff;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolItem;
import org.jetbrains.annotations.Nullable;

abstract public class AphoreaDamageWhenHealTrinketBuff extends TrinketBuff implements AphoreaMagicHealingFunctions {
    int healingDone;
    boolean showParticles = false;

    AphoreaAreaList areaDamage;

    public AphoreaDamageWhenHealTrinketBuff(AphoreaArea... areaDamage) {
        this.areaDamage = new AphoreaAreaList(areaDamage);
    }

    @Override
    public void init(ActiveBuff activeBuff, BuffEventSubscriber buffEventSubscriber) {
        this.healingDone = 0;
    }

    @Override
    public void tickEffect(ActiveBuff buff, Mob owner) {
        super.tickEffect(buff, owner);
        if(showParticles) {
            this.showParticles = false;
            this.areaDamage.showAllAreaParticles(buff.owner, 1F);
        }
    }

    public void onMagicalHealing(Mob healer, Mob target, int healing, @Nullable ToolItem toolItem, @Nullable InventoryItem item) {
        this.healingDone += healing;

        if(this.healingDone > 30) {
            this.healingDone -= 30;
            this.areaDamage.executeAreas(healer, 1F);
            this.showParticles = true;
        }
    }
}
