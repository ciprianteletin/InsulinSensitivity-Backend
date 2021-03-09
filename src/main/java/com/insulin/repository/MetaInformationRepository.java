package com.insulin.repository;

import com.insulin.metadata.MetaInformation;
import org.springframework.data.repository.CrudRepository;

/**
 * Manipulate the metaInformation in the database by using basic CRUD operation and operations that
 * require additional filtering. The MetaInformation is stored inside Redis in-memory database,
 * since it is faster compared to a normal database. This approach was chosen because every
 * 15 minutes the database will be queried to find if the user can or cannot receive
 * a new JWT token.
 */
public interface MetaInformationRepository extends CrudRepository<MetaInformation, String> {
    MetaInformation findByRefreshTokenAndDeviceInformation(String refreshToken, String device);
    MetaInformation findByUserIdAndRefreshToken(Long userId, String refreshToken);
    void deleteByUserIdAndDeviceInformation(Long userId, String deviceInformation);
}
