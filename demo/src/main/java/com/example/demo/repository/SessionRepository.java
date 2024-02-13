package com.example.demo.repository;

import com.example.demo.entity.SessionEntity;
import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity,Integer> {
    List<SessionEntity> findByUser (UserEntity user);

}
