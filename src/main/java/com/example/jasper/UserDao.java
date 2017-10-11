package com.example.jasper;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.entity.User;

@Repository
public interface UserDao extends PagingAndSortingRepository<User, Integer> {

}
