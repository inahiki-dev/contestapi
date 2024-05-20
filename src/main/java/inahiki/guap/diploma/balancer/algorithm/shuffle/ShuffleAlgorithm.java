package inahiki.guap.diploma.balancer.algorithm.shuffle;

import inahiki.guap.diploma.balancer.algorithm.BalancerAlgorithm;
import inahiki.guap.diploma.balancer.algorithm.genetic.GeneticAlgorithm;
import inahiki.guap.diploma.segment.SegmentDistribution;

/**
 * Данный метод балансировки не эффективен по сравнению с {@link GeneticAlgorithm}.
 * В основном используется для сравнения в анализе скорости обработки и точности баланса
 */
public class ShuffleAlgorithm extends BalancerAlgorithm {

    private int iterations = 5000;
    private double acceptableBalance = 1.0;

    public ShuffleAlgorithm() {}

    @Override
    public SegmentDistribution balance() {
        SegmentDistribution bestDistribution = null;
        double bestBalance = 0;
        for (int i = 0; i < iterations; i++) {
            SegmentDistribution distribution = createRandomDistribution();
            if (distribution.getBalance() > bestBalance) {
                bestBalance = distribution.getBalance();
                bestDistribution = distribution;
                if (bestBalance >= acceptableBalance) {
                    break;
                }
            }
        }
        return bestDistribution;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public void setAcceptableBalance(double acceptableBalance) {
        this.acceptableBalance = acceptableBalance;
    }
}
