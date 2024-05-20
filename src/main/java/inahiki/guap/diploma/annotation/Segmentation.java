package inahiki.guap.diploma.annotation;

import inahiki.guap.diploma.skill.SkillManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Используется только для перечислений, чтобы преобразовать константы в типы сегментов.
 * @see SkillManager
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Segmentation {}
