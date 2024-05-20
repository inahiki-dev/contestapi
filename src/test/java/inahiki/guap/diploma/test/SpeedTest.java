package inahiki.guap.diploma.test;

import inahiki.guap.diploma.balancer.Balancer;
import inahiki.guap.diploma.balancer.algorithm.genetic.GeneticAlgorithm;
import inahiki.guap.diploma.balancer.algorithm.genetic.mutate.operator.MutateOperator;
import inahiki.guap.diploma.balancer.algorithm.shuffle.ShuffleAlgorithm;
import inahiki.guap.diploma.segment.exception.SegmentationException;
import inahiki.guap.diploma.skill.SkillManager;
import inahiki.guap.diploma.skill.exception.SkillDataException;
import inahiki.guap.diploma.test.response.SpeedResponse;
import inahiki.guap.diploma.test.segment.TestTeam;
import inahiki.guap.diploma.test.entity.TestPlayer;
import inahiki.guap.diploma.test.utils.PrintUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.DoubleStream;

public class SpeedTest extends Test {

    private final static int TEST_COUNT = 100;

    private static final int SHUFFLE_ITERATIONS = 20000;
    private static final int GENETIC_ITERATIONS = 300;
    private static final double ACCEPTABLE_BALANCE = 1.0;

    private static final int INDIVIDUALS = 20;
    private static final int TOURNAMENT = 1;
    private static final double MUTATE = 1.0;
    private static final double CROSSOVER = 1.0;
    private static final MutateOperator OPERATOR = MutateOperator.DATA_SWAP;

    // 0 - Генетический алгоритм
    // 1 - Случайные перестановки
    private static byte algorithm = 0;

    public static void main(String[] args) throws SegmentationException, SkillDataException, IOException, InvalidFormatException {
        SkillManager manager = new SkillManager();
        Balancer<TestPlayer, TestTeam> balancer = new Balancer<>() {

            @Override
            public SkillManager getManager() {
                return manager;
            }

        };
        List<TestPlayer> players = loadPlayers("data16.xlsx");
        PrintUtils.print(manager, players);
        int d = TEST_COUNT / 10;
        List<Double> balances = new ArrayList<>(TEST_COUNT);
        List<Long> times = new ArrayList<>(TEST_COUNT);
        if (algorithm == 0) {
            System.out.println("Метод балансировки: Генетический алгоритм");
            GeneticAlgorithm algorithm = new GeneticAlgorithm(OPERATOR);
            algorithm.setIndividuals(INDIVIDUALS);
            algorithm.setIterations(GENETIC_ITERATIONS);
            algorithm.setTournament(TOURNAMENT);
            algorithm.setMutate(MUTATE);
            algorithm.setCrossover(CROSSOVER);
            algorithm.setAcceptableBalance(ACCEPTABLE_BALANCE);
            for (int i = 0; i < TEST_COUNT; i++) {
                SpeedResponse response = speed(() -> balancer.balance(algorithm, players));
                balances.add(response.getDistribution().getBalance());
                times.add(response.getNanos());
                if (i % d == 0 && i != 0) {
                    System.out.println(i / d + "0%");
                }
            }
        } else {
            System.out.println("Метод балансировки: Случайные перестановки");
            ShuffleAlgorithm algorithm = new ShuffleAlgorithm();
            algorithm.setIterations(SHUFFLE_ITERATIONS);
            algorithm.setAcceptableBalance(ACCEPTABLE_BALANCE);
            for (int i = 0; i < TEST_COUNT; i++) {
                SpeedResponse response = speed(() -> balancer.balance(algorithm, players));
                balances.add(response.getDistribution().getBalance());
                times.add(response.getNanos());
                if (i % d == 0 && i != 0) {
                    System.out.println(i / d + "0%");
                }
            }
        }
        System.out.println("100%");
        System.out.println();
        System.out.println("Минимальное время: " + time(times, DoubleStream::min));
        System.out.println("Максимальное время: " + time(times, DoubleStream::max));
        System.out.println("Среднее время: " + time(times, DoubleStream::average));
        System.out.println();
        System.out.println("Минимальный баланс: " + balance(balances, DoubleStream::min));
        System.out.println("Максимальный баланс: " + balance(balances, DoubleStream::max));
        System.out.println("Средний баланс: " + balance(balances, DoubleStream::average));
    }

    private static String time(List<Long> times, Function<DoubleStream, OptionalDouble> statisticFunction) {
        DoubleStream stream = times.stream().mapToDouble(i -> i);
        double value = statisticFunction.apply(stream).orElseThrow();
        return PrintUtils.floor(value / 1000000d, 3) + "мс";
    }

    private static String balance(List<Double> balances, Function<DoubleStream, OptionalDouble> statisticFunction) {
        DoubleStream stream = balances.stream().mapToDouble(i -> i);
        double value = statisticFunction.apply(stream).orElseThrow();
        return PrintUtils.percent(value, 3);
    }

}
