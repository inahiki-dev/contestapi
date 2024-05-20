package inahiki.guap.diploma.test.utils;

import inahiki.guap.diploma.segment.SegmentDistribution;
import inahiki.guap.diploma.skill.exception.SkillDataException;

public interface Solver {

    SegmentDistribution get() throws SkillDataException;

}
