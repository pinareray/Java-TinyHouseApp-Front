package business.services.paymentService;

import business.constants.ApiUrls;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import core.utilities.results.*;
import entites.dtos.*;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PaymentService implements IPaymentService {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                    new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->
                    LocalDateTime.parse(json.getAsString()))
            .create();

    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public CompletableFuture<Result> add(PaymentCreateDto dto) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiUrls.PAYMENT_ADD))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(dto)))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> gson.fromJson(json, Result.class));
    }

    @Override
    public CompletableFuture<Result> delete(int id, int requesterId) {
        String url = ApiUrls.PAYMENT_DELETE + "/" + id + "?requesterId=" + requesterId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> gson.fromJson(json, Result.class));
    }

    @Override
    public CompletableFuture<DataResult<PaymentDto>> getById(int id, int requesterId) {
        String url = ApiUrls.PAYMENT_GET_BY_ID + "/" + id + "?requesterId=" + requesterId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<PaymentDto>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }

    @Override
    public CompletableFuture<DataResult<List<PaymentListDto>>> getAll(int requesterId) {
        String url = ApiUrls.PAYMENT_GET_ALL + "?requesterId=" + requesterId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<List<PaymentListDto>>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }

    @Override
    public CompletableFuture<DataResult<List<PaymentListDto>>> getByUserId(int userId, int requesterId) {
        String url = ApiUrls.PAYMENT_GET_BY_USER_ID + "/" + userId + "?requesterId=" + requesterId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<List<PaymentListDto>>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }

    @Override
    public CompletableFuture<DataResult<List<PaymentListDto>>> getByReservationId(int reservationId, int requesterId) {
        String url = ApiUrls.PAYMENT_GET_BY_RESERVATION_ID + "/" + reservationId + "?requesterId=" + requesterId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<List<PaymentListDto>>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }

    @Override
    public CompletableFuture<DataResult<List<MonthlyIncomeDto>>> getMonthlyIncomeByHostId(int hostId) {
        String url = ApiUrls.PAYMENT_GET_MONTHLY_INCOME_BY_HOST_ID + "/" + hostId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<List<MonthlyIncomeDto>>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }
}
