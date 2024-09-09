package aphorea.other.vanillaitemtypes;

import necesse.inventory.item.placeableItem.objectItem.ObjectItem;
import necesse.level.gameObject.GameObject;

public class AphoreaObjectItem extends ObjectItem {
    public AphoreaObjectItem(GameObject object, boolean dropAsMatDeathPenalty) {
        super(object, dropAsMatDeathPenalty);
    }
    public AphoreaObjectItem(GameObject object) {
        super(object);
    }
}
