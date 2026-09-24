package com.example.Backend.infrastracture.認証;

import com.example.Backend.domain.認証.*;
import com.example.Backend.domain.認証.教員ID;
import com.example.Backend.domain.追加業務検知.record.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class 認証サーバ {
    private JdbcClient jdbcClient;

    public 認証サーバ(@Qualifier("教員JdbcClient作成") JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public 認証結果 認証する(教員ID 教員ID, 教員パスワード 教員パスワード) {
        String verifyStatement = """
                    select true from teachers
                      where teacher_id = :id and teacher_password = :pass
                """;

        var 認証可否 = jdbcClient.sql(verifyStatement)
                .param("id", 教員ID.教員ID())
                .param("pass", 教員パスワード.教員パスワード())
                .query(Boolean.class)
                .list();

        // 認証に成功した場合
        if (!認証可否.isEmpty()) {
            String searchStatement = """
                        select * from teachers
                            where teacher_id = :id and teacher_password = :pass
                    """;

            return jdbcClient.sql(searchStatement)
                    .param("id", 教員ID.教員ID())
                    .param("pass", 教員パスワード.教員パスワード())
                    .query((resultSet, rowNumber) -> new 認証結果(
                            new 認証可否(true),
                            教員ID,
                            new 教員名(resultSet.getString("teacher_name")),
                            new 教員タイプ(resultSet.getString("teacher_type"))))
                    .single();
        } else {
            return new 認証結果(new 認証可否(false), null, null, null);
        }
    }

}
