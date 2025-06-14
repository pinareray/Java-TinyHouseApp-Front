package business.services.houseService;

import core.utilities.results.DataResult;
import core.utilities.results.Result;
import entites.dtos.HouseCreateDto;
import entites.dtos.HouseDto;
import entites.dtos.HouseListDto;
import entites.dtos.HouseUpdateDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IHouseService {
    CompletableFuture<DataResult<List<HouseListDto>>> getAll(int requesterId);
    CompletableFuture<DataResult<HouseDto>> getById(int id, int requesterId);
    CompletableFuture<DataResult<HouseDto>> add(HouseCreateDto dto);
    CompletableFuture<DataResult<HouseDto>> update(HouseUpdateDto dto);
    CompletableFuture<Result> delete(int id, int requesterId);
    CompletableFuture<DataResult<List<HouseListDto>>> getByHostId(int hostId, int requesterId);
}