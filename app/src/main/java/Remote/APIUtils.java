package Remote;

public class APIUtils {

    private APIUtils() { };

    public static final String API_URL = "http://10.0.2.2:8000/"; //10.0.2.2

    public static UserService getUserService() {
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }
}
