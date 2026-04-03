package com.example.Backend.domain;

import com.example.Backend.domain.specification.時間帯;
import java.time.LocalDate;

public record アルバイト情報(時間帯 労働時間帯, LocalDate アルバイトの日にち, String 業務内容) {
}
