package aphorea.items.armor.Swamp;

import aphorea.other.AphoreaModifiers;
import aphorea.other.itemtype.armor.AphoreaChestArmorItem;
import necesse.engine.modifiers.ModifierValue;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.armorItem.ArmorModifiers;

public class SwampChestplate extends AphoreaChestArmorItem {
    public SwampChestplate() {
        super(5, 400, Rarity.UNCOMMON, "swampchestplate", "swampchestplatearms");
    }

    public ArmorModifiers getArmorModifiers(InventoryItem item, Mob mob) {
        return new ArmorModifiers(new ModifierValue<>(AphoreaModifiers.MAGIC_HEALING, 0.1F), new ModifierValue<>(BuffModifiers.MAX_RESILIENCE_FLAT, 2));
    }
}
