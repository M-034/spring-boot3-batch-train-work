package com.example.demo.batch.card.uriage.receive;

import java.time.LocalDate;
import com.example.demo.common.entity.Uriage;
import lombok.Data;

@Data
public class ImportUriageItem {
    private Integer uriId;
    private Integer memberId;
    private String createdAt;

    /**
     * 売上エンティティ生成
     * @return
     */
    public Uriage toUriage() {
        Uriage uriage = new Uriage();
        uriage.setUriId(uriId);
        uriage.setMemberId(memberId);
        uriage.setCreatedAt(LocalDate.parse(createdAt));

        return uriage;
    }
}