package com.example.Backend.infrastracture.subjectsテーブルへの登録;

import com.example.Backend.domain.subjectsテーブルへの登録.record.教員ID;
import com.example.Backend.domain.subjectsテーブルへの登録.record.教員名;
import com.example.Backend.domain.subjectsテーブルへの登録.record.教員証;
import com.example.Backend.domain.subjectsテーブルへの登録.record.教員証リスト;
import com.example.Backend.domain.subjectsテーブルへの登録.教員名簿インターフェース;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class 教員名簿 implements 教員名簿インターフェース {
    private JdbcClient jdbcClient;

    public 教員名簿(@Qualifier("教員JdbcClient作成") JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public 教員証リスト 取得する(){
        String facultyListStatement = """
                    select teacher_id as 教員ID,
                           teacher_name as 教員名
                    from teachers
                """;

        List<教員証> 教員証リスト = jdbcClient.sql(facultyListStatement)
                        .query((resultSet, rowNumber) -> new 教員証(new 教員ID(resultSet.getString("教員ID")),
                                                                               new 教員名(resultSet.getString("教員名"))))
                        .list();

        return new 教員証リスト(教員証リスト);
    }
}
