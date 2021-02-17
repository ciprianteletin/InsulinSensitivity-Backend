package com.insulin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.insulin.validation.Phone;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "user_details")
@NoArgsConstructor
@Builder
@Getter
@Setter
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class UserDetail {
    @Id
    @Column(name = "user_id")
    private Long id;
    @Column(name = "first_name", nullable = false)
    @NotNull
    @Size(min = 3, max = 20)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    @NotNull
    @Size(min = 3, max = 20)
    private String lastName;
    @Column(nullable = false)
    @Email
    @NotNull
    private String email;
    @Column(nullable = false)
    @NotNull
    @Phone
    private String phoneNr;
    @Column(nullable = false)
    @NotNull
    private Character gender;
    @Column(nullable = false)
    @NotNull
    @Min(value = 16)
    @Max(value = 100)
    private int age;
    private String profileImageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate lastLoginDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate lastLoginDateDisplay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate joinDate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
