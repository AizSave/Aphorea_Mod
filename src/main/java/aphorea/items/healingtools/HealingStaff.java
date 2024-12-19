package aphorea.items.healingtools;

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
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.entity.mobs.AttackAnimMob;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.GameResources;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.ItemInteractAction;
import necesse.level.maps.Level;

import java.awt.*;

public class HealingStaff extends AphAreaToolItem implements ItemInteractAction {

    static AphAreaList areaList = new AphAreaList(
            new AphArea(120, new Color(191, 0, 255)).setHealingArea(6, 8),
            new AphArea(120, new Color(255, 0, 191)).setHealingArea(2, 3)
    );

    public HealingStaff() {
        super(500, false, true, areaList);
        rarity = Rarity.UNCOMMON;
        attackAnimTime.setBaseValue(1400);

        manaCost.setBaseValue(6.0F);

        attackXOffset = 12;
        attackYOffset = 22;

        attackDamage.setBaseValue(1)
                .setUpgradedValue(1, 2);
    }

    @Override
    public Packet getPacket(int slot, float rangeModifier) {
        return new HealingStaffAreaParticlesPacket(slot, rangeModifier);
    }

    @Override
    public void usePacket(Level level, PlayerMob player, float rangeModifier) {
        HealingStaffAreaParticlesPacket.applyToPlayer(level, player, rangeModifier);
    }

    public static class HealingStaffAreaParticlesPacket extends Packet {
        public final int slot;
        public final float rangeModifier;

        public HealingStaffAreaParticlesPacket(byte[] data) {
            super(data);
            PacketReader reader = new PacketReader(this);
            this.slot = reader.getNextByteUnsigned();
            this.rangeModifier = reader.getNextFloat();
        }

        public HealingStaffAreaParticlesPacket(int slot, float rangeModifier) {
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

    @Override
    public void showAttack(Level level, int x, int y, AttackAnimMob mob, int attackHeight, InventoryItem item, int seed, PacketReader contentReader) {
        super.showAttack(level, x, y, mob, attackHeight, item, seed, contentReader);
        SoundManager.playSound(GameResources.magicbolt3, SoundEffect.effect(mob).volume(1.0F).pitch(1.0F));
    }
}