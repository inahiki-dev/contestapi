package inahiki.guap.diploma.test.response;

import inahiki.guap.diploma.segment.SegmentDistribution;
import inahiki.guap.diploma.test.utils.PrintUtils;

public class SpeedResponse {

    private final SegmentDistribution distribution;
    private final long nanos;

    public SpeedResponse(final SegmentDistribution distribution, final long nanos) {
        this.distribution = distribution;
        this.nanos = nanos;
    }

    public String millis() {
        return PrintUtils.floor(nanos / 1000000.0, 3) + "мс";
    }

    public SegmentDistribution getDistribution() {
        return distribution;
    }

    public long getNanos() {
        return nanos;
    }
}
