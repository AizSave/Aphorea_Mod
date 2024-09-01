package aphorea.items.armor.Rocky;

import aphorea.other.itemtype.armor.AphoreaChestArmorItem;
import necesse.engine.modifiers.ModifierValue;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.armorItem.ArmorModifiers;

public class RockyChestplate extends AphoreaChestArmorItem {
    public RockyChestplate() {
        super(9, 250, Rarity.COMMON, "rockychest", "rockyarms");
    }

    public ArmorModifiers getArmorModifiers(InventoryItem item, Mob mob) {
        return new ArmorModifiers(new ModifierValue<>(BuffModifiers.SPEED, -0.09F));
     }
}
