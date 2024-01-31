package com.hobbyzhub.javabackend.postmodule.exceptions;
import com.HobbyzHub.posts.responses.PostGenericResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostServiceExceptionHandler {
    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;
    @ExceptionHandler(value = {PostNotFoundException.class})
    public ResponseEntity<Object>  handlePostNotFoundException(PostNotFoundException postNotFoundException){
       PostServiceException  postServiceException = PostServiceException.builder()
               .message(postNotFoundException.getMessage())
               .throwable(postNotFoundException.getCause())
               .httpStatus(HttpStatus.NOT_FOUND)
               .build();
        return  new ResponseEntity<>( new PostGenericResponseException<>(
                apiVersion,
                organizationName,
                postNotFoundException.getMessage(),
                false,
                HttpStatus.NOT_FOUND.value(),
                postNotFoundException
        ),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {UserAccountNotFoundException.class})
    public ResponseEntity<Object>  handleUserAccountNotFoundException(UserAccountNotFoundException userAccountNotFoundException){
        UserAccountException userAccountException = UserAccountException.builder()
                .message(userAccountNotFoundException.getMessage())
                .throwable(userAccountNotFoundException.getCause())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return  new ResponseEntity<>( new PostGenericResponseException<>(
                apiVersion,
                organizationName,
               userAccountException.getMessage(),
                false,
                HttpStatus.NOT_FOUND.value(),
                userAccountException
        ),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {WrongMediaUploadFormatException.class})
    public ResponseEntity<?>  handleWrongMediaUploadFormatException(WrongMediaUploadFormatException wrongMediaUploadFormatException){
        MediaFormatException mediaFormatException = MediaFormatException.builder()
                .message(wrongMediaUploadFormatException.getMessage())
                .throwable(wrongMediaUploadFormatException.getCause())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(new PostGenericResponseException<>(
                apiVersion,
                organizationName,
                mediaFormatException.getMessage(),
                false,
                HttpStatus.BAD_REQUEST.value(),
                mediaFormatException
        ),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {CommentNotFoundException.class})
    public ResponseEntity<?> handleCommentNotFoundException(CommentNotFoundException commentNotFoundException){
        CommentServiceException commentServiceException = CommentServiceException.builder()
                .message(commentNotFoundException.getMessage())
                .throwable(commentNotFoundException.getCause())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(new PostGenericResponseException<>(
                apiVersion,
                organizationName,
                commentServiceException.getMessage(),
                false,
                HttpStatus.NOT_FOUND.value(),
                commentNotFoundException
        ),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {LikeNotFoundException.class})
    public ResponseEntity<?> handleLikeNotFoundException(LikeNotFoundException exception){
        LikeException likeException = LikeException.builder()
                .message(exception.getMessage())
                .cause(exception.getCause())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(new PostGenericResponseException<>(
                apiVersion,
                organizationName,
                likeException.getMessage(),
                false,
                HttpStatus.NOT_MODIFIED.value(),
                likeException

        ),HttpStatus.NOT_FOUND);
    }
}
