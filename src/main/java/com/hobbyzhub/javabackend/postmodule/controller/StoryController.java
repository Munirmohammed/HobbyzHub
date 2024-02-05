package com.hobbyzhub.javabackend.postmodule.controller;/*
*
@author ameda
@project backend-modulith
@package com.hobbyzhub.javabackend.postmodule.controller
*
*/
import com.hobbyzhub.javabackend.postmodule.requests.StoryRequest;
import com.hobbyzhub.javabackend.postmodule.responses.StoryCreateResponse;
import com.hobbyzhub.javabackend.postmodule.responses.StoryViewResponse;
import com.hobbyzhub.javabackend.postmodule.service.StoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
class StoryController {
    private final StoryService storyService;

    @PostMapping(value = "/upload", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> uploadStory(@RequestPart StoryRequest request,
            @RequestPart("files") List<MultipartFile> files){
        StoryCreateResponse storyCreateResponse = storyService.uploadStory(request, files);
        assert storyCreateResponse.getEmail().equalsIgnoreCase(request.getEmail());
        /*
        * if it evaluates to true
        * then go 'head and display the values
        * */
        return new ResponseEntity<>(storyCreateResponse,HttpStatus.OK);
    }

    /*
    *Go 'head and display all stories...
    * */
    @GetMapping("/view/all")
    public ResponseEntity<?> displayStories(){
        List<StoryViewResponse> storyViewResponses = storyService.displayStories();
        assert storyViewResponses != null;
        return new ResponseEntity<>(storyViewResponses,HttpStatus.OK);
    }
}
