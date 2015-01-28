import java.io.IOException;

public class HelloWorld {

    public static void main(String[] args) throws IOException {
        Marvin marvin = new Marvin(configuration());
        marvin.drive();
    }

    private static Configuration configuration() throws IOException {
        Configuration configuration = new Configuration();
        return configuration;
    }

}
