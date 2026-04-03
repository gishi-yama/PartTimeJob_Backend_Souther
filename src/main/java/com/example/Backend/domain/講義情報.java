package com.example.Backend.domain;

import com.example.Backend.domain.specification.時間帯;

public record 講義情報(時間帯 講義時間帯, String 講義の曜日) {
}
