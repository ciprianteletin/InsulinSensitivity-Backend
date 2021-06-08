package com.insulin.model.form;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Vertical entity to store variable data. Compared to mandatory information, I can have multiple null values
 * which are not meant to be stored in the database. Each object represent one measure with the corresponding value.
 *
 * This entity was designed contrary to a normal one, which is build horizontally. In our case, the table is build
 * vertically, the column name being now mapped to the attribute „name”.
 */
@Entity
@Table(name = "optional_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class OptionalData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "opt_generator")
    @SequenceGenerator(name="opt_generator", sequenceName = "opt_seq")
    @Column(nullable = false, updatable = false)
    private Long id;
    @NotNull
    @Column(updatable = false)
    private String name;
    @NotNull
    @Column(updatable = false)
    @Min(value = 0)
    private Double value;

    public static OptionalData buildOptionalData(String name, Double value) {
        if (value == null) {
            return null;
        }
        return OptionalData.builder() //
                .name(name) //
                .value(value) //
                .build();
    }
}
