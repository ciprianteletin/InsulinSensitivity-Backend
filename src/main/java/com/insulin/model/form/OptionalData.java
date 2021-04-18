package com.insulin.model.form;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * Vertical entity to store variable data. Compared to mandatory information, I can have multiple null values
 * which are not
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(nullable = false, updatable = false)
    private String name;
    @NotNull
    @Column(nullable = false, updatable = false)
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
