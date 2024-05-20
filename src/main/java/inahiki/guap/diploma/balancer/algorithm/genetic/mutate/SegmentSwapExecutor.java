package inahiki.guap.diploma.balancer.algorithm.genetic.mutate;

import inahiki.guap.diploma.segment.Segment;
import inahiki.guap.diploma.segment.SegmentDistribution;
import inahiki.guap.diploma.skill.data.SkillData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SegmentSwapExecutor implements MutateExecutor {

    @Override
    public SegmentDistribution mutate(SegmentDistribution distribution, Random random) {
        List<Segment> segments = distribution.getSegments();
        Segment segment1 = getRandomSegment(segments, null, random);
        Segment segment2 = getRandomSegment(segments, segment1, random);
        List<SkillData> cloned = new ArrayList<>(segment1.getDataList());
        segment1.getDataList().clear();
        segment1.getDataList().addAll(segment2.getDataList());
        segment2.getDataList().clear();
        segment2.getDataList().addAll(cloned);
        return distribution;
    }

}
