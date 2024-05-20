package inahiki.guap.diploma.segment;

import inahiki.guap.diploma.segment.math.SegmentMath;
import inahiki.guap.diploma.skill.SkillDetails;
import inahiki.guap.diploma.skill.SkillInfo;
import inahiki.guap.diploma.skill.data.SkillData;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Распределение данных сущностей по сегментам. В генетическом алгоритме
 * представлен в качестве хромосомы.
 */
public abstract class SegmentDistribution {

    private final List<Segment> segments;
    private double balance;

    public SegmentDistribution(List<Segment> segments) {
        this.segments = segments;
    }

    // Требуется для привязки к общему списку статистик по навыкам
    public abstract List<SkillDetails> getDetails();

    /**
     * Функция для подсчёта баланса. Для баланса учитываются два фактора:
     * <ol>
     *   <li>общая разность средних показателей каждого навыка одного сегмента
     *   с соответствующим навыком других сегментов должна стремиться к нулю</li>
     *   <li>расхождение средних показателей по всем навыкам всех
     *   сегментов должно стремиться к единице</li>
     * </ol>
     */
    public void calcBalance() {
        double differenceBalance = 1.0;
        double discrepancyBalance = 1.0;
        for (Segment segment : segments) {
            double discrepancySkillSum = 0;
            for (SkillDetails details : getDetails()) {
                SkillInfo info = details.getInfo();
                double min = details.getMin();
                double max = details.getMax();
                double d = max - min;
                if (d > 0) {
                    double avg = details.getAvg();
                    double avgS = segment.calcAverage(info);
                    double p = info.getRelevance() * (avgS - avg) / d;
                    discrepancySkillSum += 1 - p;
                    differenceBalance *= 1 - Math.abs(p);
                }
            }
            discrepancyBalance *= (1 - Math.abs(1 - discrepancySkillSum / (double) getDetails().size()));
        }
        balance = differenceBalance * discrepancyBalance;
    }

    /**
     * Распределяет список данных сущностей по сегментам по порядку
     * @param dataList список данных сущностей
     */
    public void setDataList(List<SkillData> dataList) {
        int dataAmount = dataList.size();
        int segmentAmount = segments.size();
        int players = 0;
        int id = 0;
        for (Segment segment : segments) {
            int size = SegmentMath.size(dataAmount, segmentAmount, id++);
            List<SkillData> segmentDataList = segment.getDataList();
            segmentDataList.clear();
            segmentDataList.addAll(dataList.subList(players, players += size));
        }
    }

    /**
     * Формирует общий список данных сущностей во всех сегментах в заданном порядке
     * @return общий список данных сущностей
     */
    public List<SkillData> getDataList() {
        return segments.stream()
                .flatMap(segment -> segment.getDataList().stream())
                .collect(Collectors.toList());
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public double getBalance() {
        return balance;
    }
}
