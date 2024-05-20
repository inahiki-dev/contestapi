package inahiki.guap.diploma.test;

import inahiki.guap.diploma.segment.SegmentDistribution;
import inahiki.guap.diploma.skill.exception.SkillDataException;
import inahiki.guap.diploma.test.response.SpeedResponse;
import inahiki.guap.diploma.test.statistic.GlobalStatistic;
import inahiki.guap.diploma.test.entity.TestPlayer;
import inahiki.guap.diploma.test.utils.Solver;
import inahiki.guap.diploma.test.xssf.XSSFLoader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class Test {

    protected static List<TestPlayer> loadPlayers(String fileName) throws IOException, InvalidFormatException {
        XSSFLoader loader = new XSSFLoader();
        loader.setDirPath(System.getProperty("user.dir") + "/build/resources/test");
        loader.setFileName(fileName);
        loader.setScoreColumns(1, 2, 3, 4, 5, 6);
        Map<Integer, List<Double>> map = loader.load();
        return map.entrySet().stream().map(entry -> {
            int id = entry.getKey();
            List<Double> values = entry.getValue();
            GlobalStatistic statistic = new GlobalStatistic();
            statistic.setScores(values.get(0).longValue());
            statistic.setKills(values.get(1).intValue());
            statistic.setDeaths(values.get(2).intValue());
            statistic.setHits(values.get(3).intValue());
            statistic.setCriticalHits(values.get(4).intValue());
            statistic.setTimePlayed(values.get(5).longValue());
            return new TestPlayer(id, statistic);
        }).toList();
    }

    protected static SpeedResponse speed(Solver solver) throws SkillDataException {
        long start = System.nanoTime();
        SegmentDistribution distribution = solver.get();
        long end = System.nanoTime();
        return new SpeedResponse(distribution, end - start);
    }

}
