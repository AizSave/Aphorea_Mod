package aphorea.mobs.summon;

import necesse.engine.registries.MobRegistry.Textures;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.MobDrawable;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.ai.behaviourTree.trees.PlayerFollowerCollisionChaserAI;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.summon.summonFollowingMob.attackingFollowingMob.AttackingFollowingMob;
import necesse.entity.mobs.summon.summonFollowingMob.attackingFollowingMob.BabySkeletonMob;
import necesse.entity.particle.FleshParticle;
import necesse.entity.particle.Particle;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
