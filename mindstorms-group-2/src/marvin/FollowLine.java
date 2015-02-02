package marvin;

import java.util.LinkedList;

import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class FollowLine implements Step {

    private final MovementPrimitives movPrim;
    private final LinkedList<StraightCase> caseHistory;
    private final LinkedList<Float> lineCenterHistory;
    
    public FollowLine(MovementPrimitives movPrim) {
        this.movPrim = movPrim;
        this.caseHistory = new LinkedList<StraightCase>();
        this.lineCenterHistory = new LinkedList<Float>();
    }

    @Override
    public void run(Configuration configuration) {
        configuration.getSensorDataCollector().collectData();
        evaluateStraightCase(configuration).adjustCourse(movPrim, caseHistory, lineCenterHistory);
    }

    private enum StraightCase {
        LOST() {
            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory, LinkedList<Float> lineCenterHistory) {
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
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory, LinkedList<Float> lineCenterHistory) {
                movPrim.slow();
                movPrim.drive();
            }
        },

        ORTHOGONAL() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory, LinkedList<Float> lineCenterHistory) {
                movPrim.correctionLeft(); // TODO handle better
            }
        },
        ON_LINE() {
            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory, LinkedList<Float> lineCenterHistory) {
                movPrim.correct((int)(Configuration.MAX_ANGLE / 2 - lineCenterHistory.get(lineCenterHistory.size() -1)));
            }
        
        },
        CORRECTION_LEFT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory, LinkedList<Float> lineCenterHistory) {
                movPrim.correctionLeft();
            }
        },

        CORRECTION_RIGHT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory, LinkedList<Float> lineCenterHistory) {
                movPrim.correctionRight();
            }
        },
        TURN_LEFT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory, LinkedList<Float> lineCenterHistory) {
                movPrim.turnLeft();
            }
        },

        TURN_RIGHT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory, LinkedList<Float> lineCenterHistory) {
                movPrim.turnRight();
            }
        },
        SPIN_LEFT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory, LinkedList<Float> lineCenterHistory) {
                movPrim.spinLeft();
            }
        },

        SPIN_RIGHT() {

            @Override
            public void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory, LinkedList<Float> lineCenterHistory) {
                movPrim.spinRight();
            }
        };

        public abstract void adjustCourse(MovementPrimitives movPrim, LinkedList<StraightCase> caseHistory, LinkedList<Float> lineCenterHistory);

    }

    private StraightCase evaluateStraightCase(Configuration config) {
        StraightCase currentCase = null;
        LineBorders lineBorders = config.getLines().get(config.getLines().size() - 1);
        int rightBorder = lineBorders.getBrightToDark();
        int leftBorder = lineBorders.getDarkToBright();
        float lineCenter = (rightBorder + leftBorder) / 2;

        if (rightBorder == Integer.MIN_VALUE && leftBorder == Integer.MIN_VALUE) {
            currentCase = StraightCase.LOST;
        } else {
        	lineCenter = Math.max(lineCenter,0);
        	currentCase = StraightCase.ON_LINE;
        }
        config.write(currentCase.name());
        if (caseHistory.size() > 30) {
            caseHistory.remove(0);
        }
        caseHistory.add(currentCase);
        lineCenterHistory.add(lineCenter);
        return currentCase;
    }
}