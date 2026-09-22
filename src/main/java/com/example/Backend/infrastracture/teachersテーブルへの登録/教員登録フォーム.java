package com.example.Backend.infrastracture.teachersテーブルへの登録;

import com.example.Backend.domain.teachersテーブルへの登録.record.教員アカウント;
import com.example.Backend.domain.teachersテーブルへの登録.record.教員アカウントリスト;
import com.example.Backend.domain.teachersテーブルへの登録.教員登録フォームインターフェース;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class 教員登録フォーム implements 教員登録フォームインターフェース {
    private JdbcClient jdbcClient;

    public 教員登録フォーム(@Qualifier("教員JdbcClient作成") JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void 登録する(教員アカウントリスト 教員アカウントリスト) {
        String registerStatement = """
                    insert into teachers (teacher_id, teacher_name, teacher_password, teacher_type) values (:id, :name, :password, :type)
                """;

        // ADMIN権限のユーザを登録
        jdbcClient.sql(registerStatement)
                .param("id", "ADMIN")
                .param("name", "ADMIN")
                .param("password", "ADMIN")
                .param("type", "ADMIN")
                .update();

        // 一般教員を登録
        for (教員アカウント 教員アカウント : 教員アカウントリスト.教員アカウントリスト()) {
            jdbcClient.sql(registerStatement)
                    .param("id", 教員アカウント.名前().名前())
                    .param("name", 教員アカウント.名前().名前())
                    .param("password", 教員アカウント.パスワード().パスワード())
                    .param("type", "USER")
                    .update();
        }
    }

    @Override
    public void 削除する() {
        String deleteStatement = """
                    delete from teachers
                """;

        jdbcClient.sql(deleteStatement)
                .update();
    }
}
