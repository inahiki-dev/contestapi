package inahiki.guap.diploma.balancer.algorithm;

import inahiki.guap.diploma.balancer.algorithm.genetic.GeneticAlgorithm;
import inahiki.guap.diploma.balancer.algorithm.shuffle.ShuffleAlgorithm;
import inahiki.guap.diploma.segment.Segment;
import inahiki.guap.diploma.segment.SegmentDistribution;
import inahiki.guap.diploma.segment.SegmentType;
import inahiki.guap.diploma.segment.math.SegmentMath;
import inahiki.guap.diploma.skill.SkillDetails;
import inahiki.guap.diploma.skill.SkillInfo;
import inahiki.guap.diploma.skill.data.SkillData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Оболочка алгоритма для реализаций нахождения баланса среди данных сущностей по сегментам.
 * Этот алгоритм не поддерживает асинхронные потоки и для их выполнения необходимо создавать
 * новый объект каждый раз.
 * @see GeneticAlgorithm
 * @see ShuffleAlgorithm
 */
public abstract class BalancerAlgorithm {

    private List<SkillDetails> details;
    private List<SkillData> dataList;
    private List<SegmentType> types;

    public BalancerAlgorithm() {}

    /**
     * Расширение для метода нахождения лучшего распределения данных сущностей по сегментам.
     * @return Лучшее распределение данных сущностей по сегментам
     */
    public abstract SegmentDistribution balance();

    /**
     * Перед вызовом функции {@link #balance()} заполняются поля параметрами. Для поля
     * {@link #details} конвертируются виды навыков в список статистик по навыкам {@link SkillDetails}.
     * @param dataList  Список данных сущностей
     * @param skills    Виды навыков сущности
     * @param types     Типы сегментов
     */
    public void setParams(List<SkillData> dataList, List<SkillInfo> skills, List<SegmentType> types) {
        this.types = types;
        this.dataList = dataList;
        this.details = skills.stream().map(info -> new SkillDetails(info, getDataList())).toList();
    }

    /**
     * Обобщённый метод для быстрого доступа к созданию случайного распределения данных
     * сущностей по сегментам.
     * @return Случайное распределение данных сущностей по сегментам
     */
    protected SegmentDistribution createRandomDistribution() {
        List<SkillData> shuffledList = new ArrayList<>(getDataList());
        Collections.shuffle(shuffledList);
        return createDistribution(shuffledList);
    }

    /**
     * Обобщённый метод для быстрого доступа к формированию распределения данных
     * сущностей по сегментам из общего списка данных с заданным порядком.
     * @return Распределение данных сущностей по сегментам из общего списка данных с заданным порядком.
     */
    protected SegmentDistribution createDistribution(List<SkillData> dataList) {
        List<SegmentType> types = getTypes();
        int dataAmount = dataList.size();
        int segmentAmount = types.size();
        int players = 0;
        List<Segment> segments = new ArrayList<>(segmentAmount);
        for (SegmentType type : types) {
            int id = type.getId();
            int size = SegmentMath.size(dataAmount, segmentAmount, id);
            List<SkillData> segmentDataList = new ArrayList<>(dataList.subList(players, players += size));
            Segment segment = new Segment(type, segmentDataList);
            segments.add(segment);
        }
        SegmentDistribution container = new SegmentDistribution(segments) {
            @Override
            public List<SkillDetails> getDetails() {
                return BalancerAlgorithm.this.getDetails();
            }
        };
        container.calcBalance();
        return container;
    }

    protected List<SkillDetails> getDetails() {
        return details;
    }

    protected List<SkillData> getDataList() {
        return dataList;
    }

    protected List<SegmentType> getTypes() {
        return types;
    }
}
