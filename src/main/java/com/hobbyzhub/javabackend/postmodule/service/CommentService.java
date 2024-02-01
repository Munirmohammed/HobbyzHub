package com.hobbyzhub.javabackend.postmodule.service;

import com.hobbyzhub.javabackend.postmodule.entity.Comment;
import com.hobbyzhub.javabackend.postmodule.entity.Post;
import com.hobbyzhub.javabackend.postmodule.exceptions.CommentNotFoundException;
import com.hobbyzhub.javabackend.postmodule.repository.CommentRepository;
import com.hobbyzhub.javabackend.postmodule.repository.PostRepository;
import com.hobbyzhub.javabackend.postmodule.requests.CreateCommentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private int counter=1;
    public String setId(){
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 12);
    }

    @Modifying
    public Comment createComment(CreateCommentRequest createCommentRequest,
                                 String postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        Comment comment = null;
        comment = Comment.builder()
                .commentId(setId())
                .commentCount(counter++)
                .postComment(post)
                .message(createCommentRequest.getMessage())
                .username(post.getUsername())
                .profileImage(post.getProfileImage())
                .commentTime(LocalDateTime.now())
                .deActivateComments(false)
                .build();
        assert comment != null;
        return commentRepository.save(comment);
    }
    public List<Comment> comments() throws CommentNotFoundException {
        List<Comment> comments = commentRepository.findAll();
        if(!(comments.isEmpty())){
            return comments;
        }else{
            throw new CommentNotFoundException("no comments are present here!");
        }
    }

    public Comment getComment(String commentId) {
        return commentRepository.findById(commentId).orElseThrow();
    }
}
