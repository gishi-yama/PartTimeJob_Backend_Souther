package com.example.Backend.infrastracture.追加業務検知souther;

import com.example.Backend.souther.追加業務検知.講義情報;
import net.unit8.raoh.Err;
import net.unit8.raoh.Issue;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 【Raoh】JDBC の行（{@code listOfRows()} が返す Map）を Souther の型に読み込む部分のテスト（DB なし）。
 * 行は JDBC が返すのと同じ {@code java.sql.Time} / {@code java.sql.Date} で作る。
 */
class DB行の読み込みTest {

    final Southerアルバイト実施報告書 報告書 = new Southerアルバイト実施報告書(null);
    final Souther時間割 時間割 = new Souther時間割(null);

    static Map<String, Object> 報告の行(String 学籍番号, String 開始, String 終了) {
        var 行 = new HashMap<String, Object>();
        行.put("学籍番号", 学籍番号);
        行.put("学生の名前", "千歳 太郎");
        行.put("アルバイトの日にち", Date.valueOf("2026-11-25"));
        行.put("開始時間", Time.valueOf(開始));
        行.put("終了時間", Time.valueOf(終了));
        行.put("業務内容", "演習補助");
        return 行;
    }

    static List<String> パスとコード(net.unit8.raoh.Result<?> 結果) {
        var err = assertInstanceOf(Err.class, 結果);
        return err.issues().asList().stream()
                .map(Issue.class::cast)
                .map(i -> i.path().toJsonPointer() + " " + i.code())
                .toList();
    }

    @Test
    void 正しい行はSoutherの型になる() {
        var 報告 = 報告書.報告の行.list().decode(List.of(報告の行("b2240001", "10:00:00", "13:00:00"))).getOrThrow().getFirst();

        assertEquals("b2240001", 報告.学籍番号().value());
        assertEquals(java.time.LocalDate.of(2026, 11, 25), 報告.日にち());
        assertEquals(java.time.LocalTime.of(10, 0), 報告.労働時間帯().開始());
    }

    @Test
    void 不正な行は1件目で止まらず全行の問題が行番号と列名付きで集まる() {
        var 行 = List.of(
                報告の行("b2240001", "10:00:00", "13:00:00"), // 0: 正しい
                報告の行("", "10:00:00", "11:00:00"),         // 1: 学籍番号が空
                報告の行("b2240003", "11:30:00", "11:00:00"), // 2: 開始と終了が逆
                報告の行("", "10:40:00", "10:40:00"));        // 3: 学籍番号が空 かつ 長さ 0

        assertEquals(List.of(
                        "/1/学籍番号 too_short",
                        "/2 invariant_violation",
                        "/3/学籍番号 too_short",
                        "/3 invariant_violation"),
                パスとコード(報告書.報告の行.list().decode(行)));
    }

    @Test
    void 読めない行があれば例外のメッセージに全ての問題が入る() {
        var 結果 = 報告書.報告の行.list().decode(List.of(報告の行("", "11:30:00", "11:00:00")));

        var 例外 = assertThrows(org.springframework.dao.DataIntegrityViolationException.class,
                () -> JDBCの値.読めなければ止める(結果, "ptj_requests の行"));
        assertTrue(例外.getMessage().contains("/0/学籍番号"), 例外.getMessage());
        assertTrue(例外.getMessage().contains("開始は終了より前"), 例外.getMessage());
    }

    @Test
    void 時間割の行は曜日と時間帯を同時に検査する() {
        var 正しい = Map.<String, Object>of("講義の曜日", "水", "開始時間", Time.valueOf("10:40:00"), "終了時間", Time.valueOf("12:10:00"));
        assertInstanceOf(講義情報.class, 時間割.時間割の行.decode(正しい).getOrThrow());

        // Java 版では "水曜" は「講義のない曜日」として素通りしていた
        var 不正 = Map.<String, Object>of("講義の曜日", "水曜", "開始時間", Time.valueOf("12:10:00"), "終了時間", Time.valueOf("10:40:00"));
        assertEquals(List.of("/講義の曜日 not_allowed", " invariant_violation"), パスとコード(時間割.時間割の行.decode(不正)));
    }

    @Test
    void JDBCの型でない値は型の不一致として報告される() {
        var 行 = 報告の行("b2240001", "10:00:00", "13:00:00");
        行.put("開始時間", "10:00"); // 文字列で来た
        assertEquals(List.of("/0/開始時間 type_mismatch"), パスとコード(報告書.報告の行.list().decode(List.of(行))));
    }
}
