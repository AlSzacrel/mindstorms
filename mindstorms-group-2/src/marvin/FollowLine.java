package marvin;

import java.util.LinkedList;

import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class FollowLine implements Step {

    private final MovementPrimitives movPrim;
    private final LinkedList<StraightCase> caseHistory;

    public FollowLine(MovementPrimitives movPrim) {
        this.movPrim = movPrim;
        this.caseHistory = new LinkedList<StraightCase>();
    }

    @Override
    public void run(Configuration configuration) {
        evaluateStraightCase(configuration).adjustCourse(movPrim, caseHistory);
    }

    private enum StraightCase {
        LOST() {
            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory) {
                RConsole.println(caseHistory.toString());
                int historySize = caseHistory.size();
                RConsole.println("Set slow");
                movPrim.fullSpeed();
                if (historySize > 1 && caseHistory.get(historySize - 2) != LOST) {
                    RConsole.println("case 1");
                    movPrim.spinLeft();
                    Delay.msDelay(200);
                } else if (historySize > 2 && caseHistory.get(historySize - 3) != LOST) {
                    RConsole.println("case 2");
                    movPrim.spinRight();
                    Delay.msDelay(200);
                } else if (historySize > 3 && caseHistory.get(historySize - 4) != LOST) {
                    RConsole.println("case 3");
                    movPrim.spinRight();
                    Delay.msDelay(200);
                } else if (historySize > 4 && caseHistory.get(historySize - 5) != LOST) {
                    RConsole.println("case 4");
                    movPrim.spinLeft();
                    Delay.msDelay(200);
                } else {
                    movPrim.slow();
                    RConsole.println("else case");
                    movPrim.backup();
                }
                RConsole.println("before delay");
                Delay.msDelay(200);
                RConsole.println("stop");
                movPrim.stop();
                movPrim.slow();
            }
        },

        STRAIGHT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory) {
                movPrim.slow();
                movPrim.drive();
            }
        },

        ORTHOGONAL() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory) {
                movPrim.correctionLeft(); // TODO handle better
            }
        },
        CORRECTION_LEFT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory) {
                movPrim.correctionLeft();
            }
        },

        CORRECTION_RIGHT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory) {
                movPrim.correctionRight();
            }
        },
        TURN_LEFT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory) {
                movPrim.turnLeft();
            }
        },

        TURN_RIGHT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory) {
                movPrim.turnRight();
            }
        },
        SPIN_LEFT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory) {
                movPrim.spinLeft();
            }
        },

        SPIN_RIGHT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory) {
                movPrim.spinRight();
            }
        };

        public abstract void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory);

    }

    private StraightCase evaluateStraightCase(Configuration config) {
        StraightCase currentCase = null;
        LineBorders lineBorders = config.getLines().get(config.getLines().size() - 1);
        int rightBorder = lineBorders.getBrightToDark();
        int leftBorder = lineBorders.getDarkToBright();
        int lineWidth = rightBorder - leftBorder;
        float lineCenter = (rightBorder + leftBorder) / 2;

        if (rightBorder == Integer.MIN_VALUE && leftBorder == Integer.MIN_VALUE) {
            currentCase = StraightCase.LOST;
        } else if (rightBorder == Integer.MIN_VALUE) {
            currentCase = StraightCase.TURN_RIGHT;
        } else if (leftBorder == Integer.MIN_VALUE) {
            currentCase = StraightCase.TURN_LEFT;
        } else if (lineWidth > 90) { // TODO: How wide is the line?
            // Line is too wide, might be orthogonal line or corner.
            // TODO what is with long lines which have both ends?
            if (leftBorder >= 120) {
                // Line is to the right of center
                currentCase = StraightCase.SPIN_RIGHT;
            } else if (rightBorder <= 30) {
                // Line is to the left of center
                currentCase = StraightCase.SPIN_LEFT;
            } else {
                currentCase = StraightCase.STRAIGHT;
            }
        } else if (lineWidth > 0) {
            // We're still on the line.
            if (64 < lineCenter && lineCenter < 94) {
                // Line is centrally in front of us.
                currentCase = StraightCase.STRAIGHT;
            } else if (lineCenter >= 94) {
                // Line is to the right of center
                currentCase = StraightCase.CORRECTION_RIGHT;
            } else {
                // Line is to the left of center
                currentCase = StraightCase.CORRECTION_LEFT;
            }
        } else {
            currentCase = StraightCase.LOST;
        }
        config.write(currentCase.name());
        if (caseHistory.size() > 30) {
            caseHistory.remove(0);
        }
        caseHistory.add(currentCase);
        return currentCase;
    }
}