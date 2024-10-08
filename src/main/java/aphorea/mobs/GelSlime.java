package aphorea.mobs;

import aphorea.other.data.AphoreaWorldData;
import aphorea.other.mobclass.DayHostileMob;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.*;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.particle.FleshParticle;
import necesse.entity.particle.Particle;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.List;

public class GelSlime extends DayHostileMob {

    public static GameDamage attack = new GameDamage(25);
    public static int attack_knockback = 50;

    public static GameTexture texture;

    public static LootTable lootTable = new LootTable(
        ChanceLootItem.between(0.8f, "gelball", 1, 2),
        ChanceLootItem.between(0.05f, "unstablecore", 1, 1),
        ChanceLootItem.between(0.02f, "gelring", 1, 1)
    );

    public static AphoreaWorldData worldData = new AphoreaWorldData();

    public boolean isValidSpawnLocation(Server server, ServerClient client, int targetX, int targetY) {
        AphoreaWorldData currentData = worldData.getData(client.getLevel().getWorldEntity());
        if(currentData.gelslimesnulled) {
            return false;
        } else {
            return super.isValidSpawnLocation(server, client, targetX, targetY);
        }
    }

    public GelSlime() {
        super(60);
        setSpeed(30);
        setFriction(2);

        collision = new Rectangle(-10, -7, 20, 14);
        hitBox = new Rectangle(-14, -12, 28, 24);
        selectBox = new Rectangle(-14, -7 - 14, 28, 28);
    }

    public void init() {
        super.init(attack, attack_knockback);
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

        if(!this.isWaterWalking()) addShadowDrawables(tileList, x, y, light, camera);
    }

    @Override
    public int getRockSpeed() {
        return 20;
    }

    @Override
    public void collidedWith(Mob other) {
        super.collidedWith(other);
        ActiveBuff buff = new ActiveBuff(BuffRegistry.getBuff("stickybuff"), other, 1000, this);
        other.addBuff(buff, true);
    }

    @Override
    public void serverTick() {
        super.serverTick();
    }

    @Override
    public void addBuff(ActiveBuff buff, boolean sendUpdatePacket) {
        if(!buff.buff.getStringID().equals("stickybuff")) super.addBuff(buff, sendUpdatePacket);
    }
}
