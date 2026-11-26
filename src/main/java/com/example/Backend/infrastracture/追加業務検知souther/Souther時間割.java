package com.example.Backend.infrastracture.追加業務検知souther;

import com.example.Backend.souther.追加業務検知.時間割から講義情報を取得する;
import com.example.Backend.souther.追加業務検知.時間割から講義情報を取得するResult;
import com.example.Backend.souther.追加業務検知.曜日;
import com.example.Backend.souther.追加業務検知.科目名;
import com.example.Backend.souther.追加業務検知.講義情報;
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
 * Souther の注入 behavior {@code 時間割から講義情報を取得する} の Java 実装。
 *
 * <p>元の {@code infrastracture/追加業務検知/時間割} と同じ SQL を使う。違いは 2 点:
 * <ul>
 *   <li>科目が見つからないとき、{@code .single()} の例外ではなく {@code 時間割に科目がない} を返す
 *       （出力の型がそれを要求するので、返し忘れはコンパイルエラーになる）。</li>
 *   <li>【Raoh】行は Raoh の {@code combine} で読む。曜日は 7 つの case のどれか、時間帯は 開始 &lt; 終了 を
 *       Souther の Decoder が検査し、両方の問題をまとめて報告する。</li>
 * </ul>
 */
@Repository
public class Souther時間割 extends 時間割から講義情報を取得する {

    private final JdbcClient jdbcClient;

    /** subjects の 1 行 → 講義情報。 */
    final Decoder<Map<String, Object>, 講義情報> 時間割の行 = combine(
            field("講義の曜日", 曜日.decoder()),            // Souther: 月〜日 のどれか（"水曜" は弾かれる）
            flat(JDBCの値.時間帯("開始時間", "終了時間"))   // Souther: invariant 開始は終了より前
    ).map(this::講義情報); // 生成された基底クラスの protected ファクトリ

    public Souther時間割(@Qualifier("教員JdbcClient作成") JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public 時間割から講義情報を取得するResult apply(科目名 科目) {
        List<Map<String, Object>> 行 = jdbcClient.sql("""
                        select day_of_the_week as 講義の曜日,
                               scheduled_start_time as 開始時間,
                               scheduled_finish_time as 終了時間
                        from subjects
                            where subject_name = ?
                        """)
                .params(科目.value())
                .query()
                .listOfRows();

        if (行.isEmpty()) {
            return 時間割に科目がない(科目); // 生成された基底クラスの protected ファクトリ
        }
        return JDBCの値.読めなければ止める(時間割の行.decode(行.getFirst()), "subjects の行");
    }
}
