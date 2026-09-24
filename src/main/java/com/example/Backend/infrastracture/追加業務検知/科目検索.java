package com.example.Backend.infrastracture.追加業務検知;

import com.example.Backend.domain.追加業務検知.record.教員ID;
import com.example.Backend.domain.追加業務検知.record.権限;
import com.example.Backend.domain.追加業務検知.record.科目名リスト;
import com.example.Backend.domain.追加業務検知.科目検索インターフェース;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class 科目検索 implements 科目検索インターフェース {

    private JdbcClient jdbcClient;

    public 科目検索(@Qualifier("教員JdbcClient作成")JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public 科目名リスト 取得する(教員ID 教員ID, 権限 権限) {// 2


        List<String> 科目名リスト;

        if(権限.権限().equals("ADMIN")){
            String subjectStatement = """
                select subject_name from subjects
                """;

            科目名リスト = jdbcClient.sql(subjectStatement)
                    .query(String.class)
                    .list();
        } else {
            String subjectStatement = """
                
                    select subject_name from subjects
                        where teacher_id = ?
   

               """;

            科目名リスト = jdbcClient.sql(subjectStatement)
                .params(教員ID.教員ID())
                .query(String.class)
                .list();
        }

        return new 科目名リスト(科目名リスト);
    }
}
