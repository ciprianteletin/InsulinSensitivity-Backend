package com.insulin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.insulin.validation.BirthDay;
import com.insulin.validation.Phone;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "user_details")
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(exclude = {"user"})
@ToString(exclude = {"user"})
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class UserDetails implements Serializable {
    @Id
    @Column(name = "user_id")
    private Long id;
    @Column(name = "first_name")
    @NotNull
    @Size(min = 3, max = 20)
    private String firstName;
    @Column(name = "last_name")
    @NotNull
    @Size(min = 3, max = 20)
    private String lastName;
    @Column(unique = true)
    @Email
    @NotNull
    private String email;
    @Column(name = "phone_number", unique = true)
    @NotNull
    @Phone
    private String phoneNr;
    @Column(updatable = false)
    @NotNull
    private Character gender;
    @Column(name = "birth_day", updatable = false)
    @NotNull
    @BirthDay
    private String birthDay;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    private byte[] profileImage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "last_login_date")
    private LocalDate lastLoginDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "join_date")
    private LocalDate joinDate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
