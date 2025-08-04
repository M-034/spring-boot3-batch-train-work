package com.example.demo.batch.card.uriage.receive;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import com.example.demo.common.entity.Uriage;
import com.example.demo.common.mapper.UriageMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImportUriageWriter implements ItemWriter<Uriage> {
    
    private final UriageMapper uriageMapper;

    @Override
    public void write(@NonNull Chunk<? extends Uriage> list) throws Exception {
        uriageMapper.bulkinsert(list.getItems());

        // for (Members members: list) {
        //     // DBに登録する
        //     membersMapper.regist(Members);
        // }
    }
}