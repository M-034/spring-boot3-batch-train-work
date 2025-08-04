package com.example.demo.common.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.example.demo.common.entity.Uriage;

@Mapper
public interface UriageMapper {
    @Insert("insert into uriage(uri_id, member_id, created_at) values(#{uriId}, #{memberId}, #{createdAt})")
    void regist(Uriage uriage);

    @Insert({
        "<script>",
        "insert into uriage (uri_id, member_id, created_at) values ",
        "<foreach collection='list' item='uriage' separator=','>",
        "(#{uriage.uriId}, #{uriage.memberId}, #{uriage.createdAt})",
        "</foreach>",
        "</script>"
    })
    void bulkinsert(List<? extends Uriage> list);

    @Delete("truncate table uriage")
    void truncate();

    @Select("select * from uriage")
    Uriage selectAll();
}