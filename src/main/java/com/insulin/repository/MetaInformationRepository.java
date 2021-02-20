package com.insulin.repository;

import com.insulin.metadata.MetaInformation;
import org.springframework.data.repository.CrudRepository;

public interface MetaInformationRepository extends CrudRepository<MetaInformation, Long> {
    MetaInformation findByRefreshTokenAndDeviceInformation(String refreshToken, String device);
    MetaInformation findByUserIdAndRefreshToken(Long userId, String refreshToken);
}
