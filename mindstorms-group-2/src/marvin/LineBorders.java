package marvin;

public class LineBorders {

    private final int darkToBrightAngle;
    private final int brightToDarkAngle;
    private final int minAngle;
    private final int maxAngle;

    public LineBorders(int darkToBrightAngle, int brightToDarkAngle, int minAngle, int maxAngle) {
        this.darkToBrightAngle = darkToBrightAngle;
        this.brightToDarkAngle = brightToDarkAngle;
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }

    public int getDarkToBright() {
        return darkToBrightAngle;
    }

    public int getBrightToDark() {
        return brightToDarkAngle;
    }

    public int getMinAngle() {
        return minAngle;
    }

    public int getMaxAngle() {
        return maxAngle;
    }

    @Override
    public String toString() {
        return "LineBorders [darkToBrightAngle=" + darkToBrightAngle + ", brightToDarkAngle=" + brightToDarkAngle + "]";
    }

}
