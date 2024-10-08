package aphorea.mobs.summon;

import necesse.entity.mobs.summon.summonFollowingMob.attackingFollowingMob.BabySkeletonMob;
import necesse.gfx.gameTexture.GameTexture;

public class UndeadSkeleton extends BabySkeletonMob {

    public int count;

    public static GameTexture texture;

    public UndeadSkeleton() {
        super();
    }

    public void init() {
        super.init();
        count = 0;
    }

    public void serverTick() {
        super.serverTick();
        count++;

        if(count >= 200) {
            this.remove();
        }
    }

}
