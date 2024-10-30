package aphorea.other.itemtype.weapons;

import aphorea.other.*;
import aphorea.other.area.AphoreaArea;
import aphorea.other.area.AphoreaAreaList;
import aphorea.other.area.AphoreaAreaType;
import aphorea.other.magichealing.AphoreaMagicHealing;
import aphorea.other.itemtype.healing.AphoreaMagicHealingToolItem;
import necesse.engine.localization.Localization;
import necesse.engine.network.PacketReader;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.registries.EnchantmentRegistry;
import necesse.entity.mobs.*;
import necesse.gfx.drawOptions.itemAttack.ItemAttackDrawOptions;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import necesse.inventory.item.*;
import necesse.level.maps.Level;

import java.util.HashSet;
import java.util.Set;

public class AphoreaMagicAreaToolItem extends AphoreaMagicHealingToolItem {

    AphoreaAreaList areaList;
    public boolean isMagicWeapon;
    public boolean isHealingTool;

    public AphoreaMagicAreaToolItem(int enchantCost, boolean isMagicWeapon, boolean isHealingTool, AphoreaAreaList areaList) {
        super(enchantCost);

        this.isMagicWeapon = isMagicWeapon;
        this.isHealingTool = isHealingTool;

        this.areaList = areaList;
        damageType = DamageTypeRegistry.MAGIC;

        if(isMagicWeapon) {
            this.setItemCategory("equipment", "weapons", "magicweapons");
            this.setItemCategory(ItemCategory.equipmentManager, "weapons", "magicweapons");
        }
    }

    @Override
    public InventoryItem onAttack(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int animAttack, int seed, PacketReader contentReader) {
        this.consumeMana(player, item);

        float rangeModifier = 1 + this.getEnchantment(item).getModifier(AphoreaModifiers.TOOL_AREA_RANGE);

        if(player.isServer()) {
            areaList.executeAreas(this, this, attackDamage, x, y, player, item, seed, rangeModifier);
        }

        areaList.showAllAreaParticles(player, rangeModifier);

        return item;
    }

    @Override
    public GameDamage getAttackDamage(InventoryItem item) {
        return super.getAttackDamage(item);
    }

    public void setDrawAttackRotation(InventoryItem item, ItemAttackDrawOptions drawOptions, float attackDirX, float attackDirY, float attackProgress) {
        drawOptions.pointRotation(attackDirX, attackDirY);
    }

    public boolean animDrawBehindHand(InventoryItem item) {
        return true;
    }

    public void showAttack(Level level, int x, int y, AttackAnimMob mob, int attackHeight, InventoryItem item, int seed, PacketReader contentReader) {
    }

    public void addStatTooltips(ItemStatTipList list, InventoryItem currentItem, InventoryItem lastItem, Mob perspective, boolean forceAdd) {
        this.addAreasTip(list, currentItem, lastItem, perspective, forceAdd);
        this.addAttackSpeedTip(list, currentItem, lastItem, perspective);
        this.addResilienceGainTip(list, currentItem, lastItem, perspective, forceAdd);
        this.addCritChanceTip(list, currentItem, lastItem, perspective, forceAdd);
        this.addManaCostTip(list, currentItem, lastItem, perspective);
    }

    public void addAreasTip(ItemStatTipList list, InventoryItem currentItem, InventoryItem lastItem, Attacker attacker, boolean forceAdd) {
        boolean multipleAreas = areaList.areas.length > 1;
        if(multipleAreas) {
            StringItemStatTip lineTip = new LocalMessageStringItemStatTip("itemtooltip", "line", "none", "none");
            list.add(100, lineTip);
        }

        for (int i = 0; i < areaList.areas.length; i++) {
            AphoreaArea area = areaList.areas[i];

            if(multipleAreas) {
                StringItemStatTip areasTip = new LocalMessageStringItemStatTip("itemtooltip", "areatip", "number", String.valueOf(i + 1));
                list.add(100, areasTip);
            }

            if(area.areaTypes.contains(AphoreaAreaType.DAMAGE)) {
                attackDamage.setBaseValue(area.getBaseDamage())
                        .setUpgradedValue(1, area.getTier1Damage());

                int damage = this.getAttackDamageValue(currentItem, attacker);
                int lastDamage = lastItem == null ? -1 : this.getAttackDamageValue(lastItem, attacker);
                if (damage > 0 || lastDamage > 0 || forceAdd) {
                    DoubleItemStatTip tip = this.getDamageType(currentItem).getDamageTip(damage);

                    if (lastItem != null) {
                        tip.setCompareValue(lastDamage);
                    }

                    list.add(100, tip);
                }
            }

            if(area.areaTypes.contains(AphoreaAreaType.HEALING)) {

                int healing = AphoreaMagicHealing.getMagicHealing((Mob) attacker, null, area.getHealing(currentItem), this, currentItem);
                DoubleItemStatTip tip = new LocalMessageDoubleItemStatTip("itemtooltip", "magichealingtip", "health", healing, 0);

                if (lastItem != null) {
                    int lastHealing = AphoreaMagicHealing.getMagicHealing((Mob) attacker, null, area.getHealing(lastItem), this, lastItem);
                    tip.setCompareValue(lastHealing);
                }

                list.add(100, tip);

            }

            DoubleItemStatTip rangeTip = new LocalMessageDoubleItemStatTip("itemtooltip", "rangetip", "range", area.currentRange, 0);
            list.add(100, rangeTip);

            if(multipleAreas) {
                StringItemStatTip lineTip = new LocalMessageStringItemStatTip("itemtooltip", "line", "none", "none");
                list.add(100, lineTip);
            }
        }
    }

    @Override
    public Set<Integer> getValidEnchantmentIDs(InventoryItem item) {
        Set<Integer> enchantments = new HashSet<>();
        if(isMagicWeapon) {
            enchantments.addAll(EnchantmentRegistry.magicItemEnchantments);
        }
        if(isHealingTool) {
            enchantments.addAll(AphoreaEnchantments.healingItemEnchantments);
        }
        enchantments.addAll(AphoreaEnchantments.areaItemEnchantments);

        return enchantments;
    }

    public String getTranslatedTypeName() {
        if(isMagicWeapon) {
            return Localization.translate("item", "magicweapon");
        } else {
            return super.getTranslatedTypeName();
        }
    }
}
