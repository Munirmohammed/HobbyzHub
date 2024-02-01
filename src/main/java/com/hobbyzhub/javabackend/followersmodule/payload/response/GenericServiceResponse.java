package com.hobbyzhub.javabackend.followersmodule.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author Munir Mohammed
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericServiceResponse<T> implements Serializable {
    private String apiVersion;
    private String organizationName;
    private String message;
    private Boolean success;
    private Integer status;
    private T data;

    private static final long serialVersionUID = 1L;

    // If T is not serializable, consider providing custom serialization logic
    // For example:
    // private void writeObject(ObjectOutputStream out) throws IOException {
    //     // Custom serialization logic for T
    //     out.defaultWriteObject();
    // }
    //
    // private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    //     // Custom deserialization logic for T
    //     in.defaultReadObject();
    // }
}