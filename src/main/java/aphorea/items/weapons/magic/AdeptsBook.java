package aphorea.items.weapons.magic;

import aphorea.other.area.AphArea;
import aphorea.other.area.AphAreaList;
import aphorea.other.itemtype.AphAreaToolItem;
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
import necesse.inventory.InventoryItem;
import necesse.inventory.item.ItemInteractAction;
import necesse.level.maps.Level;

import java.awt.*;

public class AdeptsBook extends AphAreaToolItem implements ItemInteractAction {

    static AphAreaList areaList = new AphAreaList(
            new AphArea(250, new Color(58, 22, 100)).setDamageArea(30).setArmorPen(10)
    ).setDamageType(DamageTypeRegistry.MAGIC);

    public AdeptsBook() {
        super(500, true, false, areaList);
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

    @Override
    public Packet getPacket(int slot, float rangeModifier) {
        return new AdeptsBookAreaParticlesPacket(slot, rangeModifier);
    }

    @Override
    public void usePacket(Level level, PlayerMob player, float rangeModifier) {
        AdeptsBookAreaParticlesPacket.applyToPlayer(level, player, rangeModifier);
    }

    public static class AdeptsBookAreaParticlesPacket extends Packet {
        public final int slot;
        public final float rangeModifier;

        public AdeptsBookAreaParticlesPacket(byte[] data) {
            super(data);
            PacketReader reader = new PacketReader(this);
            this.slot = reader.getNextByteUnsigned();
            this.rangeModifier = reader.getNextFloat();
        }

        public AdeptsBookAreaParticlesPacket(int slot, float rangeModifier) {
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
}
