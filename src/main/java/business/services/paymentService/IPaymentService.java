package business.services.paymentService;

import core.utilities.results.DataResult;
import core.utilities.results.Result;
import entites.dtos.MonthlyIncomeDto;
import entites.dtos.PaymentCreateDto;
import entites.dtos.PaymentDto;
import entites.dtos.PaymentListDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IPaymentService {

    CompletableFuture<Result> add(PaymentCreateDto dto);

    CompletableFuture<Result> delete(int id, int requesterId);

    CompletableFuture<DataResult<PaymentDto>> getById(int id, int requesterId);

    CompletableFuture<DataResult<List<PaymentListDto>>> getAll(int requesterId);

    CompletableFuture<DataResult<List<PaymentListDto>>> getByUserId(int userId, int requesterId);

    CompletableFuture<DataResult<List<PaymentListDto>>> getByReservationId(int reservationId, int requesterId);

    CompletableFuture<DataResult<List<MonthlyIncomeDto>>> getMonthlyIncomeByHostId(int hostId);
}
