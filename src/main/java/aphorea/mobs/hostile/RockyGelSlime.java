package aphorea.mobs.hostile;

import aphorea.registry.AphBuffs;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.*;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.ai.behaviourTree.trees.CollisionPlayerChaserWandererAI;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.hostile.HostileMob;
import necesse.entity.particle.FleshParticle;
import necesse.entity.particle.Particle;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.inventory.lootTable.lootItem.LootItem;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.HashSet;
import java.util.List;

public class RockyGelSlime extends HostileMob {

    public static GameDamage attack = new GameDamage(30);
    public static int attack_knockback = 50;

    @Override
    public boolean isValidSpawnLocation(Server server, ServerClient client, int targetX, int targetY) {
        return super.isValidSpawnLocation(server, client, targetX, targetY);
    }

    public static GameTexture texture;

    public static LootTable lootTable = new LootTable(
            LootItem.between("rockygel", 1, 4),
            ChanceLootItem.between(0.05f, "unstablecore", 1, 1)
    );

    public RockyGelSlime() {
        super(160);
        setSpeed(25);
        setFriction(3);

        collision = new Rectangle(-18, -12, 36, 20);
        hitBox = new Rectangle(-26, -16, 52, 28);
        selectBox = new Rectangle(-26, -27, 52, 39);
    }

    @Override
    public void init() {
        super.init();
        ai = new BehaviourTreeAI<>(this, new CollisionPlayerChaserWandererAI<>(null, 12 * 32, attack, attack_knockback, 40000));
    }

    @Override
    public LootTable getLootTable() {
        return lootTable;
    }

    @Override
    public void spawnDeathParticles(float knockbackX, float knockbackY) {
        for (int i = 0; i < 4; i++) {
            getLevel().entityManager.addParticle(new FleshParticle(
                    getLevel(), texture,
                    GameRandom.globalRandom.nextInt(5),
                    8,
                    32,
                    x, y, 20f,
                    knockbackX, knockbackY
            ), Particle.GType.IMPORTANT_COSMETIC);
        }
    }

    @Override
    protected void addDrawables(List<MobDrawable> list, OrderableDrawables tileList, OrderableDrawables topList, Level level, int x, int y, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        super.addDrawables(list, tileList, topList, level, x, y, tickManager, camera, perspective);
        GameLight light = level.getLightLevel(getTileX(), getTileY());
        int drawX = camera.getDrawX(x) - 32;
        int drawY = camera.getDrawY(y) - 51;

        Point sprite = getAnimSprite(x, y, getDir());

        drawY += getBobbing(x, y);
        drawY += getLevel().getTile(getTileX(), getTileY()).getMobSinkingAmount(this);

        DrawOptions drawOptions = texture.initDraw()
                .sprite(sprite.x, sprite.y, 64)
                .light(light)
                .pos(drawX, drawY);

        list.add(new MobDrawable() {
            @Override
            public void draw(TickManager tickManager) {
                drawOptions.draw();
            }
        });

        if (!this.isWaterWalking()) addShadowDrawables(tileList, x, y, light, camera);
    }

    @Override
    public int getRockSpeed() {
        return 20;
    }

    @Override
    public void collidedWith(Mob other) {
        super.collidedWith(other);
        ActiveBuff buff = new ActiveBuff(AphBuffs.STICKY, other, 1000, this);
        other.addBuff(buff, true);
    }

    @Override
    protected void onDeath(Attacker attacker, HashSet<Attacker> attackers) {
        super.onDeath(attacker, attackers);
    }

    @Override
    public void addBuff(ActiveBuff buff, boolean sendUpdatePacket) {
        if (buff.buff != AphBuffs.STICKY) super.addBuff(buff, sendUpdatePacket);
    }
}
