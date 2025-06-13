package business.services.userService;

import core.utilities.results.DataResult;
import entites.dtos.UserDto;
import entites.dtos.UserLoginDto;
import entites.dtos.UserRegisterDto;

import java.util.concurrent.CompletableFuture;

public interface IUserService {

    CompletableFuture<DataResult<UserRegisterDto>> register(UserRegisterDto dto);
    CompletableFuture<DataResult<UserDto>> login(UserLoginDto dto);
}


