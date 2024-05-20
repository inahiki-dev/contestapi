package inahiki.guap.diploma.skill.data;

import inahiki.guap.diploma.skill.SkillMap;

public class SkillData {

    private final SkillDataEntity entity;
    private final SkillMap skillMap;

    public SkillData(SkillDataEntity entity, SkillMap skillMap) {
        this.entity = entity;
        this.skillMap = skillMap;
    }

    public SkillDataEntity getEntity() {
        return entity;
    }

    public SkillMap getSkillMap() {
        return skillMap;
    }
}
