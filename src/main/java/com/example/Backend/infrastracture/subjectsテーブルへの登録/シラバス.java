package com.example.Backend.infrastracture.subjectsテーブルへの登録;

import com.example.Backend.domain.subjectsテーブルへの登録.record.*;
import com.example.Backend.domain.subjectsテーブルへの登録.シラバスインターフェース;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("subjectシラバス")
public class シラバス implements シラバスインターフェース {
    private JdbcClient jdbcClient;

    public シラバス(@Qualifier("学生JdbcClient作成") JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public 講義情報リスト 取得する(){
        String syllabusStatement = """
                    select id as 科目ID, name as 科目名, teacher_name as 教員名 from subjects
                """;

        List<講義情報> 講義情報リスト = jdbcClient.sql(syllabusStatement)
                .query((resultSet, rowNumber) -> new 講義情報(
                        new 科目ID(resultSet.getInt("科目ID")),
                        new 科目名(resultSet.getString("科目名")),
                        new 教員名(resultSet.getString("教員名"))))
                .list();

        return new 講義情報リスト(講義情報リスト);
    }

    public void 科目名表示(){
        String subjectsNameStatement = " select name as 科目名 from subjects ";

        List<科目名> 科目名リスト = jdbcClient.sql(subjectsNameStatement)
                .query(科目名.class)
                .list();
    }
}
