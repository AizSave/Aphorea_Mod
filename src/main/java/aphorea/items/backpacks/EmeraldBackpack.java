package aphorea.items.backpacks;

import aphorea.other.itemtype.AphBackpack;

public class EmeraldBackpack extends AphBackpack {
    public EmeraldBackpack() {
        this.rarity = Rarity.UNCOMMON;
    }

    public int getInternalInventorySize() {
        return 12;
    }
}
