package aphorea.other.buffs.trinkets;

import necesse.engine.network.server.FollowPosition;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.MobRegistry;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.trinketBuffs.TrinketBuff;
import necesse.entity.mobs.summon.summonFollowingMob.attackingFollowingMob.AttackingFollowingMob;
import necesse.inventory.item.ItemStatTip;
import java.util.LinkedList;
import java.util.Objects;

abstract public class AphoreaSummoningTrinketBuff extends TrinketBuff {

    int count;

    String buffId;
    String mobId;
    int mobQuantity;
    GameDamage damage;

    public AphoreaSummoningTrinketBuff(String buffId, String mobId, int mobQuantity, GameDamage damage) {
        this.buffId = buffId;
        this.mobId = mobId;
        this.mobQuantity = mobQuantity;
        this.damage = damage;
    }

    @Override
    public void init(ActiveBuff activeBuff, BuffEventSubscriber buffEventSubscriber) {
        count = 200;
    }

    @Override
    public void serverTick(ActiveBuff buff) {
        super.serverTick(buff);

        if(this.count >= 200) {
            this.count = 0;

            PlayerMob player = (PlayerMob) buff.owner;
            int summonedMobs = 2 - getSummonedMobs(player.getServerClient(), player);
            if(summonedMobs > 0) {
                for(int i = 0; i < summonedMobs; i++) {
                    AttackingFollowingMob mob = (AttackingFollowingMob) MobRegistry.getMob(mobId, buff.owner.getLevel());

                    player.getServerClient().addFollower(buffId, mob, FollowPosition.WALK_CLOSE, buffId, 1, 2, null, false);
                    mob.updateDamage(damage);
                    mob.getLevel().entityManager.addMob(mob, buff.owner.x, buff.owner.y);
                }
            }
        }
        count++;
    }

    public int getSummonedMobs(ServerClient client, PlayerMob player) {
        return (int) client.streamFollowers().filter((m) -> m.mob.getStringID().equals(mobId) && Objects.equals(m.mob.getFollowingPlayer().getStringID(), player.getStringID())).count();
    }

    public void addStatTooltips(LinkedList<ItemStatTip> list, ActiveBuff currentValues, ActiveBuff lastValues) {
        super.addStatTooltips(list, currentValues, lastValues);
        currentValues.getModifierTooltipsBuilder(true, true).addLastValues(lastValues).buildToStatList(list);
    }
}