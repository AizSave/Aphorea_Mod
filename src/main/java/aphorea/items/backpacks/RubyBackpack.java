package aphorea.items.backpacks;

import aphorea.other.itemtype.AphoreaBackpack;

public class RubyBackpack extends AphoreaBackpack {
    public RubyBackpack() {
        this.rarity = Rarity.UNCOMMON;
    }

    public int getInternalInventorySize() {
        return 6;
    }
}
