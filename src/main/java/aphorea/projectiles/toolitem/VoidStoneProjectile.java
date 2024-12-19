package aphorea.projectiles.toolitem;


import necesse.engine.network.server.ServerClient;
import necesse.engine.util.GameUtils;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.projectile.StoneProjectile;
import necesse.level.maps.LevelObjectHit;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class VoidStoneProjectile extends StoneProjectile {
    public Set<Mob> attackedMob = new HashSet<>();

    public VoidStoneProjectile() {
    }

    public VoidStoneProjectile(Mob owner, float x, float y, float targetX, float targetY, float speed, int distance, GameDamage damage, int knockback) {
        super(x, y, targetX, targetY, damage, owner);

        this.speed = speed;
        this.setDistance(distance);
        this.knockback = knockback;
    }

    @Override
    public void init() {
        super.init();

        this.canBounce = true;
        this.bouncing = 1;
        this.piercing = 2;
    }

    @Override
    public void onHit(Mob mob, LevelObjectHit object, float x, float y, boolean fromPacket, ServerClient packetSubmitter) {
        super.onHit(mob, object, x, y, fromPacket, packetSubmitter);
        if (!this.removed()) {
            attackedMob.add(mob);
            Mob nextTarget = getClosestNotAttackedMob();
            if (nextTarget != null) {
                this.setTarget(nextTarget.x, nextTarget.y);
                this.updateAngle();
            }
        }
    }

    public Mob getClosestNotAttackedMob() {
        return GameUtils.streamTargetsRange(this.getOwner(), this.distance * 3)
                .filter(mob -> !attackedMob.contains(mob) && mob.canBeTargeted(this.getOwner(), ((PlayerMob) this.getOwner()).getNetworkClient()))
                .min(Comparator.comparingDouble(mob -> mob.getDistance(this.x, this.y)))
                .orElse(null);
    }

}