package aphorea.items.backpacks;

import aphorea.other.itemtype.AphBackpack;

public class DiamondBackpack extends AphBackpack {
    public DiamondBackpack() {
        this.rarity = Rarity.RARE;
    }

    public int getInternalInventorySize() {
        return 14;
    }
}
