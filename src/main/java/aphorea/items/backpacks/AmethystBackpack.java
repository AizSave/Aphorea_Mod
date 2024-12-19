package aphorea.items.backpacks;

import aphorea.other.itemtype.AphBackpack;

public class AmethystBackpack extends AphBackpack {
    public AmethystBackpack() {
        this.rarity = Rarity.COMMON;
    }

    public int getInternalInventorySize() {
        return 8;
    }
}
