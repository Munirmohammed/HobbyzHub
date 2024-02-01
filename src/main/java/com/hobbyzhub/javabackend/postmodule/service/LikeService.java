package com.hobbyzhub.javabackend.postmodule.service;
import com.hobbyzhub.javabackend.postmodule.entity.Like;
import com.hobbyzhub.javabackend.postmodule.entity.Post;
import com.hobbyzhub.javabackend.postmodule.exceptions.LikeNotFoundException;
import com.hobbyzhub.javabackend.postmodule.exceptions.PostNotFoundException;
import com.hobbyzhub.javabackend.postmodule.repository.LikeRepository;
import com.hobbyzhub.javabackend.postmodule.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private int counter = 1;
    public String setId(){
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 12);
    }
    @Modifying
    public Like createLike(String postId) {
        Post post = postRepository.findById(postId).get();
        Like like = null;
        if(post.getPostId().equalsIgnoreCase(postId)){
            like = Like.builder()
                    .likeId(setId())
                    .likeCount(counter++)
                    .postLike(post)
                    .profileImage(post.getProfileImage())
                    .username(post.getUsername())
                    .likeTime(LocalDateTime.now())
                    .userId(post.getUserId())
                    .build();
            log.info("successfully placed a like...");
              like = likeRepository.save(like);
              //after successfully saving, update the like count in post table...
            post.setLikeCount(like.getLikeCount());
              return like;
        }else{
            log.info("like couldn't be placed...");
            throw new PostNotFoundException("post was not found, therefore like is disabled.");
        }
    }
    public List<Like> displayLikes() {
        List<Like> likes = likeRepository.findAll();
        if(!(likes.isEmpty())){
            return likes;
        }else{
            throw new LikeNotFoundException("likes not found.");
        }
    }

    public Map<String,Boolean> unlike(String likeId) {
        Like like = likeRepository.findById(likeId)
                .orElseThrow(
                        ()-> new LikeNotFoundException("Like with passed Id not found"+likeId));
        Post post = like.getPostLike();
        if(like.getLikeCount()>0 && post!=null){
            like.setLikeCount(like.getLikeCount()-1);
            likeRepository.save(like);
            //also, go head and deduct from post table by 1...
            post.setLikeCount(like.getLikeCount());
            log.info("successfully unliked post: "+like.getPostLike().getPostId());
        }else{
            log.info("unliking operation was unsuccessful.");
        }
        Map<String,Boolean> response = new HashMap<>();
        response.put("unliked",true);
        return response;
    }
}
