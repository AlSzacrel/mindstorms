public class Value {

    private final Integer position;
    private final Integer value;

    public Value(Integer position, Integer value) {
        super();
        this.position = position;
        this.value = value;
    }

    public Integer getPosition() {
        return position;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Value [position=" + position + ", value=" + value + "]";
    }

}
