package aphorea.items.armor.Swamp;

import aphorea.other.AphoreaModifiers;
import aphorea.other.itemtype.armor.AphoreaSetHelmetArmorItem;
import necesse.engine.modifiers.ModifierValue;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.entity.mobs.Mob;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.armorItem.ArmorModifiers;

public class SwampHood extends AphoreaSetHelmetArmorItem {
    public SwampHood() {
        super(2, DamageTypeRegistry.MAGIC, 400, Rarity.UNCOMMON, "swamphood", "swampchestplate", "swampboots", "swamphoodsetbonusbuff");
        this.hairDrawOptions = HairDrawMode.NO_HAIR;
    }
    public ArmorModifiers getArmorModifiers(InventoryItem item, Mob mob) {
        return new ArmorModifiers(new ModifierValue<>(AphoreaModifiers.MAGIC_HEALING, 0.1F));
    }
}
