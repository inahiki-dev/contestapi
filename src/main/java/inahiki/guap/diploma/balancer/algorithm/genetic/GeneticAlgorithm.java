package inahiki.guap.diploma.balancer.algorithm.genetic;

import inahiki.guap.diploma.balancer.algorithm.BalancerAlgorithm;
import inahiki.guap.diploma.balancer.algorithm.genetic.mutate.MutateExecutor;
import inahiki.guap.diploma.balancer.algorithm.genetic.mutate.operator.MutateOperator;
import inahiki.guap.diploma.balancer.algorithm.genetic.population.Population;
import inahiki.guap.diploma.segment.Segment;
import inahiki.guap.diploma.segment.SegmentDistribution;
import inahiki.guap.diploma.segment.math.SegmentMath;
import inahiki.guap.diploma.skill.data.SkillData;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Настраиваемый генетический алгоритм.
 */
public class GeneticAlgorithm extends BalancerAlgorithm {

    private final Random random = new Random();
    private final MutateExecutor mutateExecutor;

    /*
     * Параметры по умолчанию были настроены, как результат оптимальных значений для
     * достижения высоких показателях в скорости и точности при больших списках данных
     * сущностей или немалого количества типов сегментов.
     */
    private int individuals = 50;
    private int iterations = 100;
    private int tournament = 2;
    private double mutate = 0.25;
    private double crossover = 0.7;
    private double acceptableBalance = 1.0;

    public GeneticAlgorithm(MutateOperator operator) {
        this.mutateExecutor = operator.getExecutor();
    }

    @Override
    public SegmentDistribution balance() {
        Population population = random(individuals);
        for (int i = 0; ; i++) {
            SegmentDistribution fittest = population.getFittest();
            if (i == iterations || fittest.getBalance() >= acceptableBalance) {
                return fittest;
            }
            population = evolve(population);
        }
    }

    /**
     * В каждой эволюции выделяется самый лучший вариант распределения данных сущностей
     * по сегментам и переходит в следующую популяцию без изменений. Остальные проходят
     * через три оператора:
     * <ul>
     *   <li>Оператор селекции – случайным образом отбирается определенное количество
     *   хромосом {@link #tournament} для участия в турнире, после чего формируется пара
     *   хромосом (родители).</li>
     *   <li>Оператор кроссинговера:
     *     <ol>
     *       <li>Выбирается случайная точка от начала хромосомы до конечного гена в
     *       предпоследнем сегменте;</li>
     *       <li>Первые части родителей №1 и №2 накладываются на первые части потомков
     *       №1 и №2 соответственно;</li>
     *       <li>Вторые части родителя №1 и №2 накладываются на вторые части потомков
     *       №2 и №1 с учётом повторений;</li>
     *       <li>Те гены, которые повторились при наложении второй части потока №1
     *       накладываются в конец второй части потомка №2, а повторяющиеся гены у
     *       потомка №2 в конец второй части потомка №1 соответственно.</li>
 *         </ol>
     *   </li>
     *   <li>Оператор мутации – на выбор даётся один из трёх операторов мутации.
     *   Подробнее смотрите в {@link MutateOperator}.</li>
     * </ul>
     * @param population    текущая популяция распределений
     * @return              следующая популяция распределений
     */
    private Population evolve(Population population) {
        Population newPopulation = new Population(individuals + 1);
        SegmentDistribution fittest = population.getFittest();
        newPopulation.getDistributions().add(fittest);
        for (int i = 1; i < individuals; i += 2) {
            SegmentDistribution distribution1 = tournamentSelection(population, null);
            SegmentDistribution distribution2 = tournamentSelection(population, distribution1);
            if (random.nextDouble() < crossover) {
                SegmentDistribution[] descendants = crossover(distribution1, distribution2);
                distribution1 = descendants[0];
                distribution2 = descendants[1];
            }
            newPopulation.add(preMutate(distribution1));
            newPopulation.add(preMutate(distribution2));
        }
        return newPopulation;
    }

    /**
     * Оператор селекции – случайным образом отбирается определенное количество хромосом.
     * {@link #tournament} для участия в турнире, после чего формируется пара хромосом (родители)
     * @param population    текущая популяция
     * @param paired        Парная хромосома
     * @return              Хромосома для пары
     */
    private SegmentDistribution tournamentSelection(Population population, @Nullable SegmentDistribution paired) {
        Population tournamentPopulation = new Population(tournament);
        for (int i = 0; i < tournament; i++) {
            while (true) {
                int randomId = random.nextInt(0, population.getDistributions().size());
                SegmentDistribution distribution = population.get(randomId);
                if (paired == null || !isSimilar(paired, distribution)) {
                    tournamentPopulation.getDistributions().add(distribution);
                    break;
                }
            }
        }
        return tournamentPopulation.getFittest();
    }

