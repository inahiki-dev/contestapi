package inahiki.guap.diploma.skill.exception;

import inahiki.guap.diploma.skill.data.SkillDataEntity;

import java.lang.reflect.Method;

public class SkillMethodNANException extends SkillDataException {

    public <T extends SkillDataEntity> SkillMethodNANException(Method method, Class<T> tClass) {
        super("Метод " + method.getName() + " экземпляра класса " + tClass.getName() + " должен возвращать числовое значение");
    }

}
