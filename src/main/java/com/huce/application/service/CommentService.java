package com.huce.application.service;

import com.huce.application.model.request.CreateCommentPostRequest;
import com.huce.application.model.request.CreateCommentProductRequest;
import com.huce.application.entity.Comment;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    Comment createCommentPost(CreateCommentPostRequest createCommentPostRequest, long userId);
    Comment createCommentProduct(CreateCommentProductRequest createCommentProductRequest, long userId);
}
