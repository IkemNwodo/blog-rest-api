package com.ikem.blog.blogrestapi.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private long id;

    @NotEmpty
    @Size(min = 2, message = "name should have at least 2 characters.")
    private String name;

    @Email
    private String email;

    @NotEmpty
    @Size(min = 2, message = "Comment body should have at least 2 characters.")
    private String body;
}
