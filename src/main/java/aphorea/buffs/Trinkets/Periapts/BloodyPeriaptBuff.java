package aphorea.buffs.Trinkets.Periapts;

import aphorea.other.buffs.trinkets.AphPeriaptActivableBuff;
import necesse.engine.localization.Localization;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.trinketItem.TrinketItem;

import java.awt.*;

public class BloodyPeriaptBuff extends AphPeriaptActivableBuff {

    public BloodyPeriaptBuff() {
        super("bloodyperiaptactive");
    }

    @Override
    public Color getColor() {
        return new Color(255, 0, 0);
    }

    public ListGameTooltips getTrinketTooltip(TrinketItem trinketItem, InventoryItem item, PlayerMob perspective) {
        ListGameTooltips tooltips = super.getTrinketTooltip(trinketItem, item, perspective);
        tooltips.add(Localization.translate("itemtooltip", "bloodyperiapt"));
        return tooltips;
    }

}