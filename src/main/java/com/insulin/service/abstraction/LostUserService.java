package com.insulin.service.abstraction;

import com.insulin.metadata.LostUser;

public interface LostUserService {
    void save(String email, String code);

    void deleteByEmail(String email);

    LostUser findByCode(String code);
}
