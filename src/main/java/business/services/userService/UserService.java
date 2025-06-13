package business.services.userService;

import business.constants.ApiUrls;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.utilities.results.DataResult;
import entites.dtos.UserDto;
import entites.dtos.UserLoginDto;
import entites.dtos.UserRegisterDto;

import javax.swing.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class UserService implements IUserService {

    private final Gson gson = new Gson();
    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public CompletableFuture<DataResult<UserRegisterDto>> register(UserRegisterDto dto) {
        CompletableFuture<DataResult<UserRegisterDto>> future = new CompletableFuture<>();

        try {
            Gson gson = new Gson();
            String json = gson.toJson(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(ApiUrls.USER_REGISTER))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(responseBody -> {
                        Type type = new TypeToken<DataResult<UserRegisterDto>>() {}.getType();
                        Object parsed = gson.fromJson(responseBody, type);
                        return (DataResult<UserRegisterDto>) parsed;
                    })
                    .thenAccept(future::complete)
                    .exceptionally(ex -> {
                        future.completeExceptionally(ex);
                        return null;
                    });

        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    @Override
    public CompletableFuture<DataResult<UserDto>> login(UserLoginDto dto) {
        CompletableFuture<DataResult<UserDto>> future = new CompletableFuture<>();

        try {
            String json = gson.toJson(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(ApiUrls.USER_LOGIN))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(responseBody -> {
                        Type type = new TypeToken<DataResult<UserDto>>() {}.getType();
                        Object parsed = gson.fromJson(responseBody, type);
                        return (DataResult<UserDto>) parsed;
                    })
                    .thenAccept(future::complete)
                    .exceptionally(ex -> {
                        future.completeExceptionally(ex);
                        return null;
                    });

        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }
}
