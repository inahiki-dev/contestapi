package inahiki.guap.diploma.balancer.algorithm.genetic.mutate;

import inahiki.guap.diploma.segment.Segment;
import inahiki.guap.diploma.segment.SegmentDistribution;

import java.util.List;
import java.util.Random;

public interface MutateExecutor {

    SegmentDistribution mutate(SegmentDistribution distribution, Random random);

    /**
     * Ищет случайный сегмент, который не соответствует
     * @param segments          Список сегментов
     * @param parentSegment     Парный сегмент
     * @return                  Возвращает случайный неповторяемый сегмент
     */
    default Segment getRandomSegment(List<Segment> segments, Segment parentSegment, Random random) {
        Segment randomSegment;
        do {
            int randomId = random.nextInt(0, segments.size());
            randomSegment = segments.get(randomId);
        } while (randomSegment == parentSegment);
        return randomSegment;
    }

}
