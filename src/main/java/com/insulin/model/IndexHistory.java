package com.insulin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "index_history")
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class IndexHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @NotNull
    @Column(name = "index_name", nullable = false, updatable = false)
    private String indexName;
    @NotNull
    @Min(value = 0)
    @Column(nullable = false, updatable = false)
    private Double result;
    @NotNull
    @Column(nullable = false, updatable = false)
    private String message;
    @NotNull
    @Column(nullable = false, updatable = false)
    private String normalRange;
}
