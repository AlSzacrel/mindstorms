public class Value {

    private final Integer position;
    private final Integer value;
    private final Integer distance;

    public Value(Integer position, Integer value, Integer distance) {
        super();
        this.position = position;
        this.value = value;
        this.distance = distance;
    }

    public Integer getDistance() {
		return distance;
	}

	public Integer getPosition() {
        return position;
    }

    public Integer getLightValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Value [position=" + position + ", value=" + value + ", distance=" + distance + "]";
    }

}
