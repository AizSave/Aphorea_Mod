package aphorea.registry;

import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.modifiers.Modifier;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.gameDamageType.DamageType;
import necesse.inventory.item.DoubleItemStatTip;
import necesse.inventory.item.LocalMessageDoubleItemStatTip;

public class AphDamageType {
    public static DamageType BANNER;

    public static void registerCore() {
        DamageTypeRegistry.registerDamageType("banner", BANNER = new BannerDamageType());
    }

    public static class BannerDamageType extends DamageType {

        public BannerDamageType() {
        }

        public Modifier<Float> getBuffDamageModifier() {
            return AphModifiers.BANNER_DAMAGE;
        }

        public Modifier<Float> getBuffAttackSpeedModifier(Attacker attacker) {
            return null;
        }

        public Modifier<Float> getBuffCritChanceModifier() {
            return AphModifiers.BANNER_CRIT_CHANCE;
        }

        public Modifier<Float> getBuffCritDamageModifier() {
            return AphModifiers.BANNER_CRIT_DAMAGE;
        }

        public GameMessage getStatsText() {
            return new LocalMessage("stats", "banner_damage");
        }

        public DoubleItemStatTip getDamageTip(int damage) {
            return new LocalMessageDoubleItemStatTip("itemtooltip", "bannerdamagetip", "value", damage, 0);
        }

        public String getSteamStatKey() {
            return "banner_damage_dealt";
        }
    }
}
