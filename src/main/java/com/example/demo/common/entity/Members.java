package com.example.demo.common.entity;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Members {
    private Integer keiyakuId;
    private Integer memberId;
    private Integer merchId;
    private String cardNum;
    private LocalDate createdAt;
}