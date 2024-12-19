package aphorea.items.backpacks;

import aphorea.other.itemtype.AphBackpack;

public class SapphireBackpack extends AphBackpack {
    public SapphireBackpack() {
        this.rarity = Rarity.COMMON;
    }

    public int getInternalInventorySize() {
        return 6;
    }
}
