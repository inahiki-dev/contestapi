package inahiki.guap.diploma.test.statistic;

public class GlobalStatistic implements IStatistic {

    private long scores;
    private int kills;
    private int deaths;
    private int hits;
    private int criticalHits;
    private long timePlayed;

    public GlobalStatistic() {}

    public void append(IStatistic statistic) {
        scores += statistic.getScores();
        kills += statistic.getKills();
        deaths += statistic.getDeaths();
        hits += statistic.getHits();
        criticalHits += statistic.getCriticalHits();
        timePlayed += statistic.getTimePlayed();
    }

    @Override
    public long getScores() {
        return scores;
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public int getDeaths() {
        return deaths;
    }

    @Override
    public int getHits() {
        return hits;
    }

    @Override
    public int getCriticalHits() {
        return criticalHits;
    }

    @Override
    public long getTimePlayed() {
        return timePlayed;
    }

    public void setScores(long scores) {
        this.scores = scores;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setCriticalHits(int criticalHits) {
        this.criticalHits = criticalHits;
    }

    public void setTimePlayed(long timePlayed) {
        this.timePlayed = timePlayed;
    }

}
