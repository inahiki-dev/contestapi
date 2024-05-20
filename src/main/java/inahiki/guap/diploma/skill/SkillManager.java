package inahiki.guap.diploma.skill;

import inahiki.guap.diploma.annotation.Segmentation;
import inahiki.guap.diploma.annotation.Skill;
import inahiki.guap.diploma.segment.SegmentType;
import inahiki.guap.diploma.segment.exception.SegmentationException;
import inahiki.guap.diploma.skill.exception.SkillMethodNANException;
import inahiki.guap.diploma.skill.exception.SkillMethodParamCountException;
import inahiki.guap.diploma.skill.exception.SkillRelevanceException;
import inahiki.guap.diploma.skill.exception.SkillDataException;
import inahiki.guap.diploma.skill.data.SkillData;
import inahiki.guap.diploma.skill.data.SkillDataEntity;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Менеджер по управлению мета-данными навыков сущностей и типов сегментов
 */
public class SkillManager {

    private final Map<Class<?>, List<SkillInfo>> skillListMap = new HashMap<>();
    private final Map<Class<?>, List<SegmentType>> typeListMap = new HashMap<>();

    public SkillManager() {}

    /**
     * Заполняет таблицу навыков значениями по экземпляру класса, который унаследовал интерфейс {@link SkillDataEntity}.
     * @param entity    объект, класс которого унаследовал интерфейс {@link SkillDataEntity}
     * @return          Хранилище таблицы навыков со значениями и экземпляр класса {@link SkillDataEntity}
     * @throws          SkillDataException экземпляр класса не имеет заданных методов с аннотацией Skill
     * @throws          NullPointerException Класс сущности не зарегистрирован
     */
    public <E extends SkillDataEntity> SkillData load(E entity) throws SkillDataException {
        Map<SkillInfo, Double> map = new HashMap<>();
        List<SkillInfo> infoList = getSkills(entity.getClass());
        for (SkillInfo info : infoList) {
            double value = info.get(entity);
            map.put(info, value);
        }
        SkillMap skillMap = new SkillMap(map);
        return new SkillData(entity, skillMap);
    }

    /**
     * Через ReflectAPI анализирует класс с аннотацией {@link Segmentation} и получает типы сегментов
     * в виде списка. Полученный список заносится в таблицу.
     * @param typeClass     класс перечислений с аннотацией {@link Segmentation}
     * @throws              SegmentationException отсутствует аннотация {@link Segmentation} у заданного класса
     * @return              Типы сегментов
     */
    public <T extends Enum<T>> ArrayList<SegmentType> registerTypes(Class<T> typeClass) throws SegmentationException {
        if (typeClass.getDeclaredAnnotation(Segmentation.class) == null) {
            throw new SegmentationException(typeClass);
        }
        int lastId = 0;
        ArrayList<SegmentType> types = new ArrayList<>();
        for (T constant : typeClass.getEnumConstants()) {
            SegmentType type = new SegmentType(lastId++, constant.name());
            types.add(type);
        }
        types.trimToSize();
        typeListMap.put(typeClass, types);
        return types;
    }

    /**
     * Через ReflectAPI анализирует класс, который унаследовал интерфейс {@link SkillDataEntity}, и
     * получает методы с аннотацией {@link Skill}. Полученный список заносится в таблицу.
     * @param entityClass   класс, который унаследовал интерфейс {@link SkillDataEntity}
     * @throws              SkillMethodNANException возвращаемый тип не является числом
     * @throws              SkillMethodParamCountException метод имеет параметры
     * @throws              SkillRelevanceException значимость навыка выходит за пределы отрезка от 0 до 1
     * @throws              SkillDataException экземпляр класса не имеет методов с аннотацией Skill
     * @return              Виды навыков
     */
    public <E extends SkillDataEntity> List<SkillInfo> registerSkills(Class<E> entityClass) throws SkillDataException {
        ArrayList<SkillInfo> skills = new ArrayList<>();
        for (Method declaredMethod : entityClass.getDeclaredMethods()) {
            Skill annotation = declaredMethod.getDeclaredAnnotation(Skill.class);
            if (annotation != null) {

                if (declaredMethod.getReturnType().getSuperclass() != Number.class
                        && declaredMethod.getReturnType() != Double.TYPE
                        && declaredMethod.getReturnType() != Integer.TYPE
                        && declaredMethod.getReturnType() != Long.TYPE
                        && declaredMethod.getReturnType() != Short.TYPE
                        && declaredMethod.getReturnType() != Byte.TYPE) {
                    throw new SkillMethodNANException(declaredMethod, entityClass);
                }

                if (declaredMethod.getParameterCount() > 0) {
                    throw new SkillMethodParamCountException(declaredMethod, entityClass);
                }
                double relevance = annotation.relevance();
                if (relevance > 1.0 || relevance < 0.0) {
                    throw new SkillRelevanceException(declaredMethod, entityClass);
                }
                declaredMethod.setAccessible(true);
                SkillInfo info = new SkillInfo();
                info.setMethod(declaredMethod);
                info.setName(annotation.name());
                info.setRelevance(relevance);
                if (!annotation.description().isEmpty()) {
                    info.setDescription(annotation.description());
                }
                skills.add(info);
            }
        }
        if (skills.isEmpty()) {
            throw SkillDataException.empty(entityClass);
        }
        skills.trimToSize();
        skillListMap.put(entityClass, skills);
        return skills;
    }

    /**
     * Находит список мета-данных навыков по классу, который унаследовал интерфейс {@link SkillDataEntity}.
     * @param entityClass   класс, который унаследовал интерфейс {@link SkillDataEntity}
     * @return              Список мета-данных навыков {@link SkillInfo}
     */
    public <E extends SkillDataEntity> List<SkillInfo> getSkills(Class<E> entityClass) {
        return skillListMap.get(entityClass);
    }

    /**
     * Находит список типов сегментов по классу перечислений с аннотацией {@link Segmentation}.
     * @param typeClass     класс перечислений с аннотацией {@link Segmentation}
     * @return              Список типов сегментов навыков {@link SkillInfo}
     */
    public <T extends Enum<T>> List<SegmentType> getTypes(Class<T> typeClass) {
        return typeListMap.get(typeClass);
    }

}
