package com.insulin.service.implementation;

import com.insulin.metadata.MetaInformation;
import com.insulin.repository.MetaInformationRepository;
import com.insulin.service.abstraction.MetaInformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public void deleteByUserIdAndDeviceDetails(Long userId, String deviceInformation) {
        Optional<MetaInformation> metaInformation = metaInformationRepository //
                .findByUserIdAndDeviceInformation(userId, deviceInformation);
        // Even if the metaInf should be present, a defensive approach is preferred.
        metaInformation.ifPresent(metaInf -> metaInformationRepository.deleteById(metaInf.getId()));
        logger.info("Deleted metaInformation with success!");
    }

    @Override
    public void deleteByUserId(Long userId) {
        List<MetaInformation> metaInformations = metaInformationRepository.findByUserId(userId);
        logger.info("Extracted metaInformation by userId!");
        metaInformations.forEach(metaInf -> metaInformationRepository.deleteById(metaInf.getId()));
    }

    @Override
    public MetaInformation findById(String id) {
        Optional<MetaInformation> metaInf = metaInformationRepository.findById(id);
        return metaInf.orElse(null);
    }

    @Override
    public List<MetaInformation> findByUserId(Long id) {
        return metaInformationRepository.findByUserId(id);
    }
}
