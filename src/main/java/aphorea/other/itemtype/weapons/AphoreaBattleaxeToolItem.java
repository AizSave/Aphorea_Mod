package aphorea.other.itemtype.weapons;

import java.awt.Color;
import aphorea.other.itemtype.weapons.AphoreaChargeAttackToolItem.AphoreaChargeAttackToolItem;
import aphorea.other.itemtype.weapons.AphoreaChargeAttackToolItem.AphoreaCustomChargeAttackHandler;
import aphorea.other.itemtype.weapons.AphoreaChargeAttackToolItem.AphoreaCustomChargeLevel;
import necesse.engine.localization.Localization;
import necesse.engine.network.PacketReader;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import necesse.inventory.item.ItemInteractAction;
import necesse.level.maps.Level;

abstract public class AphoreaBattleaxeToolItem extends AphoreaChargeAttackToolItem implements ItemInteractAction {
    public AphoreaCustomChargeLevel[] rushChargeLevels;
    boolean isCharging;

    public AphoreaBattleaxeToolItem(int enchantCost, AphoreaCustomChargeLevel[] chargeLevels, AphoreaCustomChargeLevel[] rushChargeLevels) {
        super(enchantCost, chargeLevels);
        this.rushChargeLevels = rushChargeLevels;
        if (rushChargeLevels.length == 0) {
            throw new IllegalArgumentException("Must have at least one charge level for battleaxes berserker rush");
        }
    }

    public static AphoreaCustomChargeLevel[] getChargeLevel(int time, Color color) {
        return new AphoreaCustomChargeLevel[]{new AphoreaCustomChargeLevel(time, 1.0F, color)};
    }

    @Override
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "battleaxe"));
        tooltips.add(Localization.translate("itemtooltip", "battleaxe2"));
        return tooltips;
    }

    @Override
    public InventoryItem onAttack(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int animAttack, int seed, PacketReader contentReader) {
        AphoreaCustomChargeLevel[] charge = player.buffManager.hasBuff("berserkerrushbuff") ? this.rushChargeLevels : this.chargeLevels;

        player.startAttackHandler(new BattleaxeAttackHandler(player, slot, item, this, seed, x, y, charge));

        return item;
    }

    @Override
    public String getTranslatedTypeName() {
        return Localization.translate("itemtype", "battleaxe");
    }

    @Override
    public int getLevelInteractAttackAnimTime(InventoryItem item, PlayerMob player) {
        return 1000;
    }

    @Override
    public boolean canLevelInteract(Level level, int x, int y, PlayerMob player, InventoryItem item) {
        return !player.isRiding() && !player.isAttacking && !this.isCharging && !player.buffManager.hasBuff("berserkerrushbuff") && !player.buffManager.hasBuff("berserkerrushcooldownbuff");
    }

    @Override
    public InventoryItem onLevelInteract(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int seed, PacketReader contentReader) {

        ActiveBuff buff = new ActiveBuff(BuffRegistry.getBuff("berserkerrushbuff"), player, 11.0F, null);
        player.addBuff(buff, true);

        return item;
    }

    public static class BattleaxeAttackHandler extends AphoreaCustomChargeAttackHandler<AphoreaBattleaxeToolItem> {

        public BattleaxeAttackHandler(PlayerMob player, PlayerInventorySlot slot, InventoryItem item, AphoreaBattleaxeToolItem toolItem, int seed, int startX, int startY, AphoreaCustomChargeLevel<AphoreaBattleaxeToolItem>[] chargeLevels) {
            super(player, slot, item, toolItem, seed, startX, startY, chargeLevels);
        }

        @Override
        public void drawWeaponParticles(InventoryItem showItem, Color color) {
        }

    }
}
