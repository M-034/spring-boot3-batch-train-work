package com.example.demo.batch.card.keiyaku.receive;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import com.example.demo.common.entity.Members;
import com.example.demo.core.exception.SkipException;

@Component
public class ImportMembersProcessor implements ItemProcessor<ImportMembersItem, Members> {
    @Override
    public Members process(@NonNull ImportMembersItem item) throws Exception {
        Integer ngMerchId = 999;
        //商品IDが999の時は登録しない
        if (ngMerchId.equals(item.getMerchId())) {
            throw new SkipException("この顧客は登録できません");
        }
        //メンバーエンティティを生成して返却
        return item.toMembers();
    }
}