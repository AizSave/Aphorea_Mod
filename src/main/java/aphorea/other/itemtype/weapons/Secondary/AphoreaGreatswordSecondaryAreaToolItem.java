package aphorea.other.itemtype.weapons.Secondary;

import aphorea.other.itemtype.weapons.AphoreaChargeAttackToolItem.AphoreaGreatswordCustomChargeToolItem;
import aphorea.other.itemtype.weapons.AphoreaChargeAttackToolItem.AphoreaCustomGreatswordChargeAttackHandler;
import aphorea.other.itemtype.weapons.AphoreaChargeAttackToolItem.AphoreaCustomGreatswordChargeLevel;
import necesse.engine.localization.Localization;
import necesse.engine.network.PacketReader;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.attackHandler.GreatswordChargeLevel;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import necesse.inventory.item.ItemInteractAction;
import necesse.level.maps.Level;

import java.awt.*;
import java.awt.geom.Point2D;

abstract public class AphoreaGreatswordSecondaryAreaToolItem extends AphoreaGreatswordCustomChargeToolItem implements ItemInteractAction {
    int mode;
    AphoreaCustomGreatswordChargeLevel[] chargeLevelsSecondary;
    int areaAttackAnimTime;
    int normalAttackAnimTime;

    public AphoreaGreatswordSecondaryAreaToolItem(int enchantCost, int normalAttackAnimTime, int areaAttackAnimTime, GreatswordChargeLevel[] chargeLevels, AphoreaCustomGreatswordChargeLevel[] chargeLevelsSecondary) {
        super(enchantCost, chargeLevels);
        this.chargeLevelsSecondary = chargeLevelsSecondary;
        if (chargeLevelsSecondary.length == 0) {
            throw new IllegalArgumentException("Must have at least one charge level for secondary charge attack tool items");
        }

        this.attackAnimTime.setBaseValue(normalAttackAnimTime);

        this.normalAttackAnimTime = normalAttackAnimTime;
        this.areaAttackAnimTime = areaAttackAnimTime;

    }

    @Override
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "fisicareasecondaryattack"));
        return tooltips;
    }

    public static AphoreaCustomGreatswordChargeLevel[] getChargeAreaLevels(int time, float damageModifier, Color colorSinCarga, Color color) {
        return new AphoreaCustomGreatswordChargeLevel[]{new AphoreaCustomGreatswordChargeLevel(0, damageModifier / 2, colorSinCarga), new AphoreaCustomGreatswordChargeLevel(time, damageModifier, color)};
    }

    @Override
    public float getSwingRotationOffset(InventoryItem item, int dir, float swingAngle) {
        float offset = super.getSwingRotationOffset(item, dir, swingAngle);
        if(mode == 2 && (dir == 1 || dir == 3)) offset -= 90;
        return offset;
    }

    @Override
    public InventoryItem onAttack(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int animAttack, int seed, PacketReader contentReader) {
        player.isAttacking = true;
        if(mode == 2) {
            mode = 0;
        }
        if(mode == 0) {
            return super.onAttack(level, x, y, player, attackHeight, item, slot, animAttack, seed, contentReader);
        } else {
            player.startAttackHandler(new AphoreaCustomGreatswordChargeAttackHandler<AphoreaGreatswordCustomChargeToolItem>(player, slot, item, this, seed, x, y, this.chargeLevelsSecondary));
            return item;
        }
    }

    @Override
    public int getLevelInteractAttackAnimTime(InventoryItem item, PlayerMob player) {
        return 500;
    }

    @Override
    public boolean canLevelInteract(Level level, int x, int y, PlayerMob player, InventoryItem item) {
        return !player.isRiding() && mode != 1 && !player.isAttacking && !player.buffManager.hasBuff("greatswordareaattackcooldown");
    }

    @Override
    public InventoryItem onLevelInteract(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int seed, PacketReader contentReader) {

        if(player.isClient()) {
            mode = 1;
            this.attackAnimTime.setBaseValue(areaAttackAnimTime);
        }

        return item;
    }

    @Override
    public float getHitboxSwingAngle(InventoryItem item, int dir) {
        return mode != 2 ? 150.0F : 360.0F;
    }

    public float getSwingRotationAngle(InventoryItem item, int dir) {
        return mode != 2 ? 150.0F : 360.0F;
    }

    @Override
    public void endChargeAttack(PlayerMob player, Point2D.Float dir, int charge) {
        mode = 2;
        this.attackAnimTime.setBaseValue(normalAttackAnimTime);
        super.endChargeAttack(player, dir, charge);
    }
}
