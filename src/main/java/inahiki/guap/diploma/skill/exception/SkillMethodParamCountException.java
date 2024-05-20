package inahiki.guap.diploma.skill.exception;

import inahiki.guap.diploma.skill.data.SkillDataEntity;

import java.lang.reflect.Method;

public class SkillMethodParamCountException extends SkillDataException {

    public <T extends SkillDataEntity> SkillMethodParamCountException(Method method, Class<T> tClass) {
        super("Метод " + method.getName() + " экземпляра класса " + tClass.getName() + " не должен иметь параметров");
    }

}
