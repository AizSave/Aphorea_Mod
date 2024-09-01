package aphorea.other.data;

import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.level.maps.Level;
import necesse.level.maps.levelData.LevelData;

public class AphoreaSwampLevelData extends LevelData {
    public static final String DATA_KEY = "aphoreaswampleveldata";
    public boolean witchesnulled;

    public AphoreaSwampLevelData() {}

    public AphoreaSwampLevelData getData(Level level) {
        LevelData customData = level.getLevelData(DATA_KEY);
        if (customData instanceof AphoreaSwampLevelData) {
            return (AphoreaSwampLevelData) customData;
        }
        AphoreaSwampLevelData newData = new AphoreaSwampLevelData();
        level.addLevelData(DATA_KEY, newData);
        return newData;
    }

    @Override
    public void addSaveData(SaveData save) {
        super.addSaveData(save);
        save.addBoolean("witchesnulled", witchesnulled);
    }

    @Override
    public void applyLoadData(LoadData save) {
        super.applyLoadData(save);
        witchesnulled = save.getBoolean("witchesnulled", witchesnulled);
    }

}
