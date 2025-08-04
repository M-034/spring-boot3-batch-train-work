package com.example.demo.common.entity;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UriMeisai {
    private Integer uriMeisaiId;
    private Integer uriId;
    private String uriKingaku;
    private LocalDate createdAt;
}