package com.example.Backend.infrastracture.teachersテーブルへの登録;

import com.example.Backend.domain.teachersテーブルへの登録.record.名前;
import com.example.Backend.domain.teachersテーブルへの登録.record.名前リスト;
import com.example.Backend.domain.teachersテーブルへの登録.シラバスインターフェース;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class シラバス implements シラバスインターフェース {
    private JdbcClient jdbcClient;

    public シラバス(@Qualifier("学生JdbcClient作成") JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public 名前リスト 取得する() {

        String syllabusStatement = """
                    select teacher_name as 名前 from subjects
                """;

        List<名前> nameList = jdbcClient.sql(syllabusStatement)
                                    .query(名前.class)
                                    .list();

        return new 名前リスト(nameList);
    }
}
