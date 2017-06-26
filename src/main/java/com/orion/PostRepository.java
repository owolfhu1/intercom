package com.orion;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface PostRepository extends CrudRepository<Post, Integer> {
    ArrayList<Post> findAllByOrderByTimeStampDesc();
    ArrayList<Post> findAllByUserNameOrderByTimeStampDesc(String UserName);
}
