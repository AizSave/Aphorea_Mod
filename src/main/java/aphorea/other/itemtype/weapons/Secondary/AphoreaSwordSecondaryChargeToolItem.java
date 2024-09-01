package aphorea.other.itemtype.weapons.Secondary;

import aphorea.other.itemtype.weapons.AphoreaChargeAttackToolItem.AphoreaChargeAttackToolItem;
import aphorea.other.itemtype.weapons.AphoreaChargeAttackToolItem.AphoreaCustomChargeAttackHandler;
import aphorea.other.itemtype.weapons.AphoreaChargeAttackToolItem.AphoreaCustomChargeLevel;
import necesse.engine.network.PacketReader;
import necesse.engine.registries.BuffRegistry;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import necesse.inventory.item.ItemInteractAction;
import necesse.level.maps.Level;

import java.awt.*;

public class AphoreaSwordSecondaryChargeToolItem extends AphoreaChargeAttackToolItem implements ItemInteractAction {
    public AphoreaSwordSecondaryChargeToolItem(int enchantCost, AphoreaCustomChargeLevel[] chargeLevels) {
        super(enchantCost, chargeLevels);

        this.enchantCost.setUpgradedValue(1.0F, 500);

        this.attackAnimTime.setBaseValue(300);

        this.evadeCharge = true;
    }

    @Override
    public InventoryItem onAttack(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int animAttack, int seed, PacketReader contentReader) {
        if(evadeCharge) {
            return super.onAttack(level, x, y, player, attackHeight, item, slot, animAttack, seed, contentReader);
        } else {
            isCharging = true;

            AphoreaCustomChargeLevel[] charge = this.chargeLevels;
            player.startAttackHandler(new ChargeAttackHandler(player, slot, item, this, seed, x, y, charge));

            return item;
        }
    }

    public static AphoreaCustomChargeLevel[] getChargeLevels(int time, float damageModifier, Color color, Color colorCharged) {
        return new AphoreaCustomChargeLevel[]{new AphoreaCustomChargeLevel(0, 1.0F, color), new AphoreaCustomChargeLevel(time, damageModifier, colorCharged)};
    }

    @Override
    public int getLevelInteractAttackAnimTime(InventoryItem item, PlayerMob player) {
        return 500;
    }

    @Override
    public boolean canLevelInteract(Level level, int x, int y, PlayerMob player, InventoryItem item) {
        return !player.isRiding() && evadeCharge && !player.isAttacking && !this.isCharging && !player.buffManager.hasBuff("swordchargeattackcooldown");
    }

    public InventoryItem onLevelInteract(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int seed, PacketReader contentReader) {

        if(player.isClient()) {
            evadeCharge = false;
        }

        return item;
    }

    public static class ChargeAttackHandler extends AphoreaCustomChargeAttackHandler<AphoreaSwordSecondaryChargeToolItem> {

        public ChargeAttackHandler(PlayerMob player, PlayerInventorySlot slot, InventoryItem item, AphoreaSwordSecondaryChargeToolItem toolItem, int seed, int startX, int startY, AphoreaCustomChargeLevel<AphoreaSwordSecondaryChargeToolItem>[] chargeLevels) {
            super(player, slot, item, toolItem, seed, startX, startY, chargeLevels);
        }

        @Override
        public void onEndAttack(boolean bySelf) {
            super.onEndAttack(bySelf);

            toolItem.evadeCharge = true;

            if(!this.endedByInteract) {
                player.buffManager.addBuff(new ActiveBuff(BuffRegistry.getBuff("swordchargeattackcooldown"), player, 3.0F, (Attacker)null), false);
            }

        }
    }

}
