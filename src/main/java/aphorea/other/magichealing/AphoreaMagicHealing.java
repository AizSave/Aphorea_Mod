package aphorea.other.magichealing;

import aphorea.other.AphoreaModifiers;
import necesse.entity.levelEvent.LevelEvent;
import necesse.entity.levelEvent.mobAbilityLevelEvent.MobHealthChangeEvent;
import necesse.entity.mobs.Mob;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolItem;
import org.jetbrains.annotations.Nullable;

public class AphoreaMagicHealing {


    public static void healMob(Mob healer, Mob target, int healing) {
        healMob(healer, target, healing, null, null);
    }

    public static void healMob(Mob healer, Mob target, int healing, @Nullable ToolItem toolItem, @Nullable InventoryItem item) {
        int magicalHealing = AphoreaMagicHealing.getMagicHealing(healer, target, healing, toolItem, item);
        int realHealing = Math.min(magicalHealing, target.getMaxHealth() - target.getHealth());
        if(realHealing > 0) {
            LevelEvent healEvent = new MobHealthChangeEvent(target, realHealing);
            target.getLevel().entityManager.addLevelEvent(healEvent);

            healer.buffManager.getArrayBuffs().stream().filter(buff -> buff.buff instanceof AphoreaMagicHealingFunctions).forEach(buff -> ((AphoreaMagicHealingFunctions) buff.buff).onMagicalHealing(healer, target, healing, toolItem, item));
            if(toolItem instanceof AphoreaMagicHealingFunctions) {
                ((AphoreaMagicHealingFunctions) toolItem).onMagicalHealing(healer, target, healing, toolItem, item);
            }

            if(healer.getID() != target.getID()) {
                float healGrace = healer.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_GRACE) + (toolItem == null || item == null ? 0 : toolItem.getEnchantment(item).getModifier(AphoreaModifiers.TOOL_MAGIC_HEALING_GRACE));
                int magicalHealingGrace = (int) Math.floor(realHealing * healGrace);
                int realHealingGrace = Math.min(magicalHealingGrace, healer.getMaxHealth() - healer.getHealth());
                if(realHealingGrace > 0) {
                    LevelEvent healEventhealer = new MobHealthChangeEvent(healer, realHealingGrace);
                    target.getLevel().entityManager.addLevelEvent(healEventhealer);
                }
            }
        }
    }


    public static int getMagicHealing(@Nullable Mob healer, @Nullable Mob target, int healing) {
        return getMagicHealing(healer, target, healing, null, null);
    }

    public static int getMagicHealing(@Nullable Mob healer, @Nullable Mob target, int healing, @Nullable ToolItem toolItem, @Nullable InventoryItem item) {
        return (int) (getFlatMagicHealing(healer, target, healing) * ((healer == null ? 1.0F : healer.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING) + (toolItem == null || item == null ? 0 : toolItem.getEnchantment(item).getModifier(AphoreaModifiers.TOOL_MAGIC_HEALING))) + (target == null ? 1.0F : target.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_RECEIVED) + (healer == target ? (toolItem == null || item == null ? 0 : toolItem.getEnchantment(item).getModifier(AphoreaModifiers.TOOL_MAGIC_HEALING_RECEIVED)) : 0)) - 1));
    }


    protected static int getFlatMagicHealing(@Nullable Mob healer, @Nullable Mob target, int healing) {
        return healing + (healer == null ? 0 : healer.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_FLAT)) + (target == null ? 0 : target.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_RECEIVED_FLAT));
    }


    public static String getMagicHealingToolTip(@Nullable Mob healerMob, int healing) {
        return getMagicHealingToolTip(healerMob, healing, null, null);
    }

    public static String getMagicHealingToolTip(@Nullable Mob healerMob, int healing, ToolItem toolItem, InventoryItem item) {
        int received = getMagicHealing(healerMob, healerMob, healing, toolItem, item);
        int normal = getMagicHealing(healerMob, null, healing, toolItem, item);
        if(received == normal) {
            return String.valueOf(normal);
        } else {
            return received + " | " + normal;
        }
    }


    public static String getMagicHealingToolTipPercent(@Nullable Mob healer, @Nullable Mob target, float healingPercent, int healing) {
        return getMagicHealingToolTipPercent(healer, target, healingPercent, healing, null, null);
    }

    public static String getMagicHealingToolTipPercent(@Nullable Mob healer, @Nullable Mob target, float healingPercent, int healing, @Nullable ToolItem toolItem, @Nullable InventoryItem item) {
        String toolTip;
        healingPercent *= ((healer == null ? 1.0F : healer.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING) + (toolItem == null || item == null ? 0 : toolItem.getEnchantment(item).getModifier(AphoreaModifiers.TOOL_MAGIC_HEALING))) + (target == null ? 1.0F : target.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_RECEIVED) + (healer == target ? (toolItem == null || item == null ? 0 : toolItem.getEnchantment(item).getModifier(AphoreaModifiers.TOOL_MAGIC_HEALING_RECEIVED)) : 0)) - 1);

        if(healingPercent < 0) {
            toolTip = "0%";
        } else {
            toolTip = (int) healingPercent + "%";
        }

        healing += (healer == null ? 0 : healer.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_FLAT)) + (target == null ? 0 : target.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_RECEIVED_FLAT));
        if(healing > 0) {
            toolTip += " +" + healing;
        } else if(healing < 0) {
            toolTip += " -" + healing;
        }
        return toolTip;
    }
}
