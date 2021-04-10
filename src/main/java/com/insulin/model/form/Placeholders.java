package com.insulin.model.form;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Placeholders {
    @NonNull
    private String glucosePlaceholder;
    @NotNull
    private String insulinPlaceholder;
}
