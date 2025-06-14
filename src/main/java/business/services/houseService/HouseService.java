package business.services.houseService;

import business.constants.ApiUrls;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import core.utilities.results.DataResult;
import core.utilities.results.Result;
import entites.dtos.HouseCreateDto;
import entites.dtos.HouseDto;
import entites.dtos.HouseListDto;
import entites.dtos.HouseUpdateDto;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HouseService implements IHouseService {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                    new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                    LocalDate.parse(json.getAsString()))
            .create();

    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public CompletableFuture<DataResult<List<HouseListDto>>> getAll(int requesterId) {
        String url = ApiUrls.HOUSE_GET_ALL + "?requesterId=" + requesterId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(responseBody -> {
                    Type type = new TypeToken<DataResult<List<HouseListDto>>>() {}.getType();
                    return gson.fromJson(responseBody, type);
                });
    }

    @Override
    public CompletableFuture<DataResult<HouseDto>> getById(int id, int requesterId) {
        String url = ApiUrls.HOUSE_GET_BY_ID + "?id=" + id + "&requesterId=" + requesterId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(responseBody -> {
                    Type type = new TypeToken<DataResult<HouseDto>>() {}.getType();
                    return gson.fromJson(responseBody, type);
                });
    }

    @Override
    public CompletableFuture<DataResult<HouseDto>> add(HouseCreateDto dto) {
        String json = gson.toJson(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiUrls.HOUSE_ADD))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(responseBody -> {
                    Type type = new TypeToken<DataResult<HouseDto>>() {}.getType();
                    return gson.fromJson(responseBody, type);
                });
    }

    @Override
    public CompletableFuture<DataResult<HouseDto>> update(HouseUpdateDto dto) {
        String json = gson.toJson(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiUrls.HOUSE_UPDATE))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(responseBody -> {
                    Type type = new TypeToken<DataResult<HouseDto>>() {}.getType();
                    return gson.fromJson(responseBody, type);
                });
    }

    @Override
    public CompletableFuture<Result> delete(int id, int requesterId) {
        String url = ApiUrls.HOUSE_DELETE + "?id=" + id + "&requesterId=" + requesterId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(responseBody -> {
                    Type type = new TypeToken<Result>() {}.getType();
                    return gson.fromJson(responseBody, type);
                });
    }

    @Override
    public CompletableFuture<DataResult<List<HouseListDto>>> getByHostId(int hostId, int requesterId) {
        String url = ApiUrls.HOUSE_GET_BY_HOST_ID + "?hostId=" + hostId + "&requesterId=" + requesterId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(responseBody -> {
                    Type type = new TypeToken<DataResult<List<HouseListDto>>>() {}.getType();
                    return gson.fromJson(responseBody, type);
                });
    }
}
