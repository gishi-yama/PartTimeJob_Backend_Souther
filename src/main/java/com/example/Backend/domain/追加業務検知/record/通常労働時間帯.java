package com.example.Backend.domain.追加業務検知.record;

import java.time.LocalTime;

public record 通常労働時間帯(LocalTime 開始時間, LocalTime 終了時間) {
}
