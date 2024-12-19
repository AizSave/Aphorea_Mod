package aphorea.buffs.Trinkets.Healing;

import aphorea.other.area.AphArea;
import aphorea.other.area.AphAreaList;
import aphorea.other.buffs.trinkets.AphDamageWhenHealTrinketBuff;
import necesse.engine.localization.Localization;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.client.ClientClient;
import necesse.engine.network.packet.PacketRequestPlayerData;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.trinketItem.TrinketItem;
import necesse.level.maps.Level;

import java.awt.*;

public class WitchMedallionBuff extends AphDamageWhenHealTrinketBuff {
    public static AphAreaList areaList = new AphAreaList(
            new AphArea(200, new Color(104, 0, 204)).setDamageArea(15).setArmorPen(5)
    ).setDamageType(DamageTypeRegistry.MAGIC);

    public WitchMedallionBuff() {
        super(20, areaList);
    }

    @Override
    public Packet getPacket(int slot, float rangeModifier) {
        return new WitchMedallionAreaParticlesPacket(slot, rangeModifier);
    }

    public static class WitchMedallionAreaParticlesPacket extends Packet {
        public final int slot;
        public final float rangeModifier;

        public WitchMedallionAreaParticlesPacket(byte[] data) {
            super(data);
            PacketReader reader = new PacketReader(this);
            this.slot = reader.getNextByteUnsigned();
            this.rangeModifier = reader.getNextFloat();
        }

        public WitchMedallionAreaParticlesPacket(int slot, float rangeModifier) {
            this.slot = slot;
            this.rangeModifier = rangeModifier;
            PacketWriter writer = new PacketWriter(this);
            writer.putNextByteUnsigned(slot);
            writer.putNextFloat(rangeModifier);
        }

        public void processClient(NetworkPacket packet, Client client) {
            if (client.getLevel() != null) {
                ClientClient target = client.getClient(this.slot);
                if (target != null && target.isSamePlace(client.getLevel())) {
                    applyToPlayer(target.playerMob.getLevel(), target.playerMob, rangeModifier);
                } else {
                    client.network.sendPacket(new PacketRequestPlayerData(this.slot));
                }

            }
        }

        public static void applyToPlayer(Level level, Mob mob, float rangeModifier) {
            if (level != null && level.isClient()) {
                areaList.showAllAreaParticles(mob, rangeModifier);
            }

        }
    }

    public ListGameTooltips getTrinketTooltip(TrinketItem trinketItem, InventoryItem item, PlayerMob perspective) {
        ListGameTooltips tooltips = super.getTrinketTooltip(trinketItem, item, perspective);
        tooltips.add(Localization.translate("itemtooltip", "witchmedallion"));
        return tooltips;
    }

}