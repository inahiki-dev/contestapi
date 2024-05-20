package inahiki.guap.diploma.segment;

import inahiki.guap.diploma.skill.SkillInfo;
import inahiki.guap.diploma.skill.data.SkillData;
import java.util.List;

/**
 * В генетическом алгоритме представлен как часть хромосомы с генами.
 */
public class Segment {

    private final SegmentType type;
    private final List<SkillData> dataList;

    public Segment(SegmentType type, List<SkillData> dataList) {
        this.type = type;
        this.dataList = dataList;
    }

    /**
     * Функция подсчёта среднего показателя навыка в данном сегменте
     * @param info  Информация о навыке
     * @return      средний показателя навыка в данном сегменте
     */
    public double calcAverage(SkillInfo info) {
        return dataList.stream()
                .mapToDouble(data -> data.getSkillMap().get(info))
                .average()
                .orElseThrow();
    }

    public SegmentType getType() {
        return type;
    }

    public List<SkillData> getDataList() {
        return dataList;
    }
}
