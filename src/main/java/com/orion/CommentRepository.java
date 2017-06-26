package com.orion;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
    ArrayList<Comment> findAllByPostIdOrderByTimeStamp(int postId);
}
