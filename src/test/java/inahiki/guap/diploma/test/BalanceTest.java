package inahiki.guap.diploma.test;

import inahiki.guap.diploma.balancer.Balancer;
import inahiki.guap.diploma.balancer.algorithm.genetic.GeneticAlgorithm;
import inahiki.guap.diploma.balancer.algorithm.genetic.mutate.operator.MutateOperator;
import inahiki.guap.diploma.segment.exception.SegmentationException;
import inahiki.guap.diploma.skill.SkillManager;
import inahiki.guap.diploma.skill.exception.SkillDataException;
import inahiki.guap.diploma.test.response.SpeedResponse;
import inahiki.guap.diploma.test.segment.TestTeam;
import inahiki.guap.diploma.test.entity.TestPlayer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import inahiki.guap.diploma.test.utils.PrintUtils;

import java.io.IOException;
import java.util.List;

public class BalanceTest extends Test {

    private static final int INDIVIDUALS = 50;
    private static final int ITERATIONS = 4000;
    private static final int TOURNAMENT = 2;
    private static final double MUTATE = 0.25;
    private static final double CROSSOVER = 0.7;
    private static final double ACCEPTABLE_BALANCE = 1.0;
    private static final MutateOperator OPERATOR = MutateOperator.DATA_SWAP;

    public static void main(String[] args) throws SkillDataException, SegmentationException, IOException, InvalidFormatException {
        SkillManager manager = new SkillManager();
        manager.registerTypes(TestTeam.class);
        manager.registerSkills(TestPlayer.class);
        Balancer<TestPlayer, TestTeam> balancer = new Balancer<>() {
            @Override
            public SkillManager getManager() {
                return manager;
            }
        };
        List<TestPlayer> players = loadPlayers("data29.xlsx");
        PrintUtils.print(manager, players);
        GeneticAlgorithm algorithm = new GeneticAlgorithm(OPERATOR);
        algorithm.setIndividuals(INDIVIDUALS);
        algorithm.setIterations(ITERATIONS);
        algorithm.setTournament(TOURNAMENT);
        algorithm.setMutate(MUTATE);
        algorithm.setCrossover(CROSSOVER);
        algorithm.setAcceptableBalance(ACCEPTABLE_BALANCE);
        SpeedResponse response = speed(() -> balancer.balance(algorithm, players));
        PrintUtils.print(response);
    }

}
