package inahiki.guap.diploma.segment.exception;

public class SegmentationException extends Exception {

    public SegmentationException(Class<?> typeClass) {
        super("Для перечисления " + typeClass.getName() + " не задана аннотация Segmentation");
    }

}
