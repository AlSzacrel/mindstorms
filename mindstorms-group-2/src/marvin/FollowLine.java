package marvin;

//import MovementPrimitives;

public class FollowLine implements Step {

	private final MovementPrimitives movPrim;

	public FollowLine(MovementPrimitives movPrim) {
		this.movPrim = movPrim;
	}

	@Override
	public void run(Configuration configuration) {
		evaluateStraightCase(configuration).adjustCourse(movPrim);
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

	private StraightCase evaluateStraightCase(Configuration config) {
		StraightCase currentCase = null;
		LineBorders lineBorders = config.getLines().get(
				config.getLines().size() - 1);
		int lineWidth = lineBorders.getBrightToDark()
				- lineBorders.getDarkToBright();
		if (lineWidth > 50) { // TODO: How wide is the line?
			// Line is too wide, might be orthogonal line or corner.
			currentCase = StraightCase.ORTHOGONAL;
		} else if (lineWidth > 0) {
			// We're still on the line.
			if (lineBorders.getBrightToDark() > 85
					&& lineBorders.getDarkToBright() < 85) {
				// Line is centrally in front of us.
				currentCase = StraightCase.STRAIGHT;
			} else if (lineBorders.getDarkToBright() >= 85) {
				// Line is to the right of center
				currentCase = StraightCase.RIGHT;
			} else if (lineBorders.getBrightToDark() <= 85) {
				// Line is to the left of center
				currentCase = StraightCase.LEFT;
			}
		} else if (lineBorders.getBrightToDark() == Integer.MIN_VALUE
				&& lineBorders.getDarkToBright() == Integer.MIN_VALUE) {
			currentCase = StraightCase.LOST;
		}
		return currentCase;
	}
}