package com.example.Backend.presentation;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 【Raoh × Souther】試算 API のテスト（DB なし）。本文は Spring が JSON から作るのと同じ Map / List / String で渡す。
 */
class 追加業務検知試算APITest {

    final 追加業務検知試算API api = new 追加業務検知試算API();

    static Map<String, Object> 講義(String 曜日) {
        return Map.of("講義の曜日", 曜日, "講義時間帯", Map.of("開始", "10:40", "終了", "12:10"));
    }

    static Map<String, Object> 報告(String 学籍番号, String 開始, String 終了) {
        var 報告 = new HashMap<String, Object>();
        報告.put("学籍番号", 学籍番号);
        報告.put("学生の名前", "千歳 太郎");
        報告.put("日にち", "2026-11-25"); // 水曜日
        報告.put("労働時間帯", Map.of("開始", 開始, "終了", 終了));
        報告.put("業務内容", "演習補助");
        return 報告;
    }

    @SuppressWarnings("unchecked")
    static List<String> 問題のパス(Object 本文) {
        var 問題 = (List<Map<String, Object>>) ((Map<String, Object>) 本文).get("問題");
        return 問題.stream().map(i -> i.get("path") + " " + i.get("code")).toList();
    }

    @Test
    @SuppressWarnings("unchecked")
    void 正しい入力は分割結果を返す() {
        var 応答 = api.試算(Map.of("講義", 講義("水"), "報告", List.of(報告("b2240001", "10:00", "13:00"))));

        assertEquals(200, 応答.getStatusCode().value());
        var 結果 = (List<Map<String, Object>>) 応答.getBody();
        var 区分 = (Map<String, Object>) 結果.getFirst().get("区分");
        assertEquals("講義の前後にはみ出した労働", 区分.get("type"));
        assertEquals("水", 結果.getFirst().get("曜日"));
    }

    @Test
    void Java版の不具合入力は全部まとめてパス付きの400になる() {
        var 応答 = api.試算(Map.of(
                "講義", 講義("水曜"),                           // 曜日の表記ゆれ
                "報告", List.of(
                        報告("b2240001", "10:40", "10:40"),     // 長さ 0
                        報告("b2240002", "10:00", "11:00"),     // 正しい
                        報告("", "11:30", "11:00"))));          // 学籍番号が空、開始と終了が逆

        assertEquals(400, 応答.getStatusCode().value());
        assertEquals(List.of(
                        "/講義/講義の曜日 not_allowed",
                        "/報告/0/労働時間帯 invariant_violation",
                        "/報告/2/学籍番号 too_short",
                        "/報告/2/労働時間帯 invariant_violation"),
                問題のパス(応答.getBody()));
    }

    @Test
    void 必須の項目が無ければrequiredになる() {
        var 応答 = api.試算(Map.of("講義", 講義("水")));
        assertEquals(400, 応答.getStatusCode().value());
        assertEquals(List.of("/報告 required"), 問題のパス(応答.getBody()));
    }
}
