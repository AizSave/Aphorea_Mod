package aphorea.buffs.Trinkets.Periapts;

import aphorea.other.buffs.trinkets.AphoreaPeriaptActivableBuff;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.ParticleTypeSwitcher;
import necesse.entity.levelEvent.LevelEvent;
import necesse.entity.levelEvent.mobAbilityLevelEvent.MobHealthChangeEvent;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.MobWasHitEvent;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.particle.Particle;

import java.awt.*;

public class DemonicPeriaptBuff extends AphoreaPeriaptActivableBuff {

    public DemonicPeriaptBuff() {
        super("demonicperiaptactivebuff");
    }

    @Override
    public Color getColor() {
        return new Color(204, 0, 0);
    }

}