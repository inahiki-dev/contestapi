package inahiki.guap.diploma.test.statistic;

public class RoundStatistic implements IStatistic {

    private long scores;
    private int kills;
    private int deaths;
    private int hits;
    private int criticalHits;
    private long startGame;

    public void clear() {
        scores = 0;
        kills = 0;
        deaths = 0;
        hits = 0;
        criticalHits = 0;
        startGame = 0;
    }

    public void appendScores(long append) {
        scores += append;
    }

    public void incKills() {
        kills++;
    }

    public void incDeaths() {
        deaths++;
    }

    public void incHits() {
        hits++;
    }

    public void incCriticalHits() {
        criticalHits++;
    }

    @Override
    public long getTimePlayed() {
        return System.currentTimeMillis() - startGame;
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

    public long getStartGame() {
        return startGame;
    }
}
