package business.services.commentService;

import core.utilities.results.DataResult;
import core.utilities.results.Result;
import entites.dtos.CommentCreateDto;
import entites.dtos.CommentListDto;
import entites.dtos.CommentUpdateDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ICommentService {

    CompletableFuture<Result> add(CommentCreateDto dto);

    CompletableFuture<Result> update(CommentUpdateDto dto);

    CompletableFuture<Result> delete(int id, int userId);

    CompletableFuture<DataResult<List<CommentListDto>>> getByHouseId(int houseId);

    CompletableFuture<DataResult<List<CommentListDto>>> getAll();
}
