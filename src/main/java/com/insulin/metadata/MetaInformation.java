package com.insulin.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RedisHash("MetaInformation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaInformation implements Serializable {
    @Id
    private Long id;
    private Long userId;
    private String deviceInformation;
    private String refreshToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expirationTime = 432_000_000L; // 5 days Expiration time

    @Override
    public boolean equals(Object meta) {
        if (!(meta instanceof MetaInformation)) {
            return false;
        }
        MetaInformation deviceMeta = (MetaInformation) meta;
        return Objects.equals(userId, deviceMeta.userId) && //
                Objects.equals(refreshToken, deviceMeta.refreshToken) && //
                Objects.equals(deviceInformation, deviceMeta.deviceInformation);
    }
}
