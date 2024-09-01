package aphorea.items.armor.Gold;

import aphorea.other.itemtype.armor.AphoreaSetHelmetArmorItem;
import necesse.engine.registries.DamageTypeRegistry;

public class GoldHat extends AphoreaSetHelmetArmorItem {
    public GoldHat() {
        super(1, DamageTypeRegistry.RANGED, 500, Rarity.COMMON, "goldhat", "goldchestplate", "goldboots", "goldhatsetbonusbuff");
    }
}
