package aphorea.mobs.bosses;

import necesse.engine.eventStatusBars.EventStatusBarManager;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.packet.PacketChatMessage;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.registries.MobRegistry;
import necesse.engine.registries.MusicRegistry;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.util.GameRandom;
import necesse.engine.util.GameUtils;
import necesse.engine.util.gameAreaSearch.GameAreaStream;
import necesse.entity.mobs.*;
import necesse.entity.mobs.ai.behaviourTree.AINodeResult;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.ai.behaviourTree.Blackboard;
import necesse.entity.mobs.ai.behaviourTree.composites.SelectorAINode;
import necesse.entity.mobs.ai.behaviourTree.leaves.WandererAINode;
import necesse.entity.mobs.ai.behaviourTree.trees.CollisionChaserAI;
import necesse.entity.mobs.ai.behaviourTree.util.FlyingAIMover;
import necesse.entity.mobs.ai.behaviourTree.util.TargetFinderDistance;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.BossNearbyBuff;
import necesse.entity.mobs.hostile.bosses.FlyingBossMob;
import necesse.entity.particle.FleshParticle;
import necesse.entity.particle.Particle;
import necesse.gfx.GameResources;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTooltips.GameTooltips;
import necesse.gfx.gameTooltips.StringTooltips;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.inventory.lootTable.lootItem.LootItem;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;


import java.awt.*;
import java.util.*;
import java.util.List;

public class UnstableGelSlime extends FlyingBossMob {

    int anger;
    int count;
    int escape;

    public static GameDamage attack = new GameDamage(40);
    public static int attack_knockback = 500;

    public static int baseSpeed = 40;
    public static int speedPerAnger = 4;

    public static GameTexture icon;
    public static GameTexture texture;
    public static MaxHealthGetter MAX_HEALTH = new MaxHealthGetter(1000, 1250, 1500, 1750, 2000);

    public static LootTable lootTable = new LootTable(
            ChanceLootItem.between(1f, "unstablegel", 21, 28),
            new LootItem("unstableperiapt"),
            new LootItem("gelslimenullifier")
    );



    protected MobHealthScaling scaling = new MobHealthScaling(this);


    public UnstableGelSlime() {
        super(1200);
        this.difficultyChanges.setMaxHealth(MAX_HEALTH);

        this.setArmor(10);
        this.setSpeed(baseSpeed);
        this.setFriction(3);
        this.setKnockbackModifier(0.0F);

        staySmoothSnapped = true;

        this.collision = new Rectangle(-30, -10, 60, 50);
        this.hitBox = new Rectangle(-40, -20, 80, 70);
        this.selectBox = new Rectangle(-50, -45, 100, 90);
    }


    @Override
    public void init() {
        super.init();

        SoundManager.playSound(GameResources.roar, SoundEffect.effect(this)
                .volume(0.7f)
                .pitch(GameRandom.globalRandom.getFloatBetween(1.0f, 1.1f)));

        this.anger = 0;
        this.count = 0;
        this.escape = 0;

        ai = new BehaviourTreeAI<>(this, new UnstableGelSlimeAI<>(1024 * 32, attack, attack_knockback), new FlyingAIMover());
    }


    @Override
    public LootTable getLootTable() { return lootTable; }

    @Override
    public void spawnDeathParticles(float knockbackX, float knockbackY) {
        for (int i = 0; i < 4; i++) {
            getLevel().entityManager.addParticle(new FleshParticle(
                    getLevel(), texture == null ? GameTexture.fromFile("mobs/unstablegelslime") : texture,
                    GameRandom.globalRandom.nextInt(5),
                    8,
                    96,
                    x, y, 20f,
                    knockbackX, knockbackY
            ), Particle.GType.IMPORTANT_COSMETIC);
        }
    }

    @Override
    protected void addDrawables(List<MobDrawable> list, OrderableDrawables tileList, OrderableDrawables topList, Level level, int x, int y, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        super.addDrawables(list, tileList, topList, level, x, y, tickManager, camera, perspective);
        GameLight light = level.getLightLevel(getTileX(), getTileY());
        int drawX = camera.getDrawX(x) - 96;
        int drawY = camera.getDrawY(y) - 123;

        Point sprite = getAnimSprite(x, y, getDir());

        drawY += getBobbing(x, y);
        drawY += getLevel().getTile(getTileX(), getTileY()).getMobSinkingAmount(this);

        DrawOptions drawOptions = texture.initDraw()
                .sprite(sprite.x, sprite.y, 192)
                .light(light)
                .pos(drawX, drawY);

        list.add(new MobDrawable() {
            @Override
            public void draw(TickManager tickManager) {
                drawOptions.draw();
            }
        });
    }


