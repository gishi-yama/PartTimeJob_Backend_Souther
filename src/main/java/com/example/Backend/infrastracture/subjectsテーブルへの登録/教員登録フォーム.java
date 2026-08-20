package com.example.Backend.infrastracture.subjectsテーブルへの登録;

import com.example.Backend.domain.teachersテーブルへの登録.record.教員アカウント;
import com.example.Backend.domain.teachersテーブルへの登録.教員登録フォームインターフェース;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class 教員登録フォーム implements 教員登録フォームインターフェース {
    private JdbcClient jdbcClient;

    public 教員登録フォーム(@Qualifier("教員JdbcClient作成")JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void 登録する(教員アカウント 教員アカウント) {
        String registerStatement = """
                    insert into teachers (teacher_name, teacher_password) values (:name, :password)
                """;

        jdbcClient.sql(registerStatement)
                .param("name", 教員アカウント.名前())
                .param("password", 教員アカウント.パスワード())
                .update();
    }
}
