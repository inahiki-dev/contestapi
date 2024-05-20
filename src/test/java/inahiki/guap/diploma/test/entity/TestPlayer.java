package inahiki.guap.diploma.test.entity;

import inahiki.guap.diploma.annotation.Skill;
import inahiki.guap.diploma.skill.data.SkillDataEntity;
import inahiki.guap.diploma.test.statistic.GlobalStatistic;
import inahiki.guap.diploma.test.statistic.RoundStatistic;

public class TestPlayer implements SkillDataEntity {

    private final RoundStatistic roundStatistic = new RoundStatistic();

    private final int id;
    private final GlobalStatistic globalStatistic;

    public TestPlayer(int id, GlobalStatistic globalStatistic) {
        this.id = id;
        this.globalStatistic = globalStatistic;
    }

    public void update() {
        globalStatistic.append(roundStatistic);
    }

    @Skill(name = "Очки/мин", description = "Средний заработок очков за минуту")
    public double getScoreMin() {
        return div(globalStatistic.getScores(), globalStatistic.getTimePlayed() / 60000.0);
    }

    @Skill(name = "У/С", description = "Отношение убийств к смертям", relevance = 0.9)
    public double getKillDeath() {
        return div(globalStatistic.getKills(), globalStatistic.getDeaths());
    }

    @Skill(name = "Убийство/Мин", description = "Средний показатель убийств за минуту", relevance = 0.7)
    public double getKillMin() {
        return div(globalStatistic.getKills(), globalStatistic.getTimePlayed() / 60000.0);
    }

    @Skill(name = "ТКУ", description = "Точность критических ударов", relevance = 0.5)
    public double getCriticalAccuracy() {
        return div(globalStatistic.getCriticalHits(), globalStatistic.getHits());
    }

    private double div(double v1, double v2) {
        return v2 == 0 ? 0 : v1 / v2;
    }

    public int getId() {
        return id;
    }

    public RoundStatistic getRoundStatistic() {
        return roundStatistic;
    }

    public GlobalStatistic getGlobalStatistic() {
        return globalStatistic;
    }
}