    @Override
    public int getRockSpeed() {
        return 20;
    }

    public int getMaxHealth() {
        return super.getMaxHealth() + (int) ((float) (this.scaling == null ? 0 : this.scaling.getHealthIncrease()) * this.getMaxHealthModifier());
    }

    public void setupHealthPacket(PacketWriter writer, boolean isFull) {
        this.scaling.setupHealthPacket(writer, isFull);
        super.setupHealthPacket(writer, isFull);
    }

    public void applyHealthPacket(PacketReader reader, boolean isFull) {
        this.scaling.applyHealthPacket(reader, isFull);
        super.applyHealthPacket(reader, isFull);
    }

    public void setMaxHealth(int maxHealth) {
        super.setMaxHealth(maxHealth);
        if (this.scaling != null) {
            this.scaling.updatedMaxHealth();
        }

    }

    public boolean shouldDrawOnMap() {
        return true;
    }

    @Override
    public void drawOnMap(TickManager tickManager, Client client, int x, int y) {
        super.drawOnMap(tickManager, client, x, y);
        int drawX = x - 16;
        int drawY = y - 16;
        icon.initDraw().sprite(0, 0, 64).size(32, 32).draw(drawX, drawY);

    }

    @Override
    public boolean isHealthBarVisible() {
        return super.isHealthBarVisible();
    }

    public Rectangle drawOnMapBox() {
        return new Rectangle(-16, -16, 32, 32);
    }

    public GameTooltips getMapTooltips() {
        return new StringTooltips(this.getDisplayName() + " " + this.getHealth() + "/" + this.getMaxHealth());
    }

    protected void onDeath(Attacker attacker, HashSet<Attacker> attackers) {
        super.onDeath(attacker, attackers);

        attackers.stream().map(Attacker::getAttackOwner).filter((m) -> m != null && m.isPlayer).distinct().forEach((m) -> this.getLevel().getServer().network.sendPacket(new PacketChatMessage(new LocalMessage("misc", "bossdefeat", "name", this.getLocalization())), ((PlayerMob) m).getServerClient()));

        for (int i = 0; i < 4; i++) {
            Mob invocar = MobRegistry.getMob("unstablegelslime_mini", this.getLevel());

            this.getLevel().entityManager.addMob(invocar, randomPositionClose(this.x), randomPositionClose(this.y));
        }
    }

    public void clientTick() {
        super.clientTick();
        SoundManager.setMusic(MusicRegistry.TheFirstTrial, SoundManager.MusicPriority.EVENT, 1.5F);
        EventStatusBarManager.registerMobHealthStatusBar(this);
        BossNearbyBuff.applyAround(this);
    }

    public void serverTick() {
        super.serverTick();
        this.scaling.serverTick();
        BossNearbyBuff.applyAround(this);
    }

    public float randomPositionClose(float n) {
        return n + (float) Math.floor(Math.random() * 11) - 5;
    }

    @Override
    public void handleCollisionHit(Mob target, GameDamage damage, int knockback) {
        super.handleCollisionHit(target, damage, knockback);
        ActiveBuff buff = new ActiveBuff(BuffRegistry.getBuff("stickybuff"), target, 1000, this);
        target.addBuff(buff, true);
    }

    @Override
    public void addBuff(ActiveBuff buff, boolean sendUpdatePacket) {
        if(!buff.buff.getStringID().equals("stickybuff")) super.addBuff(buff, sendUpdatePacket);
    }

    public static class UnstableGelSlimeAI<T extends UnstableGelSlime> extends SelectorAINode<T> {
        public final UnstableGelSlimeChasePlayerAI<T> collisionPlayerChaserAI;
        public final WandererAINode<T> wandererAINode;

        public UnstableGelSlimeAI(int searchDistance, GameDamage damage, int knockback) {
            this.addChild(this.collisionPlayerChaserAI = new UnstableGelSlimeChasePlayerAI<>(searchDistance, damage, knockback));
            this.addChild(this.wandererAINode = new WandererAINode<>(100));
        }
    }

