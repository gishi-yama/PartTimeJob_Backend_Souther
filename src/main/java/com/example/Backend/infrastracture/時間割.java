package com.example.Backend.infrastracture;

import com.example.Backend.domain.追加業務検知.record.時間帯;
import com.example.Backend.domain.追加業務検知.record.講義の曜日;
import com.example.Backend.domain.追加業務検知.record.講義情報;
import com.example.Backend.domain.追加業務検知.record.選択した科目のデータ;
import com.example.Backend.domain.追加業務検知.時間割インターフェース;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class 時間割 implements 時間割インターフェース {

    // Spring Frameworkのデータベース接続ライブラリを利用する
    private JdbcClient jdbcClient;

    // Spring Frameworkのデータベース接続ライブラリを初期化する
    public 時間割(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public 講義情報 取得する(選択した科目のデータ 選択した科目のデータ) { // 1
        String timeStatement = """
                    select scheduled_start_time as 開始時間, 
                           scheduled_finish_time as 終了時間 
                    from subjects
                        where subject_name = ?
                """;

        String weekStatement = """
                    select day_of_week as 講義の曜日
                    from subjects
                        where subject_name = ?
                """;

        時間帯 講義時間帯 = jdbcClient.sql(timeStatement)
                .params(選択した科目のデータ.科目名())
                .query(時間帯.class)
                .single();

        講義の曜日 講義の曜日 = jdbcClient.sql(weekStatement)
                .params(選択した科目のデータ.科目名())
                .query(講義の曜日.class)
                .single();

        return new 講義情報(講義時間帯, 講義の曜日);
    }

}