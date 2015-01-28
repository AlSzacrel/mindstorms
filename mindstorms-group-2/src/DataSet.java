import java.util.ArrayList;

public class DataSet {

    private ArrayList<Value> values;

    public DataSet(int numberOfElements) {
        super();
        this.values = new ArrayList<>(numberOfElements);
    }

    public void add(Value value) {
        values.add(value);
    }

}
