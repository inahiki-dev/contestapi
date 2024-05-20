package inahiki.guap.diploma.test.utils;

import inahiki.guap.diploma.segment.Segment;
import inahiki.guap.diploma.segment.SegmentDistribution;
import inahiki.guap.diploma.skill.*;
import inahiki.guap.diploma.skill.exception.SkillDataException;
import inahiki.guap.diploma.skill.SkillManager;
import inahiki.guap.diploma.skill.data.SkillData;
import inahiki.guap.diploma.skill.data.SkillDataEntity;
import inahiki.guap.diploma.test.response.SpeedResponse;
import inahiki.guap.diploma.test.entity.TestPlayer;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PrintUtils {

    public static String percent(double percent, int numbers) {
        double d = Math.pow(10, numbers);
        return Math.floor(percent * 100 * d) / d + "%";
    }

    public static String floor(double value, int numbers) {
        double d = Math.pow(10, numbers);
        return Math.floor(value * d) / d + "";
    }

    public static String toIdLine(List<SkillData> dataList) {
        return "[" + dataList.stream()
                .map(SkillData::getEntity)
                .map(SkillDataEntity::getId)
                .map(Objects::toString)
                .collect(Collectors.joining(", ")) + "]";
    }

    public static String details(SkillDetails details) {
        double avg = details.getAvg();
        double min = details.getMin();
        double max = details.getMax();
        return "%s: %s (%s; %s)".formatted(details.getInfo().getName(), floor(avg, 2), floor(min, 2), floor(max, 2));
    }

    public static void print(SpeedResponse response) {
        SegmentDistribution distribution = response.getDistribution();
        double balancePercent = distribution.getBalance();
        System.out.println("----------------------------------------------------");
        System.out.println("Баланс: " + PrintUtils.percent(balancePercent, 3));
        System.out.println("Время: " + response.millis());
        for (Segment segment : distribution.getSegments()) {
            String type = segment.getType().getName();
            String players = PrintUtils.toIdLine(segment.getDataList());
            System.out.println();
            System.out.println(type + "(" + segment.getDataList().size() + "): " + players);
            Set<SkillInfo> infoSet = segment.getDataList().get(0).getSkillMap().getKeys();
            for (SkillInfo info : infoSet.stream().sorted(Comparator.comparingDouble(info -> -info.getRelevance())).toList()) {
                System.out.println("  " + details(new SkillDetails(info, segment.getDataList())));
            }
        }
        System.out.println("----------------------------------------------------");
    }

    public static void print(SkillManager manager, List<TestPlayer> players) throws SkillDataException {
        System.out.println("----------------------------------------------------");
        List<SkillInfo> skills = manager.getSkills(TestPlayer.class).stream()
                .sorted(Comparator.comparingDouble(info -> -info.getRelevance()))
                .toList();

        System.out.print("ID" + " ".repeat(3));
        for (SkillInfo info : skills) {
            String name = info.getName();
            System.out.print(name);
            System.out.print(" ".repeat(15 - name.length()));
        }
        System.out.println();

        for (TestPlayer player : players.stream().sorted(Comparator.comparingInt(TestPlayer::getId)).toList()) {
            System.out.print(player.getId());
            System.out.print(" ".repeat(5 - (player.getId() + "").length()));
            for (SkillInfo info : skills) {
                String floor = floor(info.get(player), 2);
                System.out.print(floor + " ".repeat(15 - floor.length()));
            }
            System.out.println();
        }
        System.out.println("----------------------------------------------------");
    }
}
