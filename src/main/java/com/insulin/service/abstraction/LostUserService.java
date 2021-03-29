package com.insulin.service.abstraction;

import com.insulin.metadata.LostUser;

public interface LostUserService {
    void save(String email, String code);

    void deleteById(String id);

    LostUser findByCode(String code);

    void deleteByEmail(String email);
}
