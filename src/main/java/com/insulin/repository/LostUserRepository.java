package com.insulin.repository;

import com.insulin.metadata.LostUser;
import org.springframework.data.repository.CrudRepository;

/**
 * Object used for change password operation. Attached to the link, a secret code is created
 * which is linked to the email of the account. It is stored in Redis DB in order to
 * have a fast way to access data.
 */
public interface LostUserRepository extends CrudRepository<LostUser, String> {
    void deleteByEmail(String email);

    LostUser findByCode(String code);
}
