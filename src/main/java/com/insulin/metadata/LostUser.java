package com.insulin.metadata;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

import static com.insulin.shared.constants.SecurityConstants.RESET_PASSWORD_LIFE;

/**
 * Represents a user which wants to reset it's password. It has a time to live of three
 * hours, so that a link expires after that time. Also, when the link is used and the
 * password is changed, it will became invalid.
 */
@RedisHash(value = "LostUser", timeToLive = RESET_PASSWORD_LIFE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LostUser implements Serializable {
    @Id
    private String id;
    @Indexed
    private String code;
    @Indexed
    private String email;

    @Override
    public boolean equals(Object lostUser) {
        if (!(lostUser instanceof LostUser)) {
            return false;
        }
        LostUser comparedUser = (LostUser) lostUser;
        return Objects.equals(email, comparedUser.email) && //
                Objects.equals(code, comparedUser.code);
    }
}
