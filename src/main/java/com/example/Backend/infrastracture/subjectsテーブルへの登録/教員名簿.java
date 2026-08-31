package com.example.Backend.infrastracture.subjectsテーブルへの登録;

import com.example.Backend.domain.subjectsテーブルへの登録.record.教員IDあり講義情報リスト;
import com.example.Backend.domain.subjectsテーブルへの登録.record.講義情報リスト;
import com.example.Backend.domain.subjectsテーブルへの登録.教員名簿インターフェース;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class 教員名簿 implements 教員名簿インターフェース {
    private JdbcClient jdbcClient;

    public 教員名簿(@Qualifier("教員JdbcClient作成") JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public 教員IDあり講義情報リスト 取得する(講義情報リスト 講義情報リスト){
        String facultyListStatement = """
                    select teacher_number as 教員ID,
                           teacher_name as 教員名
                    from teachers
                """;

        Map<Integer, String> facultyList = new LinkedHashMap<>();

        jdbcClient.sql(facultyListStatement)
                                .query()
                                .listOfRows()
                                .forEach(row ->
                                        facultyList.put(
                                                (Integer) row.get("id"),
                                                (String) row.get("name")
                                        )
                                );
    }
}
