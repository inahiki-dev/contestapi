package inahiki.guap.diploma.balancer;

import inahiki.guap.diploma.annotation.Segmentation;
import inahiki.guap.diploma.balancer.algorithm.BalancerAlgorithm;
import inahiki.guap.diploma.balancer.algorithm.genetic.GeneticAlgorithm;
import inahiki.guap.diploma.balancer.algorithm.shuffle.ShuffleAlgorithm;
import inahiki.guap.diploma.segment.SegmentDistribution;
import inahiki.guap.diploma.segment.SegmentType;
import inahiki.guap.diploma.segment.exception.SegmentationException;
import inahiki.guap.diploma.skill.SkillInfo;
import inahiki.guap.diploma.skill.SkillManager;
import inahiki.guap.diploma.skill.data.SkillData;
import inahiki.guap.diploma.skill.data.SkillDataEntity;
import inahiki.guap.diploma.skill.exception.SkillDataException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * Модуль для вычисления баланса. При создании экземпляра данного класса, если прежде
 * не были зарегистрированы классы {@link E} через {@link SkillManager#registerSkills(Class)} и {@link T}
 * через {@link SkillManager#registerTypes(Class)}, они регистрируются.
 * @param <E> Класс хранилища данных сущности
 * @param <T> Класс констант, как виды сегментов. Класс обязательно должен иметь аннотацию {@link Segmentation}
 */
public abstract class Balancer<E extends SkillDataEntity, T extends Enum<T>> {

    private final List<SkillInfo> skills;
    private final List<SegmentType> types;

    @SuppressWarnings("unchecked")
    public Balancer() throws SegmentationException, SkillDataException {
        if (getClass().getGenericSuperclass() instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            skills = loadSkills((Class<E>) actualTypeArguments[0]);
            types = loadTypes((Class<T>) actualTypeArguments[1]);
        } else {
            throw new CancellationException();
        }
    }

    // Требуется для привязки к общему экземпляру
    public abstract SkillManager getManager();

    /**
     * Метод выполнения заданного алгоритма баланса над списком сущностей.
     * @param algorithm     Экземпляр класса с реализованным алгоритмом
     * @param entities      Список сущностей
     * @return              Лучшее распределение данных сущностей по сегментам
     * @throws              SkillDataException Не удалось получить данные сущности
     * @see                 GeneticAlgorithm
     * @see                 ShuffleAlgorithm
     */
    public SegmentDistribution balance(BalancerAlgorithm algorithm, List<E> entities) throws SkillDataException {
        List<SkillData> dataList = convert(entities);
        algorithm.setParams(dataList, skills, types);
        return algorithm.balance();
    }

    // Преобразование списка сущностей в список данных сущностей
    private List<SkillData> convert(List<E> entities) throws SkillDataException {
        List<SkillData> dataList = new ArrayList<>(entities.size());
        for (E entity : entities) {
            SkillData data = getManager().load(entity);
            dataList.add(data);
        }
        return dataList;
    }

    private List<SkillInfo> loadSkills(Class<E> eClass) throws SkillDataException {
        List<SkillInfo> skills = getManager().getSkills(eClass);
        if (skills == null) {
            return getManager().registerSkills(eClass);
        }
        return skills;
    }

    private List<SegmentType> loadTypes(Class<T> tClass) throws SegmentationException {
        List<SegmentType> types = getManager().getTypes(tClass);
        if (types == null) {
            return getManager().registerTypes(tClass);
        }
        return types;
    }

    public List<SkillInfo> getSkills() {
        return skills;
    }

    public List<SegmentType> getTypes() {
        return types;
    }

}
