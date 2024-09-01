package aphorea.items.armor.Witch;

import aphorea.other.itemtype.armor.AphoreaBootsArmorItem;
import necesse.engine.modifiers.ModifierValue;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.armorItem.ArmorModifiers;

public class MagicalBoots extends AphoreaBootsArmorItem {
    public MagicalBoots() {
        super(1, 250, Rarity.UNCOMMON, "magicalboots");
    }

    public ArmorModifiers getArmorModifiers(InventoryItem item, Mob mob) {
        return new ArmorModifiers(new ModifierValue<>(BuffModifiers.MAGIC_DAMAGE, 0.05F));
    }
}
