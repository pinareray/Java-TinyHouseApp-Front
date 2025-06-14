package business.services.adminService;

import business.constants.ApiUrls;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import core.utilities.results.DataResult;
import entites.dtos.AdminSummaryDto;
import entites.dtos.SystemStatisticsDto;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.CompletableFuture;

public class AdminService implements IAdminService {

    private final Gson gson = new Gson();
    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public CompletableFuture<DataResult<AdminSummaryDto>> getSystemSummary() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiUrls.ADMIN_GET_SUMMARY))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<AdminSummaryDto>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }

    @Override
    public CompletableFuture<DataResult<SystemStatisticsDto>> getSystemStatistics() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiUrls.ADMIN_GET_STATISTICS))  // ApiUrls içinde ilgili URL tanımlı olmalı
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    Type type = new TypeToken<DataResult<SystemStatisticsDto>>() {}.getType();
                    return gson.fromJson(json, type);
                });
    }

}
