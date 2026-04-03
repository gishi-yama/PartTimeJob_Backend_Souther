package com.example.Backend.domain;

import com.example.Backend.domain.specification.時間帯;

import java.time.LocalDate;
import java.time.LocalTime;

public class アルバイト実施報告書 implements アルバイト実施報告書インターフェース {
    @Override
    public アルバイト情報 取得する(選択した科目のデータ 選択した科目のデータ){ // 2
        時間帯 労働時間帯 = new 時間帯(LocalTime.of(9,0,0), LocalTime.of(12,15,0));
        LocalDate アルバイトの日にち = LocalDate.of(2026,4,7);
        String 業務内容 = "授業補助";

        return new アルバイト情報(労働時間帯, アルバイトの日にち, 業務内容);
    }
}
