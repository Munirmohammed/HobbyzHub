package com.hobbyzhub.javabackend.postmodule.service;


import com.hobbyzhub.javabackend.postmodule.entity.HashTag;
import com.hobbyzhub.javabackend.postmodule.entity.Post;
import com.hobbyzhub.javabackend.postmodule.repository.HashTagRepository;
import com.hobbyzhub.javabackend.postmodule.repository.PostRepository;
import com.hobbyzhub.javabackend.postmodule.requests.CreateTagRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class HashTagService {
    private final HashTagRepository hashTagRepository;
    private final PostRepository postRepository;
    public String setId(){
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 12);

    }
    public HashTag createTag(CreateTagRequest createTagRequest, String postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        if(post!=null){
            HashTag hashTag = HashTag.builder()
                    .hashTagId(setId())
                    .tagName(createTagRequest.getTagName())
                    .posts(Set.of(post))
                    .build();
            log.info("post found with given Id and therefore proceed to creating a tag...");
            return hashTagRepository.save(hashTag);
        }else{
            log.info("Post with the passed Id couldn't be established, no tag created...");
            return null;
        }
    }
    public List<HashTag> allTags() {
        return hashTagRepository.findAll();
    }
}
