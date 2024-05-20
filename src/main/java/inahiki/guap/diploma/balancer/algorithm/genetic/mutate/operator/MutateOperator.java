package inahiki.guap.diploma.balancer.algorithm.genetic.mutate.operator;

import inahiki.guap.diploma.balancer.algorithm.genetic.mutate.MutateExecutor;
import inahiki.guap.diploma.balancer.algorithm.genetic.mutate.SegmentSwapExecutor;
import inahiki.guap.diploma.balancer.algorithm.genetic.mutate.SectorReverseOrderExecutor;
import inahiki.guap.diploma.balancer.algorithm.genetic.mutate.DataSwapExecutor;

import java.util.function.Supplier;

public enum MutateOperator {

    /**
     * Выбирает случайным образом два сегмента и точку обмена.
     * Данные сущностей меняются местами.
     * @see DataSwapExecutor
     */
    DATA_SWAP(DataSwapExecutor::new),

    /**
     * Выбирает случайным образом два сегмента и обменивает весь
     * список данных сущностей одного сегмента на список другого.
     * @see SegmentSwapExecutor
     */
    SEGMENT_SWAP(SegmentSwapExecutor::new),

    /**
     * Делит общий список хранилища на 3 сектора случайной длины
     * и заполняет эти секторы в обратном порядке.
     * @see SectorReverseOrderExecutor
     */
    SECTOR_REVERSE_ORDER(SectorReverseOrderExecutor::new);

    private final MutateExecutor executor;

    MutateOperator(Supplier<MutateExecutor> executorSupplier) {
        this.executor = executorSupplier.get();
    }

    public MutateExecutor getExecutor() {
        return executor;
    }

}
