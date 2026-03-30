package com.example.Backend.domain.specification;

import java.time.LocalTime;

public class 時間帯 {
    private LocalTime 開始時間;
    private LocalTime 終了時間;

    public 時間帯(LocalTime 開始時間, LocalTime 終了時間) {
        this.開始時間 = 開始時間;
        this.終了時間 = 終了時間;
    }
}
