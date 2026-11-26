package com.example.Backend.infrastracture.追加業務検知souther;

import com.example.Backend.souther.追加業務検知.時間帯;
import net.unit8.raoh.Issues;
import net.unit8.raoh.Result;
import net.unit8.raoh.decode.Decoder;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import static net.unit8.raoh.decode.map.MapDecoders.combine;
import static net.unit8.raoh.decode.map.MapDecoders.field;

/**
 * 【Raoh】JDBC の行（{@code Map<String, Object>}）を Souther の型へ読み込むための部品。
 *
 * <p>役割分担:
 * <ul>
 *   <li>Raoh（ここ）: DB の列名・JDBC の型（{@code java.sql.Time} など）を読み替える。境界の「形」の問題。</li>
 *   <li>Souther の Decoder: 読み替えた値が業務ルール（invariant）を満たすかを検査する。モデルの「意味」の問題。</li>
 * </ul>
 * Souther が生成する {@code 型.decoder()} は Raoh の {@link Decoder} そのものなので、Raoh の {@code combine} の部品として混ぜて使える。
 */
final class JDBCの値 {

    private JDBCの値() {
    }

    /** JDBC の {@code java.sql.Time} を {@link LocalTime} に。Souther の Time の Decoder は java.sql.Time を受け付けないため。 */
    static final Decoder<Object, LocalTime> 時刻 = (in, path) -> switch (in) {
        case java.sql.Time t -> Result.ok(t.toLocalTime());
        case LocalTime t -> Result.ok(t);
        case null -> Result.fail(path, "required", "is required");
        default -> Result.fail(path, "type_mismatch", "expected java.sql.Time but was " + in.getClass().getSimpleName());
    };

    /** JDBC の {@code java.sql.Date} を {@link LocalDate} に。 */
    static final Decoder<Object, LocalDate> 日付 = (in, path) -> switch (in) {
        case java.sql.Date d -> Result.ok(d.toLocalDate());
        case LocalDate d -> Result.ok(d);
        case null -> Result.fail(path, "required", "is required");
        default -> Result.fail(path, "type_mismatch", "expected java.sql.Date but was " + in.getClass().getSimpleName());
    };

    /**
     * 2 つの列から Souther の {@code 時間帯} を作る。
     * 列の読み替えは Raoh、{@code invariant 開始は終了より前} の検査は Souther の {@code 時間帯.decoder()} に任せる。
     */
    static Decoder<Map<String, Object>, 時間帯> 時間帯(String 開始の列, String 終了の列) {
        return combine(field(開始の列, 時刻), field(終了の列, 時刻))
                .map((開始, 終了) -> Map.<String, Object>of("開始", 開始, "終了", 終了))
                .pipe(時間帯.decoder());
    }

    /**
     * 読み込みに失敗した行があれば止める。Raoh は全行の問題をパス付きで集めて返すので、
     * 例外のメッセージには「何行目のどの列が、なぜ駄目か」がすべて入る（例: {@code /3/学籍番号: must be at least 1 characters}）。
     */
    static <T> T 読めなければ止める(Result<T> 読み込み結果, String 何を) {
        return 読み込み結果.orElseThrow((Issues 問題) ->
                new DataIntegrityViolationException(何を + " を Souther の型に読み込めません: " + 問題.toJsonList()));
    }
}
