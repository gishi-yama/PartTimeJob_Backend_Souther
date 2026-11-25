package com.example.Backend.souther;

import com.example.Backend.souther.追加業務検知.*;
import org.junit.jupiter.api.Test;
import souther.runtime.InvariantFailure;
import souther.runtime.Result;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Souther が生成したクラスを Java から使う例（DB なし）。
 * 分割ロジック自体は .sou の example でコンパイル時に検査済みなので、ここでは「Java からどう呼ぶか」だけを確かめる。
 */
class 追加業務検知SoutherJava連携Test {

    static <T> T ok(Result<T, InvariantFailure> r) {
        return ((Result.Ok<T, InvariantFailure>) r).value();
    }

    static 時間帯 時間帯(String 開始, String 終了) {
        return ok(com.example.Backend.souther.追加業務検知.時間帯.__construct(LocalTime.parse(開始), LocalTime.parse(終了)));
    }

    /** 注入 behavior の Java 実装（テスト用のメモリ版）。生成された抽象基底クラスを継承する。 */
    static class メモリ時間割 extends 時間割から講義情報を取得する {
        @Override
        public 時間割から講義情報を取得するResult apply(科目名 科目) {
            if (!科目.value().equals("プログラミング演習")) {
                return 時間割に科目がない(科目);
            }
            曜日 水 = 曜日.decoder().decode("水").getOrThrow();
            return 講義情報(水, 時間帯("10:40", "12:10"));
        }
    }

    static class メモリ報告書 extends アルバイト実施報告書を取得する {
        @Override
        public List<アルバイト実施報告> apply(科目名 科目) {
            return List.of(アルバイト実施報告(
                    ok(学籍番号.__construct("b2240001")),
                    ok(学生の名前.__construct("千歳 太郎")),
                    LocalDate.of(2026, 11, 25), // 水曜日
                    時間帯("10:00", "13:00"),
                    ok(業務内容.__construct("演習補助"))));
        }
    }

    final 追加業務を検知する 検知 = 追加業務を検知する.bind(new メモリ時間割(), new メモリ報告書());

    @Test
    void 講義の前後にはみ出した労働は3つの時間帯に分割される() {
        var 結果 = 検知.apply(ok(科目名.__construct("プログラミング演習")));

        var 一覧 = assertInstanceOf(追加業務検知の結果一覧.class, 結果);
        var 区分 = assertInstanceOf(講義の前後にはみ出した労働.class, 一覧.value().getFirst().区分());
        assertEquals(時間帯("10:00", "10:40"), 区分.前の追加労働時間帯());
        assertEquals(時間帯("10:40", "12:10"), 区分.通常労働時間帯());
        assertEquals(時間帯("12:10", "13:00"), 区分.後の追加労働時間帯());
    }

    @Test
    void 時間割に無い科目は例外ではなく結果のcaseとして返る() {
        var 結果 = 検知.apply(ok(科目名.__construct("存在しない科目")));
        assertInstanceOf(時間割に科目がない.class, 結果);
    }

    @Test
    void 開始が終了より後の時間帯はJavaからも作れない() {
        assertTrue(com.example.Backend.souther.追加業務検知.時間帯.__construct(LocalTime.parse("11:00"), LocalTime.parse("10:00")).isErr());
        assertTrue(com.example.Backend.souther.追加業務検知.時間帯.__construct(LocalTime.parse("10:40"), LocalTime.parse("10:40")).isErr());
    }

    @Test
    void 空の科目名はDecoderで弾かれる() {
        assertTrue(科目名.decoder().decode("").isErr());
    }

    @Test
    void 結果はEncoderでJSONにそのまま出せる形になる() {
        var 一覧 = (追加業務検知の結果一覧) 検知.apply(ok(科目名.__construct("プログラミング演習")));
        Object json = 追加業務検知の結果一覧.encoder().encode(一覧);

        var 先頭 = assertInstanceOf(Map.class, ((List<?>) json).getFirst());
        var 区分 = assertInstanceOf(Map.class, 先頭.get("区分"));
        assertEquals("講義の前後にはみ出した労働", 区分.get("type"));
        assertEquals("水", 先頭.get("曜日"));
    }
}
