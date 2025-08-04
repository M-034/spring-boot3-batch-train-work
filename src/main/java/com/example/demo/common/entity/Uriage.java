package com.example.demo.common.entity;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Uriage {
    private Integer uriId;
    private Integer memberId;
    private LocalDate createdAt;
}