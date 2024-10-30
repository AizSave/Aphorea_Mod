package aphorea.buffs.Trinkets.Healing;

import aphorea.other.area.AphoreaArea;
import aphorea.other.buffs.trinkets.AphoreaDamageWhenHealTrinketBuff;

import java.awt.*;

public class WitchMedallionBuff extends AphoreaDamageWhenHealTrinketBuff {
    public WitchMedallionBuff() {
        super(20, new AphoreaArea(200, new Color(104, 0, 204)).setDamageArea(15));
    }
}