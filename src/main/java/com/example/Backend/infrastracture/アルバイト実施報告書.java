package com.example.Backend.infrastracture;

import com.example.Backend.domain.追加業務検知.record.*;
import com.example.Backend.domain.追加業務検知.アルバイト実施報告書インターフェース;
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
        String subjectsStatement = """
                    select subject_id from subjects
                        where subject_name = ?
                """;


        String timeStatement = """
                    select work_start_time, work_finish_time from ptj_requests
                        where subject_id = ?
                """;

        String dayStatement = """
                    select ptj_date from ptj_requests
                        where subject_id = ?
                """;

        String dutiesStatement = """
                    select duties from ptj_requests
                        where subject_id = ?
                """;

        int 科目のid = jdbcClient.sql(subjectsStatement)
                .params(選択した科目のデータ)
                .query(Integer.class)
                .single();

        List<時間帯> 労働時間帯 = jdbcClient.sql(timeStatement)
                .params(科目のid)
                .query(時間帯.class)
                .list();

        List<アルバイトの日にち> アルバイトの日にち = jdbcClient.sql(dayStatement)
                .params(科目のid)
                .query(アルバイトの日にち.class)
                .list();

        List<業務内容> 業務内容 = jdbcClient.sql(dutiesStatement)
                .params(科目のid)
                .query(業務内容.class)
                .list();

        ArrayList<アルバイト情報> アルバイト情報 = new ArrayList<>();

        for(int i=0;i<労働時間帯.size();i++){
            アルバイト情報.add(new アルバイト情報(労働時間帯.get(i),アルバイトの日にち.get(i),業務内容.get(i)));
        }
        return アルバイト情報.get(0);
    }
}
