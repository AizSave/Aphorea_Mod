package aphorea.testing;

import aphorea.other.area.AphoreaArea;
import aphorea.other.area.AphoreaAreaList;
import aphorea.other.itemtype.weapons.AphoreaMagicAreaToolItem;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.ItemInteractAction;

import java.awt.*;

public class TestItem2 extends AphoreaMagicAreaToolItem implements ItemInteractAction {

    public TestItem2() {
        super(500, false, true,
            new AphoreaAreaList(
                new AphoreaArea(200, new Color(214, 0, 0)).setDamageArea(2, 10).setModParticles(0.5F, 0),
                new AphoreaArea(200, new Color(214, 0, 0)).setDamageArea(12, 30).setModParticles(0.5F, 2.5F)
            )
        );
        rarity = Rarity.UNCOMMON;
        attackAnimTime.setBaseValue(1000);

        manaCost.setBaseValue(4.0F);

        this.attackXOffset = 2;
        this.attackYOffset = 8;
    }

    @Override
    public void draw(InventoryItem item, PlayerMob perspective, int x, int y, boolean inInventory) {
        super.draw(item, perspective, x, y, inInventory);
    }
}
