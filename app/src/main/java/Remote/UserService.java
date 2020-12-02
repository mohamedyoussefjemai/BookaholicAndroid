package Remote;

import java.util.List;
import Model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @GET("users/")
    Call<List<User>> getUsers();

    @POST("users/add-user")
    Call<User> addUser(@Body User user);

    @PUT("users/update-user/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);

    @DELETE("users/delete-user/{id}")
    Call<User> deleteUser(@Path("id") int id);
}
