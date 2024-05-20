package inahiki.guap.diploma.balancer.algorithm.genetic.population;

import inahiki.guap.diploma.segment.SegmentDistribution;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * В качестве хромосомы выступает объект, хранящий распределение
 * данных сущностей по сегментам и процент баланса. Ген в
 * хромосоме представлен в виде самих данных сущностей.
 */
public class Population {

    private List<SegmentDistribution> distributions;
    private SegmentDistribution fittest;

    public Population(int size) {
        distributions = new ArrayList<>(size);
    }

    /**
     * Фитнес функция поиска распределения с лучшим балансом.
     * @return Лучший вариант распределения
     */
    public SegmentDistribution getFittest() {
        if (fittest == null) {
            fittest = distributions.stream()
                    .max(Comparator.comparingDouble(SegmentDistribution::getBalance))
                    .orElseThrow();
        }
        return fittest;
    }

    public SegmentDistribution get(int id) {
        return distributions.get(id);
    }

    public void add(SegmentDistribution distribution) {
        distributions.add(distribution);
    }

    public List<SegmentDistribution> getDistributions() {
        return distributions;
    }

    public void setDistributions(List<SegmentDistribution> distributions) {
        this.distributions = distributions;
    }

    public void setFittest(SegmentDistribution fittest) {
        this.fittest = fittest;
    }
}
