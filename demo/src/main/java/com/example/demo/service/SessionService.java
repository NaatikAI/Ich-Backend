package com.example.demo.service;

import com.example.demo.entity.SessionEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public SessionEntity updateSession(SessionEntity session) {
        return sessionRepository.save(session);
    }

}

