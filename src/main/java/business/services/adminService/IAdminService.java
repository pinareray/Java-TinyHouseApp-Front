package business.services.adminService;

import core.utilities.results.DataResult;
import entites.dtos.AdminSummaryDto;
import entites.dtos.SystemStatisticsDto;

import java.util.concurrent.CompletableFuture;

public interface IAdminService {
    CompletableFuture<DataResult<AdminSummaryDto>> getSystemSummary();
    CompletableFuture<DataResult<SystemStatisticsDto>> getSystemStatistics();
}
