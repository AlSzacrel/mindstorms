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
}