package business.services.commentService;

import business.constants.ApiUrls;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import core.utilities.results.*;
import entites.dtos.*;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CommentService implements ICommentService {

    private final Gson gson = new Gson();
    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public CompletableFuture<Result> add(CommentCreateDto dto) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiUrls.COMMENT_ADD))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(dto)))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> gson.fromJson(json, Result.class));
    }

    @Override
    public CompletableFuture<Result> update(CommentUpdateDto dto) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiUrls.COMMENT_UPDATE))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(dto)))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> gson.fromJson(json, Result.class));
    }

    @Override
    public CompletableFuture<Result> delete(int id, int userId) {
        String url = ApiUrls.COMMENT_DELETE + "?id=" + id + "&userId=" + userId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> gson.fromJson(json, Result.class));
    }

    @Override
    public CompletableFuture<DataResult<List<CommentListDto>>> getByHouseId(int houseId) {
        String url = ApiUrls.COMMENT_GET_BY_HOUSE_ID + "?houseId=" + houseId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<List<CommentListDto>>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }

    @Override
    public CompletableFuture<DataResult<List<CommentListDto>>> getAll() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiUrls.COMMENT_GET_ALL))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<List<CommentListDto>>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }
}
