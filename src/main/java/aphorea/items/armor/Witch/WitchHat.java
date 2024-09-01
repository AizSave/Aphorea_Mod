package aphorea.items.armor.Witch;

import aphorea.other.AphoreaModifiers;
import aphorea.other.itemtype.armor.AphoreaSetHelmetArmorItem;
import necesse.engine.modifiers.ModifierValue;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.armorItem.ArmorItem;
import necesse.inventory.item.armorItem.ArmorModifiers;

public class WitchHat extends AphoreaSetHelmetArmorItem {
    public WitchHat() {
        super(1, DamageTypeRegistry.MAGIC, 200, Rarity.RARE, "witchhat", "magicalsuit", "magicalboots", "witchsetbonusbuff");
        this.hairDrawOptions = ArmorItem.HairDrawMode.OVER_HAIR;
        this.facialFeatureDrawOptions = ArmorItem.FacialFeatureDrawMode.OVER_FACIAL_FEATURE;
    }

    public ArmorModifiers getArmorModifiers(InventoryItem item, Mob mob) {
        return new ArmorModifiers(new ModifierValue<>(AphoreaModifiers.MAGIC_HEALING, 0.2F), new ModifierValue<>(BuffModifiers.MAX_MANA_FLAT, 50));
    }

}
