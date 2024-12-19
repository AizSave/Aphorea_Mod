package aphorea.items.backpacks;

import aphorea.other.itemtype.AphBackpack;

public class BasicBackpack extends AphBackpack {
    public BasicBackpack() {
        this.rarity = Rarity.COMMON;
    }

    public int getInternalInventorySize() {
        return 4;
    }
}
