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

/**
 * Class with the sole purpose to store details about the user: it's account id, from what device he accessed the application,
 * and what is his refreshToken. The intention of this class is to provide a different refresh token, even if the user
 * is connected simultaneously on multiple devices. This is useful in order to implement auto-login functionality e.g
 * the user tries to connect to the application after two days of the last login date, the application will auto-login
 * the user without the need to login manually.
 */
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
