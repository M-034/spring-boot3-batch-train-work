package com.example.demo.common.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.example.demo.common.entity.UriMeisai;

@Mapper
public interface UriMeisaiMapper {
    @Insert("insert into urimeisai(uri_meisai_id, uri_id, uri_kingaku, created_at) values(#{uriMeisaiId}, #{uriId}, #{uriKingaku}, #{createdAt})")
    void regist(UriMeisai uriMeisai);

    @Insert({
        "<script>",
        "insert into urimeisai (uri_meisai_id, uri_id, uri_kingaku, created_at) values ",
        "<foreach collection='list' item='uriMeisai' separator=','>",
        "(#{uriMeisai.uriMeisaiId}, #{uriMeisai.uriId}, #{uriMeisai.uriKingaku}, #{uriMeisai.createdAt})",
        "</foreach>",
        "</script>"
    })
    void bulkinsert(List<? extends UriMeisai> list);

    @Delete("truncate table urimeisai")
    void truncate();

    @Select("select * from urimeisai")
    UriMeisai selectAll();
}