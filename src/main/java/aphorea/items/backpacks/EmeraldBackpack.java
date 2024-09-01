package aphorea.items.backpacks;

import aphorea.other.itemtype.AphoreaBackpack;

public class EmeraldBackpack extends AphoreaBackpack {
    public EmeraldBackpack() {
        this.rarity = Rarity.UNCOMMON;
    }

    public int getInternalInventorySize() {
        return 7;
    }
}
