package com.example.Backend.presentation;

import com.example.Backend.souther.追加業務検知.アルバイト実施報告;
import com.example.Backend.souther.追加業務検知.一件を検知する;
import com.example.Backend.souther.追加業務検知.追加業務検知の結果;
import com.example.Backend.souther.追加業務検知.講義情報;
import net.unit8.raoh.decode.Decoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static net.unit8.raoh.decode.ObjectDecoders.list;
import static net.unit8.raoh.decode.map.MapDecoders.combine;
import static net.unit8.raoh.decode.map.MapDecoders.field;
import static net.unit8.raoh.decode.map.MapDecoders.nested;

/**
 * 【Raoh × Souther】DB を使わずに、講義と報告を JSON で渡して分割結果を試算する API。
 *
 * <pre>
 * POST /結果表示/souther/試算
 * {
 *   "講義": {"講義の曜日": "水", "講義時間帯": {"開始": "10:40", "終了": "12:10"}},
 *   "報告": [{"学籍番号": "b2240001", "学生の名前": "千歳 太郎", "日にち": "2026-11-25",
 *            "労働時間帯": {"開始": "10:00", "終了": "13:00"}, "業務内容": "演習補助"}]
 * }
 * </pre>
 *
 * <ul>
 *   <li>リクエストの外枠（{@code 講義} と {@code 報告} の配列）は Raoh の {@code combine} / {@code list} / {@code nested} で読む。</li>
 *   <li>中身は Souther が生成した {@code 講義情報.decoder()} / {@code アルバイト実施報告.decoder()} がそのまま読む。
 *       invariant（空でない、開始は終了より前）と sum（曜日は 7 つのどれか）は、ここで検査される。</li>
 *   <li>誤りは最初の 1 件で止めず全部集め、{@code /報告/2/労働時間帯} のような JSON Pointer 付きで 400 を返す。</li>
 * </ul>
 */
@RestController
public class 追加業務検知試算API {

    record 試算の入力(講義情報 講義, List<アルバイト実施報告> 報告) {
    }

    static final Decoder<Map<String, Object>, 試算の入力> 試算の入力 = combine(
            field("講義", nested(講義情報.decoder())),
            field("報告", list(nested(アルバイト実施報告.decoder())))
    ).map(試算の入力::new);

    private final 一件を検知する 一件 = 一件を検知する.of();

    @PostMapping("/結果表示/souther/試算")
    public ResponseEntity<Object> 試算(@RequestBody Map<String, Object> 本文) {
        return 試算の入力.decode(本文).fold(
                入力 -> {
                    var 結果 = 入力.報告().stream()
                            .map(報告 -> 一件.apply(入力.講義(), 報告))
                            .map(追加業務検知の結果.encoder()::encode)
                            .toList();
                    return ResponseEntity.ok(結果);
                },
                問題 -> ResponseEntity.badRequest().body(Map.of("問題", 問題.toJsonList())));
    }
}
