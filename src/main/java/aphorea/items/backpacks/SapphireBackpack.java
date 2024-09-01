package aphorea.items.backpacks;

import aphorea.other.itemtype.AphoreaBackpack;

public class SapphireBackpack extends AphoreaBackpack {
    public SapphireBackpack() {
        this.rarity = Rarity.COMMON;
    }

    public int getInternalInventorySize() {
        return 4;
    }
}
