package com.insulin.service.abstraction;

import com.insulin.metadata.MetaInformation;

public interface MetaInformationService {
    void save(MetaInformation metaInformation);
    MetaInformation findByRefreshTokenAndDevice(String refreshToken, String device);
    MetaInformation findByUserIdAndRefreshToken(Long userId, String refreshToken);
    void deleteByUserIdAndDeviceDetails(Long userId, String deviceInformation);
    MetaInformation findById(String id);
}
