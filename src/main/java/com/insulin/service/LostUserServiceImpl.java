package com.insulin.service;

import com.insulin.metadata.LostUser;
import com.insulin.repository.LostUserRepository;
import com.insulin.service.abstraction.LostUserService;
import com.insulin.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LostUserServiceImpl implements LostUserService {
    private final LostUserRepository lostUserRepository;

    @Autowired
    public LostUserServiceImpl(LostUserRepository lostUserRepository) {
        this.lostUserRepository = lostUserRepository;
    }

    private LostUser buildLostUser(String email, String code) {
        return LostUser.builder() //
                .code(code) //
                .email(AuthenticationUtils.encryptText(email)) //
                .build();
    }

    @Override
    public void save(String email, String code) {
        lostUserRepository.save(buildLostUser(email, code));
    }

    @Override
    public void deleteByEmail(String email) {
        lostUserRepository.deleteByEmail(email);
    }

    @Override
    public LostUser findByCode(String code) {
        return lostUserRepository.findByCode(code);
    }
}
