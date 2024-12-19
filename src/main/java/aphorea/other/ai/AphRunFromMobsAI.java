package aphorea.other.ai;

import necesse.engine.util.GameMath;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.AINode;
import necesse.entity.mobs.ai.behaviourTree.AINodeResult;
import necesse.entity.mobs.ai.behaviourTree.Blackboard;
import necesse.entity.mobs.ai.behaviourTree.decorators.MoveTaskAINode;
import necesse.entity.mobs.ai.behaviourTree.event.AIWasHitEvent;
import necesse.entity.mobs.ai.behaviourTree.leaves.WandererAINode;
import necesse.level.maps.levelData.settlementData.ZoneTester;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Objects;
import java.util.function.Predicate;

public class AphRunFromMobsAI<T extends Mob> extends MoveTaskAINode<T> {
    public int runDistance;
    public Predicate<Mob> runFromMob;
    public boolean includeMobs = true;
    public boolean includePlayers = true;
    public long nextRunCooldown;

    public boolean isRunning;

    public AphRunFromMobsAI() {
        this.runDistance = 150;
        this.runFromMob = mob -> true;
    }

    public AphRunFromMobsAI(String runFromMobID) {
        this.runDistance = 150;
        this.runFromMob = mob -> Objects.equals(mob.getStringID(), runFromMobID);
    }

    public AphRunFromMobsAI(Predicate<Mob> runFromMob) {
        this.runDistance = 150;
        this.runFromMob = runFromMob;
    }

    public AphRunFromMobsAI(int runDistance) {
        this.runDistance = runDistance;
        this.runFromMob = mob -> true;
    }

    public AphRunFromMobsAI(int runDistance, String runFromMobID) {
        this.runDistance = runDistance;
        this.runFromMob = mob -> Objects.equals(mob.getStringID(), runFromMobID);
    }

    public AphRunFromMobsAI(int runDistance, Predicate<Mob> runFromMob) {
        this.runDistance = runDistance;
        this.runFromMob = runFromMob;
    }

    public AphRunFromMobsAI(boolean includeMobs, boolean includePlayers) {
        this.runDistance = 150;
        this.runFromMob = mob -> true;
        this.includeMobs = includeMobs;
        this.includePlayers = includePlayers;
    }

    public AphRunFromMobsAI(String runFromMobID, boolean includeMobs, boolean includePlayers) {
        this.runDistance = 150;
        this.runFromMob = mob -> Objects.equals(mob.getStringID(), runFromMobID);
        this.includeMobs = includeMobs;
        this.includePlayers = includePlayers;
    }

    public AphRunFromMobsAI(Predicate<Mob> runFromMob, boolean includeMobs, boolean includePlayers) {
        this.runDistance = 150;
        this.runFromMob = runFromMob;
        this.includeMobs = includeMobs;
        this.includePlayers = includePlayers;
    }

    public AphRunFromMobsAI(int runDistance, boolean includeMobs, boolean includePlayers) {
        this.runDistance = runDistance;
        this.runFromMob = mob -> true;
        this.includeMobs = includeMobs;
        this.includePlayers = includePlayers;
    }

    public AphRunFromMobsAI(int runDistance, String runFromMobID, boolean includeMobs, boolean includePlayers) {
        this.runDistance = runDistance;
        this.runFromMob = mob -> Objects.equals(mob.getStringID(), runFromMobID);
        this.includeMobs = includeMobs;
        this.includePlayers = includePlayers;
    }

    public AphRunFromMobsAI(int runDistance, Predicate<Mob> runFromMob, boolean includeMobs, boolean includePlayers) {
        this.runDistance = runDistance;
        this.runFromMob = runFromMob;
        this.includeMobs = includeMobs;
        this.includePlayers = includePlayers;
    }


    protected void onRootSet(AINode<T> root, T mob, Blackboard<T> blackboard) {
    }

    public void init(T mob, Blackboard<T> blackboard) {
    }

