package inahiki.guap.diploma.skill;

import inahiki.guap.diploma.skill.data.SkillData;
import java.util.Arrays;
import java.util.List;

/**
 * Хранилище статистики по навыку всех распределяемых сущностей
 */
public class SkillDetails {

    private final SkillInfo info;
    private final double min;
    private final double max;
    private final double avg;

    public SkillDetails(SkillInfo info, List<SkillData> dataList) {
        double[] values = dataList.stream().mapToDouble(data -> data.getSkillMap().get(info)).toArray();
        this.info = info;
        this.avg = Arrays.stream(values).average().orElseThrow();
        this.max = Arrays.stream(values).max().orElseThrow();
        this.min = Arrays.stream(values).min().orElseThrow();
    }

    public SkillInfo getInfo() {
        return info;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getAvg() {
        return avg;
    }
}
