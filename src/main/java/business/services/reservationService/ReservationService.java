package business.services.reservationService;

import business.constants.ApiUrls;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import core.utilities.results.DataResult;
import core.utilities.results.Result;
import entites.dtos.*;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ReservationService implements IReservationService {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                    new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                    LocalDate.parse(json.getAsString()))
            .create();

    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public CompletableFuture<DataResult<ReservationDto>> add(ReservationCreateDto dto) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiUrls.RESERVATION_ADD))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(dto)))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<ReservationDto>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }

    @Override
    public CompletableFuture<DataResult<ReservationDto>> update(ReservationUpdateDto dto) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiUrls.RESERVATION_UPDATE))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(dto)))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<ReservationDto>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }

    @Override
    public CompletableFuture<Result> delete(int id, int requesterId) {
        String url = ApiUrls.RESERVATION_DELETE + "/" + id + "?requesterId=" + requesterId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> gson.fromJson(json, Result.class));
    }

    @Override
    public CompletableFuture<DataResult<ReservationDto>> getById(int id, int requesterId) {
        String url = ApiUrls.RESERVATION_GET_BY_ID + "/" + id + "?requesterId=" + requesterId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<ReservationDto>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }

    @Override
    public CompletableFuture<DataResult<List<ReservationListDto>>> getAll(int requesterId) {
        String url = ApiUrls.RESERVATION_GET_ALL + "?requesterId=" + requesterId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<List<ReservationListDto>>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }

    @Override
    public CompletableFuture<DataResult<List<ReservationListDto>>> getByUserId(int userId, int requesterId) {
        String url = ApiUrls.RESERVATION_GET_BY_USER + "/" + userId + "?requesterId=" + requesterId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<List<ReservationListDto>>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }

    @Override
    public CompletableFuture<DataResult<List<ReservationListDto>>> getByHouseId(int houseId, int requesterId) {
        String url = ApiUrls.RESERVATION_GET_BY_HOUSE + "/" + houseId + "?requesterId=" + requesterId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<List<ReservationListDto>>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }
}
