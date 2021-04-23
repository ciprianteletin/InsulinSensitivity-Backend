package com.insulin.utils.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Class which represents the data displayed in the History page. Instead of sending useless data to the
 * front-end, we send a summary of the data. When the user wants to see the actual result, then the
 * entire set of data will be sent.
 * <p>
 * The id is used to have a way to access the original data that is mapped to the summary.
 * It was designed in mind with the concept of a proxy, sending less data to the client,
 * and when needed, send the entire History object in the requested form.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndexSummary {
    private Long id;
    private List<String> indexNames;
    private String chartsResult;
    private String indexResult;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate creationDate;
}