    public AINodeResult tickNode(T mob, Blackboard<T> blackboard) {
        if (isRunning && !mob.getLevel().isCave && !(new Rectangle(96, 96, (mob.getLevel().width - 6) * 32, (mob.getLevel().height - 6) * 32)).contains(mob.getCollision())) {
            mob.remove();
            return AINodeResult.SUCCESS;
        } else {
            if (isRunning && !blackboard.mover.isMoving()) {
                setRunning(mob, false);
            }

            for (AIWasHitEvent e : blackboard.getLastHits()) {
                Mob attackOwner = e.event.attacker != null ? e.event.attacker.getAttackOwner() : null;
                AINodeResult result;
                if (attackOwner != null) {
                    result = this.startRun(mob, mob.getTileX(), mob.getTileY(), attackOwner);
                } else {
                    result = this.startRun(mob, mob.getTileX(), mob.getTileY(), e.event.knockbackX, e.event.knockbackY, (ZoneTester) null);
                }
                if (result != null) {
                    return result;
                }
            }

            if (this.nextRunCooldown < mob.getWorldEntity().getTime()) {
                TempDistance closest;
                if (includeMobs == includePlayers) {
                    closest = mob.getLevel().entityManager.streamAreaMobsAndPlayers(mob.getTileX(), mob.getTileY(), this.runDistance / 32 + 2)
                            .filter(runFromMob).map((p) -> new TempDistance(p, mob))
                            .findBestDistance(0, (p1, p2) -> Float.compare(p1.distance, p2.distance)).orElse(null);

                } else if (includeMobs) {
                    closest = mob.getLevel().entityManager.mobs.getInRegionByTileRange(mob.getTileX(), mob.getTileY(), this.runDistance / 32 + 2)
                            .stream().filter(runFromMob).map((p) -> new TempDistance(p, mob))
                            .min((p1, p2) -> Float.compare(p1.distance, p2.distance)).orElse(null);
                } else {
                    closest = mob.getLevel().entityManager.players.getInRegionByTileRange(mob.getTileX(), mob.getTileY(), this.runDistance / 32 + 2)
                            .stream().filter(runFromMob).map((p) -> new TempDistance(p, mob))
                            .min((p1, p2) -> Float.compare(p1.distance, p2.distance)).orElse(null);
                }

                if (closest != null && closest.distance <= (float) this.runDistance) {
                    AINodeResult result = this.startRun(mob, closest.runningFrom.getTileX(), closest.runningFrom.getTileY(), closest.runningFrom);
                    if (result != null) {
                        return result;
                    }
                }

            }

            return AINodeResult.FAILURE;
        }
    }

    public AINodeResult startRun(T mob, int startTileX, int startTileY, float dx, float dy, ZoneTester zoneTester) {
        int runDistanceTile = (int) Math.ceil((float) this.runDistance / 32.0F);
        Point2D.Float dir = GameMath.normalize(dx, dy);
        float dirMod;
        if (Math.abs(dir.x) > Math.abs(dir.y)) {
            dirMod = 1.0F / Math.abs(dir.x);
        } else {
            dirMod = 1.0F / Math.abs(dir.y);
        }

        dir.x *= dirMod;
        dir.y *= dirMod;
        int radius = 8 + runDistanceTile;
        Point runPoint = WandererAINode.findWanderingPointAround(mob, startTileX + (int) (dir.x * (float) radius), startTileY + (int) (dir.y * (float) radius), radius, zoneTester, 20, 5);
        this.nextRunCooldown = mob.getWorldEntity().getTime() + 2000L;
        if (runPoint != null) {
            return this.moveToTileTask(runPoint.x, runPoint.y, null, (path) -> {
                setRunning(mob, path.moveIfWithin(-1, -1, null));
                return AINodeResult.FAILURE;
            });
        } else {
            runPoint = WandererAINode.findWanderingPointAround(mob, startTileX, startTileY, radius * 2, zoneTester, 20, 5);
            return runPoint != null ? this.moveToTileTask(runPoint.x, runPoint.y, null, (path) -> {
                setRunning(mob, path.moveIfWithin(-1, -1, null));
                return AINodeResult.FAILURE;
            }) : null;
        }
    }

    public AINodeResult startRun(T mob, int startTileX, int startTileY, float dx, float dy, Mob threat) {
        int runDistanceTile = (int) Math.ceil((float) this.runDistance / 32.0F);
        ZoneTester zoneTester = (tileX, tileY) -> {
            double distance = GameMath.diagonalMoveDistance(tileX, tileY, threat.getTileX(), threat.getTileY());
            return distance >= (double) (runDistanceTile + 2);
        };
        return this.startRun(mob, startTileX, startTileY, dx, dy, zoneTester);
    }

    public AINodeResult startRun(T mob, int startTileX, int startTileY, Mob threat) {
        return this.startRun(mob, startTileX, startTileY, mob.x - threat.x, mob.y - threat.y, threat);
    }

    protected static class TempDistance {
        public final Mob runningFrom;
        public final float distance;

        public TempDistance(Mob runningFrom, Mob mob) {
            this.runningFrom = runningFrom;
            this.distance = runningFrom.getDistance(mob);
        }
    }

    public void setRunning(Mob mob, boolean running) {
        if (running != isRunning) {
            isRunning = running;
            mob.buffManager.updateBuffs();
        }
    }
}