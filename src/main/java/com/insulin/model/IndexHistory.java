package com.insulin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "index_generator")
    @SequenceGenerator(name="index_generator", sequenceName = "index_seq")
    @Column(nullable = false, updatable = false)
    private Long id;
    @NotNull
    @Column(name = "index_name", updatable = false)
    private String indexName;
    @NotNull
    @Column(updatable = false)
    private Double result;
    @NotNull
    @Column(updatable = false)
    private String message;
    @NotNull
    @Column(updatable = false)
    private String normalRange;
}
