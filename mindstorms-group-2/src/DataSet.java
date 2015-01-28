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

    public int size() {
        return values.size();
    }

    @Override
    public String toString() {
        return "DataSet [values=" + values + "]";
    }
    
    public Value get(int index){
    	return values.get(index);
    }

}
