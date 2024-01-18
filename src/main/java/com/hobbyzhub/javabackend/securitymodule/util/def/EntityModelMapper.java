package com.hobbyzhub.javabackend.securitymodule.util.def;

import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.payload.response.UserDetailsResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public abstract class EntityModelMapper {
    @Autowired
    private ModelMapper modelMapper;

    protected final AppUser mapPayloadToEntity(Object payload) {
        return modelMapper.map(payload, AppUser.class);
    }

    protected final Object mapEntityToPayload(AppUser appUser) {
        UserDetailsResponse payload = modelMapper.map(appUser, UserDetailsResponse.class);
        // assign the genders
        if(!Objects.isNull(appUser.getGender())) {
            switch(appUser.getGender()) {
                case MALE -> {
                    payload.setGender("Male");
                }

                case FEMALE ->  {
                    payload.setGender("Female");
                }

                case OTHER -> {
                    payload.setGender("Other");
                }
            }
        }

        // convert the birthdate and dateJoined
        if(!Objects.isNull(appUser.getBirthdate())) {
            payload.setBirthdate(appUser.getBirthdate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
        payload.setJoinedDate(appUser.getJoinedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        return payload;
    }
}
