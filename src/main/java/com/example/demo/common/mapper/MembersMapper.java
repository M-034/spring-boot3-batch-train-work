package com.example.demo.common.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.example.demo.common.entity.Members;

@Mapper
public interface MembersMapper {
    @Insert("insert into members(keiyaku_id, member_id, merch_id, card_num, created_at) values(#{keiyakuId}, #{memberId}, #{merchId}, #{cardNum}, #{createdAt})")
    void regist(Members members);

    @Insert({
        "<script>",
        "insert into members (keiyaku_id, member_id, merch_id, card_num, created_at) values ",
        "<foreach collection='list' item='member' separator=','>",
        "(#{member.keiyakuId}, #{member.memberId}, #{member.merchId}, #{member.cardNum}, #{member.createdAt})",
        "</foreach>",
        "</script>"
    })
    void bulkinsert(List<? extends Members> list);

    @Delete("truncate table members")
    void truncate();

    @Select("select * from members")
    Members selectAll();
}