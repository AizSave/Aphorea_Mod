package aphorea.items.backpacks;

import aphorea.other.itemtype.AphoreaBackpack;

public class AmethystBackpack extends AphoreaBackpack {
    public AmethystBackpack() {
        this.rarity = Rarity.COMMON;
    }

    public int getInternalInventorySize() {
        return 5;
    }
}
