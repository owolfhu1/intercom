package com.orion;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface PokeRepository  extends CrudRepository<Poke, Integer>{
    ArrayList<Poke> findAllByPostId(int postId);
}
