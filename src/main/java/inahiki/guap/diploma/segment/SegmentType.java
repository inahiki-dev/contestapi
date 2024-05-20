package inahiki.guap.diploma.segment;

import inahiki.guap.diploma.annotation.Segmentation;

/**
 * Выводится из константы класса перечислений с аннотацией {@link Segmentation}
 */
public class SegmentType {

    private final int id;
    private final String name;

    public SegmentType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
