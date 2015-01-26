
public class HelloWorld {

    public static void main(String[] args) {
        Marvin marvin = new Marvin(configuration());
        marvin.drive();
    }

    private static Configuration configuration() {
        return new Configuration();
    }

}