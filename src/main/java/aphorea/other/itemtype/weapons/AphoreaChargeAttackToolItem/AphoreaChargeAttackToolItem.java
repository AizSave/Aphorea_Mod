package aphorea.other.itemtype.weapons.AphoreaChargeAttackToolItem;

import aphorea.other.vanillaitemtypes.weapons.AphoreaSwordToolItem;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.network.PacketReader;
import necesse.engine.util.GameMath;
import necesse.entity.mobs.AttackAnimMob;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.friendly.human.HumanMob;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import necesse.level.maps.Level;

import java.awt.geom.Point2D;

abstract public class AphoreaChargeAttackToolItem extends AphoreaSwordToolItem {
    public AphoreaCustomChargeLevel<AphoreaChargeAttackToolItem>[] chargeLevels;
    public boolean isCharging;
    public boolean evadeCharge = false;

    public AphoreaChargeAttackToolItem(int enchantCost, AphoreaCustomChargeLevel<AphoreaChargeAttackToolItem>[] chargeLevels) {
        super(enchantCost);
        this.chargeLevels = chargeLevels;
        if (chargeLevels.length == 0) {
            throw new IllegalArgumentException("Must have at least one charge level for charged items");
        } else {
            this.attackAnimTime.setBaseValue(200);
            this.attackXOffset = 16;
            this.attackYOffset = 16;
            this.resilienceGain.setBaseValue(4.0F);
        }

        this.isCharging = false;
    }

    @Override
    public GameMessage getSettlerCanUseError(HumanMob mob, InventoryItem item) {
        return new LocalMessage("ui", "settlercantuseitem");
    }

    @Override
    public float getSwingRotation(InventoryItem item, int dir, float attackProgress) {
        if(evadeCharge) {
            super.getSwingRotation(item, dir, attackProgress);
        } else {
            if (!item.getGndData().getBoolean("shouldFire")) {
                float chargePercent = item.getGndData().getFloat("chargePercent");
                chargePercent = GameMath.limit(chargePercent, 0.0F, 1.0F);
                attackProgress = 0.2F - chargePercent * 0.2F;
            }
        }

        return super.getSwingRotation(item, dir, attackProgress);
    }

    @Override
    public void showAttack(Level level, int x, int y, AttackAnimMob mob, int attackHeight, InventoryItem item, int seed, PacketReader contentReader) {
        if(evadeCharge) {
            super.showAttack(level, x, y, mob, attackHeight, item, seed, contentReader);
        } else {
            if (item.getGndData().getBoolean("shouldFire")) {
                super.showAttack(level, x, y, mob, attackHeight, item, seed, contentReader);
            }
        }
    }

    @Override
    public String canAttack(Level level, int x, int y, PlayerMob player, InventoryItem item) {
        if (player.isAttacking || this.isCharging) {
            return "Already attacking";
        }
        return super.canAttack(level, x, y, player, item);
    }

    @Override
    public InventoryItem onAttack(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int animAttack, int seed, PacketReader contentReader) {
        if(evadeCharge) {
            return super.onAttack(level, x, y, player, attackHeight, item, slot, animAttack, seed, contentReader);
        } else {
            isCharging = true;

            AphoreaCustomChargeLevel<AphoreaChargeAttackToolItem>[] charge = this.chargeLevels;
            player.startAttackHandler(new AphoreaCustomChargeAttackHandler<>(player, slot, item, this, seed, x, y, charge));

            return item;
        }
    }

    @Override
    public GameDamage getAttackDamage(InventoryItem item) {
        if(evadeCharge) {
            return super.getAttackDamage(item);
        } else {
            float damageMultiplier = item.getGndData().getFloat("chargeDamageMultiplier", 1.0F);
            return super.getAttackDamage(item).modDamage(damageMultiplier);
        }
    }

    @Override
    public boolean animDrawBehindHand(InventoryItem item) {
        if(evadeCharge) {
            return super.animDrawBehindHand(item);
        } else {
            return true;
        }
    }

    @Override
    public boolean getConstantUse(InventoryItem item) {
        if(evadeCharge) {
            return super.getConstantUse(item);
        } else {
            return false;
        }
    }

    @Override
    public boolean shouldRunOnAttackedBuffEvent(Level level, int x, int y, PlayerMob player, InventoryItem item, PlayerInventorySlot slot, int animTime, int seed, PacketReader contentReader) {
        if(evadeCharge) {
            return super.shouldRunOnAttackedBuffEvent(level, x, y, player, item, slot, animTime, seed, contentReader);
        } else {
            return false;
        }
    }

    public void endChargeAttack(PlayerMob player, Point2D.Float dir, int charge) {
    }

    public void superOnAttack(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int animAttack, int seed, PacketReader contentReader) {
        super.onAttack(level, x, y, player, attackHeight, item, slot, animAttack, seed, contentReader);
    }
}
