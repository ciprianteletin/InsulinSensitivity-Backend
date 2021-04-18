package com.insulin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.OptionalData;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;

/**
 * Linker entity between the entire data history (inserted values, results) and the user,
 * if there is any logged in.
 */
@Entity
@Table(name = "history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(nullable = false, updatable = false)
    private String result;
    @Valid
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "glucose_id")
    private GlucoseMandatory glucoseMandatory;
    @Valid
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "insulin_id")
    private InsulinMandatory insulinMandatory;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "index_id")
    private List<IndexHistory> indexHistory;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "optional_id")
    private Set<OptionalData> optionalData;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate creationDate;

    public void addIndex(IndexHistory index) {
        if (isNull(indexHistory)) {
            indexHistory = new ArrayList<>();
        }
        indexHistory.add(index);
    }

    public void addOptionalData(OptionalData optionalData) {
        if (isNull(optionalData)) {
            return;
        }
        if (isNull(this.optionalData)) {
            this.optionalData = new HashSet<>();
        }
        this.optionalData.add(optionalData);
    }
}
