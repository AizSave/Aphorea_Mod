package aphorea.buffs.Trinkets.Periapts;

import necesse.engine.network.server.FollowPosition;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.registries.MobRegistry;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.MobWasHitEvent;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.trinketBuffs.TrinketBuff;
import necesse.entity.mobs.summon.summonFollowingMob.attackingFollowingMob.AttackingFollowingMob;
import necesse.inventory.item.ItemStatTip;
import java.util.ArrayList;
import java.util.LinkedList;

public class NecromancyPeriaptBuff extends TrinketBuff {

    ArrayList<Mob> skeletons = new ArrayList<>();

    static GameDamage damage = new GameDamage(DamageTypeRegistry.SUMMON, 20);

    public NecromancyPeriaptBuff() {
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
    }

    public void onHasAttacked(ActiveBuff buff, MobWasHitEvent event) {
        if((event.target.removed() || event.target.getHealth() <= 0) && event.target.isHostile && buff.owner.isServer()) {
            ServerClient client = ((PlayerMob) (buff.owner)).getServerClient();
            if(client != null && buff.owner.isServer()) {
                float spwanX = buff.owner.x;
                float spwanY = buff.owner.y;

                if(this.skeletons.size() >= 3) {
                    if(!this.skeletons.get(0).removed()) {
                        spwanX = this.skeletons.get(0).x;
                        spwanY = this.skeletons.get(0).y;
                        client.removeFollower(this.skeletons.get(0), true);
                    }
                    this.skeletons.remove(0);
                }

                AttackingFollowingMob mob = (AttackingFollowingMob) MobRegistry.getMob("undeadskeleton", buff.owner.getLevel());

                client.addFollower("necromancyperiaptbuff", mob, FollowPosition.PYRAMID, "necromancyperiaptbuff", 1, 2, null, true);
                mob.updateDamage(damage);
                mob.getLevel().entityManager.addMob(mob, spwanX, spwanY);

                this.skeletons.add(mob);
            }
        }
    }

    public void addStatTooltips(LinkedList<ItemStatTip> list, ActiveBuff currentValues, ActiveBuff lastValues) {
        super.addStatTooltips(list, currentValues, lastValues);
        currentValues.getModifierTooltipsBuilder(true, true).addLastValues(lastValues).buildToStatList(list);
    }
}