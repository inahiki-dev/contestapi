package inahiki.guap.diploma.skill.exception;

import inahiki.guap.diploma.skill.data.SkillDataEntity;

import java.lang.reflect.Method;

public class SkillRelevanceException extends SkillDataException {

    public <T extends SkillDataEntity> SkillRelevanceException(Method method, Class<T> tClass) {
        super("Значимость навыка для метода " + method.getName() + " экземпляра класса " + tClass.getName() + " не может быть меньше 0.0 или больше 1.0");
    }

}
