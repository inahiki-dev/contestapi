package inahiki.guap.diploma.balancer.algorithm.genetic.mutate;

import inahiki.guap.diploma.segment.Segment;
import inahiki.guap.diploma.segment.SegmentDistribution;
import inahiki.guap.diploma.skill.data.SkillData;

import java.util.List;
import java.util.Random;

public class DataSwapExecutor implements MutateExecutor {

    @Override
    public SegmentDistribution mutate(SegmentDistribution distribution, Random random) {
        List<Segment> segments = distribution.getSegments();
        Segment segment1 = getRandomSegment(segments, null, random);
        Segment segment2 = getRandomSegment(segments, segment1, random);
        List<SkillData> dataList1 = segment1.getDataList();
        List<SkillData> dataList2 = segment2.getDataList();
        int randomId = random.nextInt(0, Math.min(dataList1.size(), dataList2.size()));
        SkillData data1 = dataList1.get(randomId);
        SkillData data2 = dataList2.get(randomId);
        dataList1.set(randomId, data2);
        dataList2.set(randomId, data1);
        distribution.calcBalance();
        return distribution;
    }

}
