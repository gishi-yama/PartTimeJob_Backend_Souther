package com.example.Backend.infrastracture;

import com.example.Backend.domain.追加業務検知.record.アルバイトの日にち;
import com.example.Backend.domain.追加業務検知.record.アルバイト情報;
import com.example.Backend.domain.追加業務検知.record.時間帯;
import com.example.Backend.domain.追加業務検知.record.業務内容;
import com.example.Backend.domain.追加業務検知.アルバイト実施報告書インターフェース;
import com.example.Backend.domain.追加業務検知.選択した科目のデータ;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class アルバイト実施報告書 implements アルバイト実施報告書インターフェース {

    private JdbcClient jdbcClient;

    public アルバイト実施報告書(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public アルバイト情報 取得する(選択した科目のデータ 選択した科目のデータ){ // 2
        String timeStatement = """
                    select work_start_time, work_finish_time from ptj_requests
                        where subject_name = 選択した科目のデータ
                """;

        String dayStatement = """
                    select ptj_date from ptj_requests
                        where subject_name = 選択した科目のデータ
                """;

        String dutiesStatement = """
                    select duties from ptj_requests
                        where subject_name = 選択した科目のデータ
                """;

        List<時間帯> 労働時間帯 = jdbcClient.sql(timeStatement)
                .query(時間帯.class)
                .list();

        List<アルバイトの日にち> アルバイトの日にち = jdbcClient.sql(dayStatement)
                .query(アルバイトの日にち.class)
                .list();

        List<業務内容> 業務内容 = jdbcClient.sql(dutiesStatement)
                .query(業務内容.class)
                .list();

        ArrayList<アルバイト情報> アルバイト情報 = new ArrayList<>();

        for(int i=0;i<労働時間帯.size();i++){
            アルバイト情報.add(new アルバイト情報(労働時間帯.get(i),アルバイトの日にち.get(i),業務内容.get(i)));
        }
        return アルバイト情報.get(0);
    }
}
