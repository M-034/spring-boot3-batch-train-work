package com.example.demo.batch.card.keiyaku.receive;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import com.example.demo.common.entity.Members;
import com.example.demo.common.mapper.MembersMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImportMembersWriter implements ItemWriter<Members> {
    
    private final MembersMapper membersMapper;

    @Override
    public void write(@NonNull Chunk<? extends Members> list) throws Exception {
        membersMapper.bulkinsert(list.getItems());

        // for (Members members: list) {
        //     // DBに登録する
        //     membersMapper.regist(Members);
        // }
    }
}