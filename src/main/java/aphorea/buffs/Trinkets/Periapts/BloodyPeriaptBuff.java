package aphorea.buffs.Trinkets.Periapts;

import aphorea.other.buffs.trinkets.AphoreaPeriaptActivableBuff;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.ParticleTypeSwitcher;
import necesse.entity.levelEvent.LevelEvent;
import necesse.entity.levelEvent.mobAbilityLevelEvent.MobHealthChangeEvent;
import necesse.entity.mobs.*;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.particle.Particle;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class BloodyPeriaptBuff extends AphoreaPeriaptActivableBuff {

    public BloodyPeriaptBuff() {
        super("bloodyperiaptactivebuff");
    }

    @Override
    public Color getColor() {
        return new Color(255, 0, 0);
    }

}