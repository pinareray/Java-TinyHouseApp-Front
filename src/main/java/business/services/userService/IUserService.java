package business.services.userService;

import core.utilities.results.DataResult;
import core.utilities.results.Result;
import entites.dtos.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IUserService {

    CompletableFuture<DataResult<UserRegisterDto>> register(UserRegisterDto dto);
    CompletableFuture<DataResult<UserDto>> login(UserLoginDto dto);
    CompletableFuture<DataResult<List<UserListDto>>> getAll(int requesterId);
    CompletableFuture<DataResult<UserDto>> getById(int id);
    CompletableFuture<DataResult<UserDto>> add(UserCreateDto dto);
    CompletableFuture<DataResult<UserDto>> update(UserUpdateDto dto, int requesterId);
    CompletableFuture<Result> delete(int id, int requesterId);
}


