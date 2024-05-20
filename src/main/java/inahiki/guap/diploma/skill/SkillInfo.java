package inahiki.guap.diploma.skill;

import inahiki.guap.diploma.annotation.Skill;
import inahiki.guap.diploma.skill.exception.SkillDataException;
import inahiki.guap.diploma.skill.data.SkillDataEntity;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Мета-данные навыка. Экземпляр класса формируется из метода класса сущности наследуемой {@link SkillDataEntity} под аннотацией {@link Skill}
 */
public class SkillInfo {

    private String name;
    private Method method;
    private String description;
    private double relevance;

    public SkillInfo() {}

    public <T extends SkillDataEntity> double get(T entity) throws SkillDataException {
        try {
            if (method.invoke(entity) instanceof Number number) {
                return number.doubleValue();
            }
            throw SkillDataException.get(method, entity.getClass());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw SkillDataException.get(e, method, entity.getClass());
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getRelevance() {
        return relevance;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setMethod(Method method) {
        this.method = method;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    protected void setRelevance(double relevance) {
        this.relevance = relevance;
    }

}
