package com.example.Backend.infrastracture.追加業務検知souther;

import com.example.Backend.souther.追加業務検知.アルバイト実施報告;
import com.example.Backend.souther.追加業務検知.アルバイト実施報告書を取得する;
import com.example.Backend.souther.追加業務検知.学生の名前;
import com.example.Backend.souther.追加業務検知.学籍番号;
import com.example.Backend.souther.追加業務検知.業務内容;
import com.example.Backend.souther.追加業務検知.科目名;
import net.unit8.raoh.decode.Decoder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static net.unit8.raoh.decode.map.MapDecoders.combine;
import static net.unit8.raoh.decode.map.MapDecoders.field;
import static net.unit8.raoh.decode.map.MapDecoders.flat;

/**
 * Souther の注入 behavior {@code アルバイト実施報告書を取得する} の Java 実装。
 * SQL は元の {@code infrastracture/追加業務検知/アルバイト実施報告書} と同じ。
 *
 * <p>【Raoh】行の読み込みは Raoh の {@code combine} で宣言的に書く。部品は Souther が生成した Decoder。
 * 不正な行があっても 1 件目で例外にせず、全行の問題を「何行目のどの列か」付きで集めてから止める。
 */
@Repository
public class Southerアルバイト実施報告書 extends アルバイト実施報告書を取得する {

    private final JdbcClient jdbcClient;

    /** ptj_requests の 1 行 → アルバイト実施報告。各列の検査は Souther の型の Decoder が行う。 */
    final Decoder<Map<String, Object>, アルバイト実施報告> 報告の行 = combine(
            field("学籍番号", 学籍番号.decoder()),       // Souther: invariant 空でない
            field("学生の名前", 学生の名前.decoder()),   // Souther: invariant 空でない
            field("アルバイトの日にち", JDBCの値.日付),  // Raoh: java.sql.Date → LocalDate
            flat(JDBCの値.時間帯("開始時間", "終了時間")), // Raoh で 2 列を束ね、Souther: invariant 開始は終了より前
            field("業務内容", 業務内容.decoder())
    ).map(this::アルバイト実施報告); // 生成された基底クラスの protected ファクトリ

    public Southerアルバイト実施報告書(@Qualifier("学生JdbcClient作成") JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<アルバイト実施報告> apply(科目名 科目) {
        int 科目のid = jdbcClient.sql("""
                        select id from subjects
                            where name = ?
                        """)
                .params(科目.value())
                .query(Integer.class)
                .single();

        List<Map<String, Object>> 行 = jdbcClient.sql("""
                        select user_student_number as 学籍番号,
                               name as 学生の名前,
                               ptj_date as アルバイトの日にち,
                               start_time as 開始時間,
                               finish_time as 終了時間,
                               duties as 業務内容
                        from ptj_requests
                        inner join users on user_student_number = student_number
                        where subject_id = ?
                        """)
                .params(科目のid)
                .query()
                .listOfRows();

        // 【Raoh】Decoder.list() で全行を読む。失敗は /行番号/列名 のパス付きで集まる
        return JDBCの値.読めなければ止める(報告の行.list().decode(行), "ptj_requests の行");
    }
}
