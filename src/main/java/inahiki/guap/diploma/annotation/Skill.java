package inahiki.guap.diploma.annotation;

import inahiki.guap.diploma.skill.SkillManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Используется для методов внутри сущностей.
 * @see SkillManager
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Skill {

    String name();

    String description() default "";

    double relevance() default 1.0;

}
