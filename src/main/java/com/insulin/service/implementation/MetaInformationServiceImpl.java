package com.insulin.service.implementation;

import com.insulin.metadata.MetaInformation;
import com.insulin.repository.MetaInformationRepository;
import com.insulin.service.abstraction.MetaInformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetaInformationServiceImpl implements MetaInformationService {
    private final MetaInformationRepository metaInformationRepository;
    private final Logger logger = LoggerFactory.getLogger(MetaInformationServiceImpl.class);

    @Autowired
    public MetaInformationServiceImpl(MetaInformationRepository metaInformationRepository) {
        this.metaInformationRepository = metaInformationRepository;
    }

    @Override
    public void save(MetaInformation metaInformation) {
        metaInformationRepository.save(metaInformation);
        logger.info("MetaInformation saved successfully");
    }

    @Override
    public MetaInformation findByRefreshTokenAndDevice(String refreshToken, String device) {
        return metaInformationRepository.findByRefreshTokenAndDeviceInformation(refreshToken, device);
    }

    @Override
    public MetaInformation findByUserIdAndRefreshToken(Long userId, String refreshToken) {
        return metaInformationRepository.findByUserIdAndRefreshToken(userId, refreshToken);
    }
}
