package com.example.Backend.domain;

import com.example.Backend.domain.object.アルバイトの日にち;
import com.example.Backend.domain.object.アルバイトの曜日;

import java.time.format.TextStyle;
import java.util.Locale;

public class カレンダー {
    public アルバイトの曜日 曜日算出する(アルバイトの日にち アルバイトの日にち){ // 3.1
        // 曜日を英語で算出
        var dayOfWeek = アルバイトの日にち.アルバイトの日にち().getDayOfWeek();

        // 日本語の短縮表記（〇）を取得
        String 曜日 = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.JAPAN);
        return new アルバイトの曜日(曜日);
    }
}
