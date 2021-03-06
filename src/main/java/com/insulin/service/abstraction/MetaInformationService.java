package com.insulin.service.abstraction;

import com.insulin.metadata.MetaInformation;

import java.util.List;

public interface MetaInformationService {
    void save(MetaInformation metaInformation);
    MetaInformation findByRefreshTokenAndDevice(String refreshToken, String device);
    MetaInformation findByUserIdAndRefreshToken(Long userId, String refreshToken);
    void deleteByUserIdAndDeviceDetails(Long userId, String deviceInformation);
    void deleteByUserId(Long userId);
    MetaInformation findById(String id);
    List<MetaInformation> findByUserId(Long id);
}
