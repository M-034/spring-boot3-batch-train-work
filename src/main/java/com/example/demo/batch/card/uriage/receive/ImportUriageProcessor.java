package com.example.demo.batch.card.uriage.receive;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import com.example.demo.common.entity.Uriage;

@Component
public class ImportUriageProcessor implements ItemProcessor<ImportUriageItem, Uriage> {
    @Override
    public Uriage process(@NonNull ImportUriageItem item) throws Exception {
        //売上エンティティを生成して返却
        return item.toUriage();
    }
}