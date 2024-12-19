package aphorea.items.backpacks;

import aphorea.other.itemtype.AphBackpack;

public class RubyBackpack extends AphBackpack {
    public RubyBackpack() {
        this.rarity = Rarity.UNCOMMON;
    }

    public int getInternalInventorySize() {
        return 10;
    }
}
