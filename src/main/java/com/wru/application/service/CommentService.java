package com.wru.application.service;

import com.wru.application.model.request.CreateCommentPostRequest;
import com.wru.application.model.request.CreateCommentProductRequest;
import com.wru.application.entity.Comment;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    Comment createCommentPost(CreateCommentPostRequest createCommentPostRequest, long userId);
    Comment createCommentProduct(CreateCommentProductRequest createCommentProductRequest, long userId);
}