    public static class UnstableGelSlimeChasePlayerAI<T extends UnstableGelSlime> extends CollisionChaserAI<T> {
        public UnstableGelSlimeChasePlayerAI(int searchDistance, GameDamage damage, int knockback) {
            super(searchDistance, damage, knockback);
        }

        public GameAreaStream<Mob> streamPossibleTargets(T mob, Point base, TargetFinderDistance<T> distance) {
            return distance.streamPlayersInRange(base, mob).filter((m) -> m != null && !m.removed() && (m.isHuman && m.getTeam() != -1 || m.isPlayer)).map((m) -> m);
        }

        public Mob getRandomTarget(T mob, Point base, TargetFinderDistance<T> distance) {
            ArrayList<Mob> list = new ArrayList<>();
            streamPossibleTargets(mob, base, distance).forEach(list::add);
            return GameRandom.globalRandom.getOneOf(list);
        }

        @Override
        public AINodeResult tick(T mob, Blackboard<T> blackboard) {
            if(mob.getLevel().getWorldEntity().isNight()) {
                mob.remove();
                PacketChatMessage mensaje = new PacketChatMessage(Localization.translate("message", "unstablegelslime_night"));
                GameUtils.streamServerClients(mob.getLevel()).forEach((j) -> j.sendPacket(mensaje));

                return AINodeResult.FAILURE;
            } else {
                AINodeResult result = super.tick(mob, blackboard);
                if (result == AINodeResult.FAILURE || mob.getLevel().getWorldEntity().isNight()) {
                    if (mob.escape >= 100) {
                        mob.remove();
                        return AINodeResult.FAILURE;
                    } else {
                        mob.escape++;
                    }
                } else {
                    mob.escape = 0;
                }

                if (mob.anger == 10) {
                    mob.count++;
                    if (mob.count == 60) {
                        mob.count = (int) Math.floor(Math.random() * 41F);

                        SoundManager.playSound(GameResources.roar, SoundEffect.effect(mob)
                                .volume(0.7f)
                                .pitch(GameRandom.globalRandom.getFloatBetween(1.0f, 1.1f)));

                        mob.count = 0;

                        executeTeleport(mob);
                    }
                } else {
                    float healthPercent = mob.getHealthPercent();
                    if (healthPercent < 1 - (0.09 * (1 + mob.anger))) {
                        float actualExtraSpeed = mob.buffManager.getModifier(BuffModifiers.SPEED_FLAT);
                        mob.buffManager.applyModifiers(BuffModifiers.SPEED_FLAT, actualExtraSpeed + speedPerAnger);

                        mob.anger++;

                        SoundManager.playSound(GameResources.roar, SoundEffect.effect(mob)
                                .volume(0.7f)
                                .pitch(GameRandom.globalRandom.getFloatBetween(1.0f, 1.1f)));
                        PacketChatMessage mensaje = new PacketChatMessage(Localization.translate("message", mob.anger == 10 ? "unstablegelslime_enfadado" : "unstablegelslime_enfadando", "anger", mob.anger));
                        GameUtils.streamServerClients(mob.getLevel()).forEach((j) -> j.sendPacket(mensaje));

                        int n = 0;
                        if (mob.anger >= 8) {
                            n = 3;
                        } else if (mob.anger >= 5) {
                            n = 2;
                        } else if (mob.anger >= 2) {
                            n = 1;
                        }

                        for (int i = 0; i < n; i++) {
                            UnstableGelSlime_Mini summoned = (UnstableGelSlime_Mini) MobRegistry.getMob("unstablegelslime_mini", mob.getLevel());
                            summoned.setTP(true);
                            mob.getLevel().entityManager.addMob(summoned, mob.randomPositionClose(mob.x), mob.randomPositionClose(mob.y));
                        }

                        executeTeleport(mob);
                    }
                }

                return result;
            }
        }

        public void executeTeleport(T mob) {
            if(!mob.removed()) {
                Mob tpTarget = getRandomTarget(mob, new Point((int) mob.x, (int) mob.y), new TargetFinderDistance<>(1024 * 32));
                if(tpTarget != null) {
                    float distance = 150 + 150 * mob.getHealthPercent();
                    float angle = (float) Math.random() * 360;
                    float xExtra = (float) (Math.cos(angle) * distance);
                    float yExtra = (float) (Math.sin(angle) * distance);

                    mob.setPos(tpTarget.x + xExtra, tpTarget.y + yExtra, true);
                }
            }
        }


    }
}


