package com.example.Backend.presentation;

import com.example.Backend.infrastracture.追加業務検知souther.Southerアルバイト実施報告書;
import com.example.Backend.infrastracture.追加業務検知souther.Souther時間割;
import com.example.Backend.souther.追加業務検知.時間割に科目がない;
import com.example.Backend.souther.追加業務検知.科目名;
import com.example.Backend.souther.追加業務検知.追加業務を検知する;
import com.example.Backend.souther.追加業務検知.追加業務を検知するResult;
import com.example.Backend.souther.追加業務検知.追加業務検知の結果一覧;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Souther 版の追加業務検知 API。既存の {@code /結果表示}（Java 版）と並べて置き、結果を比較できるようにしている。
 *
 * <ul>
 *   <li>入力: 科目名は Souther の Decoder を通す（空文字は invariant 違反として 400）。</li>
 *   <li>処理: {@code 追加業務を検知する.bind(...)} で DB 実装を注入した behavior を呼ぶだけ。分割ロジックは全て .sou 側。</li>
 *   <li>出力: Souther の Encoder が JSON にそのまま出せる形（Map / List）に変換する。
 *       各結果は {@code "区分": {"type": "講義前にはみ出した労働", ...}} のように case 名を判別子として持つ。</li>
 * </ul>
 */
@RestController
public class 追加業務検知SoutherAPI {

    private final 追加業務を検知する 検知;

    public 追加業務検知SoutherAPI(Souther時間割 時間割, Southerアルバイト実施報告書 アルバイト実施報告書) {
        // 【Souther: depends on】behavior の依存は bind の引数として渡す
        this.検知 = 追加業務を検知する.bind(時間割, アルバイト実施報告書);
    }

    @PostMapping("/結果表示/souther")
    public ResponseEntity<Object> post(@RequestParam(name = "科目名", required = false) String 科目) {
        var 復号 = 科目名.decoder().decode(科目);
        if (復号.isErr()) {
            return ResponseEntity.badRequest().body(Map.of("error", "科目名が不正です", "detail", String.valueOf(復号)));
        }
        追加業務を検知するResult 結果 = 検知.apply(復号.getOrThrow());
        // 【Souther: 出力は bare sum】Java では sealed interface になるので switch で網羅できる
        return switch (結果) {
            case 追加業務検知の結果一覧 一覧 -> ResponseEntity.ok(追加業務検知の結果一覧.encoder().encode(一覧));
            case 時間割に科目がない なし -> ResponseEntity.status(404).body(時間割に科目がない.encoder().encode(なし));
            default -> throw new IllegalStateException("想定外の結果: " + 結果);
        };
    }
}
