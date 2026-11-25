package com.example.Backend.infrastracture.追加業務検知souther;

import com.example.Backend.souther.追加業務検知.時間割から講義情報を取得する;
import com.example.Backend.souther.追加業務検知.時間割から講義情報を取得するResult;
import com.example.Backend.souther.追加業務検知.時間帯;
import com.example.Backend.souther.追加業務検知.曜日;
import com.example.Backend.souther.追加業務検知.科目名;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Time;

/**
 * Souther の注入 behavior {@code 時間割から講義情報を取得する} の Java 実装。
 *
 * <p>元の {@code infrastracture/追加業務検知/時間割} と同じ SQL を使う。違いは 2 点:
 * <ul>
 *   <li>科目が見つからないとき、{@code .single()} の例外ではなく {@code 時間割に科目がない} を返す
 *       （出力の型がそれを要求するので、返し忘れはコンパイルエラーになる）。</li>
 *   <li>曜日・時間帯は Souther の型に入れる時点で検査される（曜日は 7 つの case のどれか、時間帯は 開始 &lt; 終了）。</li>
 * </ul>
 */
@Repository
public class Souther時間割 extends 時間割から講義情報を取得する {

    private record 時間割の行(String 講義の曜日, Time 開始時間, Time 終了時間) {
    }

    private final JdbcClient jdbcClient;

    public Souther時間割(@Qualifier("教員JdbcClient作成") JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public 時間割から講義情報を取得するResult apply(科目名 科目) {
        var 行 = jdbcClient.sql("""
                        select day_of_the_week as 講義の曜日,
                               scheduled_start_time as 開始時間,
                               scheduled_finish_time as 終了時間
                        from subjects
                            where subject_name = ?
                        """)
                .params(科目.value())
                .query((rs, n) -> new 時間割の行(rs.getString("講義の曜日"), rs.getTime("開始時間"), rs.getTime("終了時間")))
                .optional();

        if (行.isEmpty()) {
            return 時間割に科目がない(科目); // 生成された基底クラスの protected ファクトリ
        }
        var r = 行.get();
        曜日 講義の曜日 = Souther値.検査して取り出す(曜日.decoder().decode(r.講義の曜日()), "講義の曜日");
        時間帯 講義時間帯 = Souther値.検査して取り出す(
                時間帯.__construct(r.開始時間().toLocalTime(), r.終了時間().toLocalTime()), "講義時間帯");
        return 講義情報(講義の曜日, 講義時間帯); // 生成された基底クラスの protected ファクトリ
    }
}
