package com.nicode.nursingapp.entities.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DateRquestDto {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
