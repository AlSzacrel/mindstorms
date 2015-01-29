package marvin;

public class LineBorders {

    private final int darkToBrightAngle;
    private final int brightToDarkAngle;

    public LineBorders(int darkToBrightAngle, int brightToDarkAngle) {
        this.darkToBrightAngle = darkToBrightAngle;
        this.brightToDarkAngle = brightToDarkAngle;
    }

    public int getDarkToBright() {
        return darkToBrightAngle;
    }

    public int getBrightToDark() {
        return brightToDarkAngle;
    }

    @Override
    public String toString() {
        return "LineBorders [darkToBrightAngle=" + darkToBrightAngle + ", brightToDarkAngle=" + brightToDarkAngle + "]";
    }

}
