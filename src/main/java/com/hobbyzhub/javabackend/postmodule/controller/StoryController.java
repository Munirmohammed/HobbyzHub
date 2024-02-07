package com.hobbyzhub.javabackend.postmodule.controller;/*
*
@author ameda
@project backend-modulith
@package com.hobbyzhub.javabackend.postmodule.controller
*
*/
import com.hobbyzhub.javabackend.postmodule.entity.Story;
import com.hobbyzhub.javabackend.postmodule.requests.StoryRequest;
import com.hobbyzhub.javabackend.postmodule.responses.StoryCreateResponse;
import com.hobbyzhub.javabackend.postmodule.responses.StoryViewResponse;
import com.hobbyzhub.javabackend.postmodule.service.StoryService;
import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
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

    @PostMapping(value = "/upload/{email}", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> uploadStory(@RequestPart StoryRequest request,
                                         @RequestPart("files") List<MultipartFile> files,
                                         @PathVariable("email") String email) throws Exception{
        StoryCreateResponse storyCreateResponse = storyService.uploadStory(request, files,email);
        assert storyCreateResponse.getEmail().equalsIgnoreCase(email);
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
    /*
    * Dummy endpoint just for testing purposes...
    * */
    @GetMapping("/user/{email}")
    public AppUser getUserByEmail(@PathVariable("email") String email){
        return storyService.getUserByEmail(email);
    }
    /*
    * retrieve stories as per user
    * */
    @GetMapping("/user/email/{email}")
    public ResponseEntity<?> retrieveStoriesPerUser(@PathVariable("email") String email){
        List<Story> stories = storyService.retrieveStoriesPerUser(email);
        assert !(stories.isEmpty());
        return new ResponseEntity<>(stories,HttpStatus.OK);
    }
}
