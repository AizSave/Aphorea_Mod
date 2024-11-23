package aphorea.other.utils;

import necesse.engine.network.packet.PacketForceOfWind;
import necesse.entity.mobs.Mob;
import necesse.level.maps.Level;

public class AphoreaCustomPushPacket extends PacketForceOfWind {
    public AphoreaCustomPushPacket(byte[] data) {
        super(data);
    }

    public AphoreaCustomPushPacket(int slot, float dirX, float dirY, float strength) {
        super(slot, dirX, dirY, strength);
    }

    public static void applyToPlayer(Level level, Mob mob, float dirX, float dirY, float strength) {
        float forceX = dirX * strength;
        float forceY = dirY * strength;
        if (Math.abs(mob.dx) < Math.abs(forceX)) {
            mob.dx = forceX;
        }

        if (Math.abs(mob.dy) < Math.abs(forceY)) {
            mob.dy = forceY;
        }

    }
}