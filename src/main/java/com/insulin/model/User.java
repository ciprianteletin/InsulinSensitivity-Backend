package com.insulin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.insulin.enumerations.Role;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false)
    @NotNull
    @Size(min = 6, max = 30)
    private String username;
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 8)
    private String password;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Role role;
    //using the primary key as a foreign key.
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private UserDetail details;
    /**
     * Stored as uni-directional relationship, because there is no need for the
     * history to know and have a reference to the user.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private List<History> historyList;

    public void setBidirectionalDetails(UserDetail userDetail) {
        this.details = userDetail;
        userDetail.setUser(this);
    }

    public void addHistory(History history) {
        if (isNull(historyList)) {
            historyList = new ArrayList<>();
        }
        historyList.add(history);
    }
}
