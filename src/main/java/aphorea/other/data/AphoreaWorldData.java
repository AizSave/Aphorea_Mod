package aphorea.other.data;

import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.engine.world.WorldEntity;
import necesse.engine.world.worldData.WorldData;

public class AphoreaWorldData extends WorldData {
    public static final String DATA_KEY = "aphoreaworlddata";
    public boolean gelslimesnulled;

    public AphoreaWorldData() {}

    public AphoreaWorldData getData(WorldEntity world) {
        WorldData customData = world.getWorldData(DATA_KEY);
        if (customData instanceof AphoreaWorldData) {
            return (AphoreaWorldData) customData;
        }
        AphoreaWorldData newData = new AphoreaWorldData();
        world.addWorldData(DATA_KEY, newData);
        return newData;
    }

    @Override
    public void addSaveData(SaveData save) {
        super.addSaveData(save);
        save.addBoolean("gelslimesnulled", gelslimesnulled);
    }

    @Override
    public void applyLoadData(LoadData save) {
        super.applyLoadData(save);
        gelslimesnulled = save.getBoolean("gelslimesnulled", gelslimesnulled);
    }

}
