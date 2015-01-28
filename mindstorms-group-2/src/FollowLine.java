//import MovementPrimitives;

public class FollowLine implements Step {

    private DataSet sensData;
    private final MovementPrimitives movPrim;

    public FollowLine(MovementPrimitives movPrim) {
        this.movPrim = movPrim;
    }

    @Override
    public void run(Configuration configuration) {
        sensData = configuration.getLastSensorData();
        evaluateStraightCase().adjustCourse(movPrim);
    }

    private enum StraightCase {
        LOST() {
            @Override
            public void adjustCourse(MovementPrimitives movPrim) {
                movPrim.crawl();
                movPrim.backup();
            }
        },

        STRAIGHT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim) {
                movPrim.slow();
            }

        },

        ORTHOGONAL() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim) {
                movPrim.correctionLeft(); // TODO handle better
            }
        },

        LEFT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim) {
                movPrim.correctionLeft();
            }

        },

        RIGHT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim) {
                movPrim.correctionRight();
            }

        };
        public abstract void adjustCourse(MovementPrimitives movPrim);

    }

    private StraightCase evaluateStraightCase() {
        boolean line = false;
        StraightCase currentCase = null;

        // case line to the left or
        if (sensData.get(0).getLightValue() > 350) {

            for (int i = 1; i < sensData.size(); i++) {

                if (sensData.get(i).getLightValue() <= 350) {
                    currentCase = StraightCase.LEFT;
                    break;

                } else {
                    currentCase = StraightCase.ORTHOGONAL;
                }
            }
        } else {

            for (int i = 1; i < sensData.size(); i++) {

                if (sensData.get(i).getLightValue() > 350) {
                    line = true;

                } else if (line == true) { // Line in center
                    currentCase = StraightCase.STRAIGHT;
                    break;
                }
            }
            if (line == true) {
                currentCase = StraightCase.RIGHT;

            } else {
                currentCase = StraightCase.LOST;
            }
        }
        return currentCase;
    }

    // alter Code der mit schiebesensor nicht funktioniert: nur für Referenz

    /**
     * private enum Direction { LEFT() {
     *
     * @Override public void switchDirection() { Motor.B.stop();
     *           Motor.A.forward(); } }, RIGHT() {
     * @Override public void switchDirection() { Motor.A.stop();
     *           Motor.B.forward(); } };
     *
     *           public abstract void switchDirection(); }
     *
     *           private static final int SILVER_LINE_THRESHOLD = 375;
     *
     *           private Direction last;
     * @Override public void run(Configuration configuration) { Direction
     *           currentDirection =
     *           configuration.getLight().getNormalizedLightValue() <
     *           SILVER_LINE_THRESHOLD ? Direction.LEFT : Direction.RIGHT; if
     *           (currentDirection == last) { return; }
     *           currentDirection.switchDirection(); last = currentDirection; }
     */
}
