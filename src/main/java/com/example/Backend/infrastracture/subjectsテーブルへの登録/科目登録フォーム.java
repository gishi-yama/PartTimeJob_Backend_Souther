package com.example.Backend.infrastracture.subjectsテーブルへの登録;

import com.example.Backend.domain.subjectsテーブルへの登録.record.日時付き講義情報;
import com.example.Backend.domain.subjectsテーブルへの登録.record.日時付き講義情報リスト;
import com.example.Backend.domain.subjectsテーブルへの登録.科目登録フォームインターフェース;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class 科目登録フォーム implements 科目登録フォームインターフェース {
    private JdbcClient jdbcClient;

    public 科目登録フォーム(@Qualifier("教員JdbcClient作成") JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void 登録する(日時付き講義情報リスト 日時付き講義情報リスト){
        String registerStatement = """
                    insert into subjects values (:subject_id, :subject_name, :teacher_number, :day_of_the_week, :scheduled_start_time, :scheduled_finish_time)
                """;

        for (日時付き講義情報 日時付き講義情報 : 日時付き講義情報リスト.日時付き講義情報リスト()) {
            jdbcClient.sql(registerStatement)
                    .param("subject_id", 日時付き講義情報.科目ID().科目ID())
                    .param("subject_name", 日時付き講義情報.科目名().科目名())
                    .param("teacher_number", 日時付き講義情報.教員ID().教員ID())
                    .param("day_of_the_week", 日時付き講義情報.講義の曜日().講義の曜日())
                    .param("scheduled_start_time", 日時付き講義情報.講義時間帯().開始時間())
                    .param("scheduled_finish_time", 日時付き講義情報.講義時間帯().終了時間())
                    .update();
        }
    }
}
