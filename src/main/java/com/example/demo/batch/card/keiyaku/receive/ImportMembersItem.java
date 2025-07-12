package com.example.demo.batch.card.keiyaku.receive;

import java.time.LocalDate;
import com.example.demo.common.entity.Members;
import lombok.Data;

@Data
public class ImportMembersItem {
    private Integer keiyakuId;
    private Integer memberId;
    private Integer merchId;
    private String cardNum;
    private String createdAt;

    /**
     * メンバーエンティティ生成
     * @return
     */
    public Members toMembers() {
        Members members = new Members();
        members.setKeiyakuId(keiyakuId);
        members.setMemberId(memberId);
        members.setMerchId(merchId);
        members.setCardNum(cardNum);
        members.setCreatedAt(LocalDate.parse(createdAt));

        return members;
    }
}