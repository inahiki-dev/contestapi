package inahiki.guap.diploma.balancer.algorithm.genetic.mutate;

import inahiki.guap.diploma.segment.SegmentDistribution;
import inahiki.guap.diploma.skill.data.SkillData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SectorReverseOrderExecutor implements MutateExecutor {

    @Override
    public SegmentDistribution mutate(SegmentDistribution distribution, Random random) {
        List<SkillData> dataList = distribution.getDataList();
        int max = dataList.size();
        int begin = random.nextInt(0, max);
        int end = random.nextInt(begin, max);
        List<SkillData> sub1 = dataList.subList(0, begin);
        List<SkillData> sub2 = dataList.subList(begin, end);
        List<SkillData> sub3 = dataList.subList(end, max);
        List<SkillData> all = new ArrayList<>(max);
        all.addAll(sub3);
        all.addAll(sub2);
        all.addAll(sub1);
        distribution.setDataList(all);
        distribution.calcBalance();
        return distribution;
    }

}
