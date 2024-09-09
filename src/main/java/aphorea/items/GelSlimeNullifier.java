package aphorea.items;

import aphorea.other.data.AphoreaWorldData;
import aphorea.other.vanillaitemtypes.AphoreaConsumableItem;
import necesse.engine.localization.Localization;
import necesse.engine.network.PacketReader;
import necesse.engine.network.packet.PacketChatMessage;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameUtils;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.level.maps.Level;

public class GelSlimeNullifier extends AphoreaConsumableItem {

    public static AphoreaWorldData worldData = new AphoreaWorldData();

    public GelSlimeNullifier() {
        super(1, false);
        this.rarity = Rarity.LEGENDARY;
    }

    @Override
    public String canPlace(Level level, int x, int y, PlayerMob player, InventoryItem item, PacketReader contentReader) {
        return null;
    }

    @Override
    public int getAttackAnimTime(InventoryItem item, Mob mob) {
        return 500;
    }

    @Override
    public int getItemCooldownTime(InventoryItem item, Mob mob) {
        return 10000;
    }

    @Override
    public InventoryItem onPlace(Level level, int x, int y, PlayerMob player, InventoryItem item, PacketReader contentReader) {

        if(level.isServer()) {
            AphoreaWorldData currentData = worldData.getData(level.getWorldEntity());
            boolean gelSlimesNulled = currentData.gelslimesnulled;

            if(gelSlimesNulled) {

                currentData.gelslimesnulled = false;

                PacketChatMessage mensaje = new PacketChatMessage(Localization.translate("message", "gelslimesunnulled"));
                GameUtils.streamServerClients(level).forEach((j) -> j.sendPacket(mensaje));

            } else {

                currentData.gelslimesnulled = true;

                PacketChatMessage mensaje = new PacketChatMessage(Localization.translate("message", "gelslimesnulled"));
                GameUtils.streamServerClients(level).forEach((j) -> j.sendPacket(mensaje));
            }
        }

        return super.onPlace(level, x, y, player, item, contentReader);
    }

    @Override
    public ListGameTooltips getBaseTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getBaseTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "gelslimenullifier"));
        return tooltips;
    }
}
