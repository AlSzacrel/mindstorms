package marvin;

import java.util.ArrayList;

public class DataSet {

    private static final int FIRST_ELEMENT = 0;
    private final ArrayList<Value> values;

    public DataSet(int numberOfElements) {
        super();
        this.values = new ArrayList<>(numberOfElements);
    }

    public void append(Value value) {
        values.add(value);
    }

    public void prepend(Value value) {
        values.add(FIRST_ELEMENT, value);
    }

    public int size() {
        return values.size();
    }

    @Override
    public String toString() {
        return "DataSet [values=" + values + "]";
    }

    public Value get(int index) {
        if (values.isEmpty()) {
            return new Value(0, 0, 0);
        }
        return values.get(index);
    }

}
