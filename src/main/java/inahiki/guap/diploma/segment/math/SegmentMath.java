package inahiki.guap.diploma.segment.math;

public class SegmentMath {

    public static int size(int dataAmount, int segmentAmount, int segmentId) {
        double d = dataAmount / (double) segmentAmount;
        return (int) (Math.round(d * (segmentId + 1)) - Math.round(d * segmentId));
    }

}
