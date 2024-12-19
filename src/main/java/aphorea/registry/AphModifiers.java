package aphorea.registry;

import necesse.engine.modifiers.Modifier;
import necesse.engine.modifiers.ModifierLimiter;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.inventory.enchants.ToolItemModifiers;

public class AphModifiers {
    public static final Modifier<Float> MAGIC_HEALING;
    public static final Modifier<Integer> MAGIC_HEALING_FLAT;
    public static final Modifier<Float> MAGIC_HEALING_RECEIVED;
    public static final Modifier<Integer> MAGIC_HEALING_RECEIVED_FLAT;
    public static final Modifier<Float> MAGIC_HEALING_GRACE;

    public static final Modifier<Float> TOOL_MAGIC_HEALING;
    public static final Modifier<Float> TOOL_MAGIC_HEALING_RECEIVED;
    public static final Modifier<Float> TOOL_MAGIC_HEALING_GRACE;

    public static final Modifier<Float> TOOL_AREA_RANGE;

    public static final Modifier<Float> BANNER_DAMAGE;
    public static final Modifier<Float> BANNER_CRIT_CHANCE;
    public static final Modifier<Float> BANNER_CRIT_DAMAGE;

    public static final Modifier<Float> BANNER_EFFECT;
    public static final Modifier<Float> BANNER_ABILITY_SPEED;

    static {
        MAGIC_HEALING = new Modifier<>(BuffModifiers.LIST, "magichealing", 1.0F, 0.0F, Modifier.FLOAT_ADD_APPEND, (v) -> Math.max(0.0F, v), Modifier.NORMAL_PERC_PARSER("magichealing"), ModifierLimiter.NORMAL_PERC_LIMITER("magichealing"));
        MAGIC_HEALING_FLAT = new Modifier<>(BuffModifiers.LIST, "magichealingflat", 0, 0, Modifier.INT_ADD_APPEND, Modifier.NORMAL_FLAT_INT_PARSER("magichealingflat"), ModifierLimiter.NORMAL_FLAT_INT_LIMITER("magichealingflat"));
        MAGIC_HEALING_RECEIVED = new Modifier<>(BuffModifiers.LIST, "magichealingreceived", 1.0F, 0.0F, Modifier.FLOAT_ADD_APPEND, (v) -> Math.max(0.0F, v), Modifier.NORMAL_PERC_PARSER("magichealingreceived"), ModifierLimiter.NORMAL_PERC_LIMITER("magichealingreceived"));
        MAGIC_HEALING_RECEIVED_FLAT = new Modifier<>(BuffModifiers.LIST, "magichealingreceivedflat", 0, 0, Modifier.INT_ADD_APPEND, Modifier.NORMAL_FLAT_INT_PARSER("magichealingreceivedflat"), ModifierLimiter.NORMAL_FLAT_INT_LIMITER("magichealingreceivedflat"));
        MAGIC_HEALING_GRACE = new Modifier<>(BuffModifiers.LIST, "magichealinggrace", 0.0F, 0.0F, Modifier.FLOAT_ADD_APPEND, (v) -> Math.max(0.0F, v), Modifier.NORMAL_PERC_PARSER("magichealinggrace"), ModifierLimiter.NORMAL_PERC_LIMITER("magichealinggrace"));

        TOOL_MAGIC_HEALING = new Modifier<>(ToolItemModifiers.LIST, "magichealing", 0.0F, 0.0F, Modifier.FLOAT_ADD_APPEND, (v) -> Math.max(0.0F, v), Modifier.NORMAL_PERC_PARSER("magichealing"), ModifierLimiter.NORMAL_PERC_LIMITER("magichealing"));
        TOOL_MAGIC_HEALING_RECEIVED = new Modifier<>(ToolItemModifiers.LIST, "magichealingreceived", 0.0F, 0.0F, Modifier.FLOAT_ADD_APPEND, (v) -> Math.max(0.0F, v), Modifier.NORMAL_PERC_PARSER("magichealingreceived"), ModifierLimiter.NORMAL_PERC_LIMITER("magichealingreceived"));
        TOOL_MAGIC_HEALING_GRACE = new Modifier<>(ToolItemModifiers.LIST, "magichealinggrace", 0.0F, 0.0F, Modifier.FLOAT_ADD_APPEND, (v) -> Math.max(0.0F, v), Modifier.NORMAL_PERC_PARSER("magichealinggrace"), ModifierLimiter.NORMAL_PERC_LIMITER("magichealinggrace"));

        TOOL_AREA_RANGE = new Modifier<>(ToolItemModifiers.LIST, "arearange", 0.0F, 0.0F, Modifier.FLOAT_ADD_APPEND, (v) -> Math.max(0.0F, v), Modifier.NORMAL_PERC_PARSER("arearange"), ModifierLimiter.NORMAL_PERC_LIMITER("arearange"));

        BANNER_DAMAGE = new Modifier<>(BuffModifiers.LIST, "bannerdamage", 0.0F, 0.0F, Modifier.FLOAT_ADD_APPEND, (v) -> v, Modifier.NORMAL_PERC_PARSER("bannerdamage"), ModifierLimiter.NORMAL_PERC_LIMITER("bannerdamage"));
        BANNER_CRIT_CHANCE = new Modifier<>(BuffModifiers.LIST, "bannercritchance", 0.0F, 0.0F, Modifier.FLOAT_ADD_APPEND, (v) -> v, Modifier.NORMAL_PERC_PARSER("bannercritchance"), ModifierLimiter.NORMAL_PERC_LIMITER("bannercritchance"));
        BANNER_CRIT_DAMAGE = new Modifier<>(BuffModifiers.LIST, "bannercritdamage", 0.0F, 0.0F, Modifier.FLOAT_ADD_APPEND, (v) -> v, Modifier.NORMAL_PERC_PARSER("bannercritdamage"), ModifierLimiter.NORMAL_PERC_LIMITER("bannercritdamage"));

        BANNER_EFFECT = new Modifier<>(BuffModifiers.LIST, "bannereffect", 1.0F, 0.0F, Modifier.FLOAT_ADD_APPEND, (v) -> Math.max(0.0F, v), Modifier.NORMAL_PERC_PARSER("bannereffect"), ModifierLimiter.NORMAL_PERC_LIMITER("bannereffect"));
        BANNER_ABILITY_SPEED = new Modifier<>(BuffModifiers.LIST, "bannerabilityspeed", 1.0F, 0.0F, Modifier.FLOAT_ADD_APPEND, (v) -> Math.max(0.0F, v), Modifier.NORMAL_PERC_PARSER("bannerabilityspeed"), ModifierLimiter.NORMAL_PERC_LIMITER("bannerabilityspeed"));
    }

    public AphModifiers() {

    }
}