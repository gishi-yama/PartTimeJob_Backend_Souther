package com.example.Backend.infrastracture;

import com.example.Backend.domain.追加業務検知.record.*;
import com.example.Backend.domain.追加業務検知.アルバイト実施報告書インターフェース;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class アルバイト実施報告書 implements アルバイト実施報告書インターフェース {

    private JdbcClient jdbcClient;

    public アルバイト実施報告書(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<曜日なしアルバイト情報> 取得する(選択した科目のデータ 選択した科目のデータ) {// 2

        String subjectsStatement = """
                    select subject_id from subjects
                        where subject_name = ?
                """;

        int 科目のid = jdbcClient.sql(subjectsStatement)
                .params(選択した科目のデータ.科目名())
                .query(Integer.class)
                .single();

        String arubaitoStatement = """
                    select student_id as 学籍番号,
                           ptj_date as アルバイトの日にち,
                           work_start_time as 開始時間,
                           work_finish_time as 終了時間,
                           duties as 業務内容
                    from ptj_requests
                    where subject_id = ?
                """;

        return jdbcClient.sql(arubaitoStatement)
                .params(科目のid)
                .query((resultSet, rowNumber) -> new 曜日なしアルバイト情報(
                        new 学籍番号(resultSet.getString("学籍番号")),
                        new アルバイトの日にち(resultSet.getDate("アルバイトの日にち").toLocalDate()),
                        new 時間帯(resultSet.getTime("開始時間").toLocalTime(),
                                  resultSet.getTime("終了時間").toLocalTime()),
                        new 業務内容(resultSet.getString("業務内容"))))
                .list();
    }

}
