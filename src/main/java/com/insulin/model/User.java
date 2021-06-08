package com.insulin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.insulin.enumerations.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(exclude = {"details"})
@ToString(exclude = {"details"})
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name="user_generator", sequenceName = "user_seq")
    @Column(nullable = false, updatable = false)
    private Long id;
    @NotNull
    @Size(min = 6, max = 30)
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 8)
    private String password;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;
    //using the primary key as a foreign key.
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private UserDetails details;
    /**
     * Stored as uni-directional relationship, because there is no need for the
     * history to know and have a reference to the user. Using Cascade.ALL,
     * because we want to get rid of all information associated to the current user.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<History> historyList;

    public void setBidirectionalDetails(UserDetails userDetails) {
        this.details = userDetails;
        userDetails.setUser(this);
    }

    public void addHistory(History history) {
        if (isNull(historyList)) {
            historyList = new ArrayList<>();
        }
        historyList.add(history);
    }
}
