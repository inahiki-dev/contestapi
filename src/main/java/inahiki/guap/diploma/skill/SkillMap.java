package inahiki.guap.diploma.skill;

import java.util.Map;
import java.util.Set;

/**
 * Таблица показателей навыков
 */
public class SkillMap {

    private final Map<SkillInfo, Double> map;

    protected SkillMap(final Map<SkillInfo, Double> map) {
        this.map = map;
    }

    public double get(SkillInfo metaData) {
        return map.get(metaData);
    }

    public void set(SkillInfo metaData, double value) {
        map.put(metaData, value);
    }

    public Set<SkillInfo> getKeys() {
        return map.keySet();
    }

}
