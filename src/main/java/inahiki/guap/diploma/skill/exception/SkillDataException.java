package inahiki.guap.diploma.skill.exception;

import inahiki.guap.diploma.skill.data.SkillDataEntity;

import java.lang.reflect.Method;

public class SkillDataException extends Exception {

    public SkillDataException(String message) {
        super(message);
    }

    public SkillDataException(String message, Exception exception) {
        super(message, exception);
    }

    public static <T extends SkillDataEntity> SkillDataException empty(Class<T> tClass) {
        return new SkillDataException("Экземпляр класса " + tClass.getName() + " не содержит ни одного метода с аннотацией Skill");
    }

    public static <T extends SkillDataEntity> SkillDataException get(Method method, Class<T> tClass) {
        return new SkillDataException("Не удалось получить число методом " + method.getName() + " из экземпляра класса " + tClass.getName());
    }

    public static <T extends SkillDataEntity> SkillDataException get(ReflectiveOperationException exception, Method method, Class<T> tClass) {
        return new SkillDataException("Не удалось получить число методом " + method.getName() + " из экземпляра класса " + tClass.getName(), exception);
    }
}
