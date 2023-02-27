package com.huce.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CertificationDTO {
    private long id;
    private String name;
    private String description;
    private String thumbnail;
    private boolean status;
}
