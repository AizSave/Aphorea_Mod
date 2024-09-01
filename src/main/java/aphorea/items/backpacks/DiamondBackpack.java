package aphorea.items.backpacks;

import aphorea.other.itemtype.AphoreaBackpack;

public class DiamondBackpack extends AphoreaBackpack {
    public DiamondBackpack() {
        this.rarity = Rarity.RARE;
    }

    public int getInternalInventorySize() {
        return 8;
    }
}
