package business.services.reservationService;

import core.utilities.results.DataResult;
import core.utilities.results.Result;
import entites.dtos.ReservationCreateDto;
import entites.dtos.ReservationDto;
import entites.dtos.ReservationListDto;
import entites.dtos.ReservationUpdateDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IReservationService {

    CompletableFuture<DataResult<ReservationDto>> add(ReservationCreateDto dto);

    CompletableFuture<DataResult<ReservationDto>> update(ReservationUpdateDto dto);

    CompletableFuture<Result> delete(int id, int requesterId);

    CompletableFuture<DataResult<ReservationDto>> getById(int id, int requesterId);

    CompletableFuture<DataResult<List<ReservationListDto>>> getAll(int requesterId);

    CompletableFuture<DataResult<List<ReservationListDto>>> getByUserId(int userId, int requesterId);

    CompletableFuture<DataResult<List<ReservationListDto>>> getByHouseId(int houseId, int requesterId);
}
