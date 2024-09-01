package aphorea.buffs.Trinkets.Periapts;

import necesse.engine.network.server.FollowPosition;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.DamageTypeRegistry;
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

public class UnstablePeriaptBuff extends TrinketBuff {

    int count;

    GameDamage damage = new GameDamage(DamageTypeRegistry.SUMMON, 8);

    public UnstablePeriaptBuff() {
    }

    @Override
    public void init(ActiveBuff activeBuff, BuffEventSubscriber buffEventSubscriber) {
        count = 200;
    }

    @Override
    public void serverTick(ActiveBuff buff) {
        super.serverTick(buff);

        if(buff.owner.buffManager.hasBuff("sticky")) {
            buff.owner.buffManager.removeBuff("sticky", true);
        }

        if(this.count >= 200) {
            this.count = 0;

            PlayerMob player = (PlayerMob) buff.owner;
            int slimes = 2 - getSlimeFollowers(player.getServerClient(), player);
            if(slimes > 0) {
                for(int i = 0; i < slimes; i++) {
                    AttackingFollowingMob mob = (AttackingFollowingMob) MobRegistry.getMob("babyunstablegelslime", buff.owner.getLevel());

                    player.getServerClient().addFollower("unstableperiaptbuff", mob, FollowPosition.WALK_CLOSE, "unstableperiaptbuff", 1, 2, null, false);
                    mob.updateDamage(damage);
                    mob.getLevel().entityManager.addMob(mob, buff.owner.x, buff.owner.y);
                }
            }
        }
        count++;
    }

    public int getSlimeFollowers(ServerClient client, PlayerMob player) {
        return (int) client.streamFollowers().filter((m) -> m.mob.getStringID().equals("babyunstablegelslime") && Objects.equals(m.mob.getFollowingPlayer().getStringID(), player.getStringID())).count();
    }

    public void addStatTooltips(LinkedList<ItemStatTip> list, ActiveBuff currentValues, ActiveBuff lastValues) {
        super.addStatTooltips(list, currentValues, lastValues);
        currentValues.getModifierTooltipsBuilder(true, true).addLastValues(lastValues).buildToStatList(list);
    }
}