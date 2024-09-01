package aphorea.items.backpacks;

import aphorea.other.itemtype.AphoreaBackpack;

public class BasicBackpack extends AphoreaBackpack {
    public BasicBackpack() {
        this.rarity = Rarity.COMMON;
    }

    public int getInternalInventorySize() {
        return 3;
    }
}
