package business.services.userService;

import business.constants.ApiUrls;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.utilities.results.DataResult;
import core.utilities.results.Result;
import entites.dtos.*;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserService implements IUserService {

    private final Gson gson = new Gson();
    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public CompletableFuture<DataResult<UserRegisterDto>> register(UserRegisterDto dto) {
        return post(ApiUrls.USER_REGISTER, dto, new TypeToken<DataResult<UserRegisterDto>>(){}.getType());
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
                        DataResult<UserDto> result = gson.fromJson(responseBody, type);

                        // ✅ Başarılı girişte oturumu başlat
                        if (result.isSuccess()) {
                            core.session.UserSession.currentUser = result.getData();
                        }

                        return result;
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
    public CompletableFuture<DataResult<List<UserListDto>>> getAll(int requesterId) {
        String url = ApiUrls.USER_GET_ALL + "?requesterId=" + requesterId;
        return get(url, new TypeToken<DataResult<List<UserListDto>>>(){}.getType());
    }

    @Override
    public CompletableFuture<DataResult<UserDto>> getById(int id) {
        String url = ApiUrls.USER_GET_BY_ID + "?id=" + id;
        return get(url, new TypeToken<DataResult<UserDto>>(){}.getType());
    }

    @Override
    public CompletableFuture<DataResult<UserDto>> add(UserCreateDto dto) {
        return post(ApiUrls.USER_ADD, dto, new TypeToken<DataResult<UserDto>>(){}.getType());
    }

    @Override
    public CompletableFuture<DataResult<UserDto>> update(UserUpdateDto dto, int requesterId) {
        String url = ApiUrls.USER_UPDATE + "?requesterId=" + requesterId;
        return put(url, dto, new TypeToken<DataResult<UserDto>>(){}.getType());
    }

    @Override
    public CompletableFuture<Result> delete(int id, int requesterId) {
        String url = ApiUrls.USER_DELETE_BY_ID + "?id=" + id + "&requesterId=" + requesterId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> gson.fromJson(json, Result.class));
    }

    // Utility methods
    private <T> CompletableFuture<DataResult<T>> post(String url, Object body, Type type) {
        try {
            String json = gson.toJson(body);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(response -> gson.fromJson(response, type));
        } catch (Exception e) {
            CompletableFuture<DataResult<T>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    private <T> CompletableFuture<DataResult<T>> put(String url, Object body, Type type) {
        try {
            String json = gson.toJson(body);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(response -> gson.fromJson(response, type));
        } catch (Exception e) {
            CompletableFuture<DataResult<T>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    private <T> CompletableFuture<DataResult<T>> get(String url, Type type) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(response -> gson.fromJson(response, type));
    }
}