    /**
     * Оператор кроссинговера:
     * <ol>
     *   <li>Выбирается случайная точка от начала хромосомы до конечного гена в
     *   предпоследнем сегменте;</li>
     *   <li>Первые части родителей №1 и №2 накладываются на первые части потомков
     *   №1 и №2 соответственно;</li>
     *   <li>Вторые части родителя №1 и №2 накладываются на вторые части потомков
     *   №2 и №1 с учётом повторений;</li>
     *   <li>Те гены, которые повторились при наложении второй части потока №1
     *   накладываются в конец второй части потомка №2, а повторяющиеся гены у
     *   потомка №2 в конец второй части потомка №1 соответственно.</li>
     * </ol>
     * @param parent1   хромосома-родитель №1
     * @param parent2   хромосома-родитель №2
     * @return          Две хромосомы-потомки
     */
    private SegmentDistribution[] crossover(SegmentDistribution parent1, SegmentDistribution parent2) {
        int size = getDataList().size();
        List<SkillData> parentDataList1 = parent1.getDataList();
        List<SkillData> parentDataList2 = parent2.getDataList();
        List<SkillData> childDataList1 = new ArrayList<>(size);
        List<SkillData> childDataList2 = new ArrayList<>(size);
        int types = getTypes().size();
        int k = random.nextInt(0, size - SegmentMath.size(size, types, types - 1));
        List<SkillData> sub1parent1 = parentDataList1.subList(0, k);
        List<SkillData> sub2parent1 = parentDataList1.subList(k, size);
        List<SkillData> sub1parent2 = parentDataList2.subList(0, k);
        List<SkillData> sub2parent2 = parentDataList2.subList(k, size);
        List<SkillData> t1 = new ArrayList<>();
        List<SkillData> t2 = new ArrayList<>();
        childDataList1.addAll(sub1parent1);
        childDataList2.addAll(sub1parent2);
        for (SkillData data21 : sub2parent1) {
            if (sub1parent2.contains(data21)) {
                t1.add(data21);
            } else {
                childDataList2.add(data21);
            }
        }
        for (SkillData data22 : sub2parent2) {
            if (sub1parent1.contains(data22)) {
                t2.add(data22);
            } else {
                childDataList1.add(data22);
            }
        }
        childDataList1.addAll(t1);
        childDataList2.addAll(t2);
        return new SegmentDistribution[] {
                createDistribution(childDataList1),
                createDistribution(childDataList2)
        };
    }

    /**
     * C заданной вероятностью мутации применяет оператор мутации над хромосомой.
     * @param distribution  хромосома
     * @return              мутирующая хромосома или прежняя
     */
    private SegmentDistribution preMutate(SegmentDistribution distribution) {
        if (random.nextDouble() < mutate) {
            return mutateExecutor.mutate(distribution, random);
        }
        return distribution;
    }

    /**
     * Генерирует случайную популяцию на старте.
     * @param individuals   размер популяции
     * @return              Популяция
     */
    public Population random(int individuals) {
        Population population = new Population(individuals);
        for (int i = 0; i < individuals; i++) {
            SegmentDistribution distribution = createRandomDistribution();
            population.add(distribution);
        }
        return population;
    }

    /**
     * Проверяет на схожесть двух распределений по составу всех сегментов, кроме последней.
     * @param distribution1 распределение №1
     * @param distribution2 распределение №2
     * @return              {@code true} если состав всех сегментов, кроме последней схож
     *                      в распределениях, в ином случае {@code false}
     */
    private boolean isSimilar(SegmentDistribution distribution1, SegmentDistribution distribution2) {
        List<Segment> segments1 = distribution1.getSegments();
        List<Segment> segments2 = distribution2.getSegments();
        for (int i = 0, size = segments1.size(); i < size - 1; i++) {
            if (!isSimilar(segments1.get(i), segments2.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверяет на схожесть два сегмента по составу данных сущностей.
     * @param segment1  сегмент №1
     * @param segment2  сегмент №2
     * @return          {@code true} если состав сегментов схож, в ином случае {@code false}
     */
    private boolean isSimilar(Segment segment1, Segment segment2) {
        List<SkillData> dataList1 = segment1.getDataList();
        List<SkillData> dataList2 = segment2.getDataList();
        for (SkillData data : dataList1) {
            if (!dataList2.contains(data)) {
                return false;
            }
        }
        return true;
    }

    public void setIndividuals(int individuals) {
        this.individuals = individuals;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public void setTournament(int tournament) {
        this.tournament = tournament;
    }

    public void setMutate(double mutate) {
        this.mutate = mutate;
    }

    public void setCrossover(double crossover) {
        this.crossover = crossover;
    }

    public void setAcceptableBalance(double acceptableBalance) {
        this.acceptableBalance = acceptableBalance;
    }
}
