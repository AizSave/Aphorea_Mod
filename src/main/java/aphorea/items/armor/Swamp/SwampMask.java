package aphorea.items.armor.Swamp;

import aphorea.other.AphoreaModifiers;
import aphorea.other.itemtype.armor.AphoreaSetHelmetArmorItem;
import necesse.engine.modifiers.ModifierValue;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.armorItem.ArmorItem;
import necesse.inventory.item.armorItem.ArmorModifiers;

public class SwampMask extends AphoreaSetHelmetArmorItem {
    public SwampMask() {
        super(4, DamageTypeRegistry.MELEE, 400, Rarity.UNCOMMON, "swampmask", "swampchestplate", "swampboots", "swampmasksetbonusbuff");
        this.hairDrawOptions = ArmorItem.HairDrawMode.OVER_HAIR;
        this.facialFeatureDrawOptions = ArmorItem.FacialFeatureDrawMode.OVER_FACIAL_FEATURE;
    }

    public ArmorModifiers getArmorModifiers(InventoryItem item, Mob mob) {
        return new ArmorModifiers(new ModifierValue<>(AphoreaModifiers.MAGIC_HEALING_RECEIVED, 0.05F), new ModifierValue<>(BuffModifiers.MAX_RESILIENCE_FLAT, 3));
    }
}
