package aphorea.other.magichealing;

import aphorea.other.AphoreaModifiers;
import necesse.entity.levelEvent.LevelEvent;
import necesse.entity.levelEvent.mobAbilityLevelEvent.MobHealthChangeEvent;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolItem;
import org.jetbrains.annotations.Nullable;

public class AphoreaMagicHealing {

    public static void healMob(PlayerMob player, Mob target, int healing) {
        int magicalHealing = AphoreaMagicHealing.getMagicHealing(player, target, healing);
        int realHealing = Math.min(magicalHealing, target.getMaxHealth() - target.getHealth());
        if(realHealing > 0) {
            LevelEvent healEvent = new MobHealthChangeEvent(target, realHealing);
            target.getLevel().entityManager.addLevelEvent(healEvent);

            if(player.getID() != target.getID()) {
                float healGrace = player.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_GRACE);
                int magicalHealingGrace = (int) Math.floor(realHealing * healGrace);
                int realHealingGrace = Math.min(magicalHealingGrace, player.getMaxHealth() - player.getHealth());
                if(realHealingGrace > 0) {
                    LevelEvent healEventPlayer = new MobHealthChangeEvent(player, realHealingGrace);
                    target.getLevel().entityManager.addLevelEvent(healEventPlayer);
                }
            }
        }
    }

    public static void healMob(PlayerMob player, Mob target, int healing, ToolItem toolItem, InventoryItem item) {
        int magicalHealing = AphoreaMagicHealing.getMagicHealing(player, target, healing, toolItem, item);
        int realHealing = Math.min(magicalHealing, target.getMaxHealth() - target.getHealth());
        if(realHealing > 0) {
            LevelEvent healEvent = new MobHealthChangeEvent(target, realHealing);
            target.getLevel().entityManager.addLevelEvent(healEvent);

            if(player.getID() != target.getID()) {
                float healGrace = player.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_GRACE)+ toolItem.getEnchantment(item).getModifier(AphoreaModifiers.TOOL_MAGIC_HEALING_GRACE);
                int magicalHealingGrace = (int) Math.floor(realHealing * healGrace);
                int realHealingGrace = Math.min(magicalHealingGrace, player.getMaxHealth() - player.getHealth());
                if(realHealingGrace > 0) {
                    LevelEvent healEventPlayer = new MobHealthChangeEvent(player, realHealingGrace);
                    target.getLevel().entityManager.addLevelEvent(healEventPlayer);
                }
            }
        }
    }


    public static int getMagicHealing(@Nullable PlayerMob player, @Nullable Mob target, int healing) {
        return (int) (getFlatMagicHealing(player, target, healing) * ((player == null ? 1.0F : player.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING)) + (target == null ? 1.0F : target.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_RECEIVED)) - 1));
    }

    public static int getMagicHealing(@Nullable PlayerMob player, @Nullable Mob target, int healing, ToolItem toolItem, InventoryItem item) {
        return (int) (getFlatMagicHealing(player, target, healing) * ((player == null ? 1.0F : player.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING) + toolItem.getEnchantment(item).getModifier(AphoreaModifiers.TOOL_MAGIC_HEALING)) + (target == null ? 1.0F : target.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_RECEIVED) + (player == target ? toolItem.getEnchantment(item).getModifier(AphoreaModifiers.TOOL_MAGIC_HEALING_RECEIVED) : 0)) - 1));
    }

    protected static int getFlatMagicHealing(@Nullable PlayerMob player, @Nullable Mob target, int healing) {
        return healing + (player == null ? 0 : player.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_FLAT)) + (target == null ? 0 : target.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_RECEIVED_FLAT));
    }

    public static String getMagicHealingToolTip(@Nullable PlayerMob playerMob, int healing) {
        int received = getMagicHealing(playerMob, playerMob, healing);
        int normal = getMagicHealing(playerMob, null, healing);
        if(received == normal) {
            return String.valueOf(normal);
        } else {
            return received + " | " + normal;
        }
    }

    public static String getMagicHealingToolTip(@Nullable PlayerMob playerMob, int healing, ToolItem toolItem, InventoryItem item) {
        int received = getMagicHealing(playerMob, playerMob, healing, toolItem, item);
        int normal = getMagicHealing(playerMob, null, healing, toolItem, item);
        if(received == normal) {
            return String.valueOf(normal);
        } else {
            return received + " | " + normal;
        }
    }

    public static String getMagicHealingToolTipPercent(@Nullable PlayerMob player, @Nullable Mob target, float healingPercent, int healing) {
        String toolTip;
        healingPercent *= ((player == null ? 1.0F : player.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING) + (target == null ? 1.0F : target.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_RECEIVED)) - 1));

        if(healingPercent < 0) {
            toolTip = "0%";
        } else {
            toolTip = (int) healingPercent + "%";
        }

        healing += (player == null ? 0 : player.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_FLAT)) + (target == null ? 0 : target.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_RECEIVED_FLAT));
        if(healing > 0) {
            toolTip += " +" + healing;
        } else if(healing < 0) {
            toolTip += " -" + healing;
        }
        return toolTip;
    }

    public static String getMagicHealingToolTipPercent(@Nullable PlayerMob player, @Nullable Mob target, float healingPercent, int healing, ToolItem toolItem, InventoryItem item) {
        String toolTip;
        healingPercent *= ((player == null ? 1.0F : player.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING) + toolItem.getEnchantment(item).getModifier(AphoreaModifiers.TOOL_MAGIC_HEALING)) + (target == null ? 1.0F : target.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_RECEIVED) + (player == target ? toolItem.getEnchantment(item).getModifier(AphoreaModifiers.TOOL_MAGIC_HEALING_RECEIVED) : 0)) - 1);

        if(healingPercent < 0) {
            toolTip = "0%";
        } else {
            toolTip = (int) healingPercent + "%";
        }

        healing += (player == null ? 0 : player.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_FLAT)) + (target == null ? 0 : target.buffManager.getModifier(AphoreaModifiers.MAGIC_HEALING_RECEIVED_FLAT));
        if(healing > 0) {
            toolTip += " +" + healing;
        } else if(healing < 0) {
            toolTip += " -" + healing;
        }
        return toolTip;
    }
}
