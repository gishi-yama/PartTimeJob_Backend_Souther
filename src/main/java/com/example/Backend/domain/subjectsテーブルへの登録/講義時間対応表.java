package com.example.Backend.domain.subjectsテーブルへの登録;

import com.example.Backend.domain.subjectsテーブルへの登録.record.講義時間帯;

import java.time.LocalTime;
import java.util.Arrays;

public enum 講義時間対応表 {
    FIRST(1, LocalTime.of(9, 0), LocalTime.of(10, 30)),
    SECOND(2, LocalTime.of(10, 45), LocalTime.of(12, 15)),
    THIRD(3, LocalTime.of(13, 15), LocalTime.of(14, 45)),
    FOURTH(4, LocalTime.of(15, 0), LocalTime.of(16, 30)),
    FIFTH(5, LocalTime.of(16, 45), LocalTime.of(18, 15));

    private final int 時限;
    private final LocalTime 開始時間;
    private final LocalTime 終了時間;

    講義時間対応表(int 時限, LocalTime 開始時間, LocalTime 終了時間) {
        this.時限 = 時限;
        this.開始時間 = 開始時間;
        this.終了時間 = 終了時間;
    }

    // 1~5の数値から対応するenum定数を返すメソッド
    public static 講義時間帯 講義時間帯を得る (int 時限) {
        講義時間対応表 講義時間対応表 =   Arrays.stream(values())
                                    .filter(p -> p.時限 == 時限)
                                    .findFirst()//返り値は Optional<T> 型になる
                                    .orElseThrow(() -> new IllegalArgumentException("1から5の数値を指定してください: " + 時限));

        return new 講義時間帯(講義時間対応表.get開始時間(), 講義時間対応表.get終了時間());
    }

    // ゲッターメソッド
    public LocalTime get開始時間() {
        return 開始時間;
    }

    public LocalTime get終了時間() {
        return 終了時間;
    }
}
