package com.example.Backend.domain;

import com.example.Backend.domain.object.アルバイトの日にち;
import com.example.Backend.domain.object.アルバイト情報;
import com.example.Backend.domain.object.時間帯;
import com.example.Backend.domain.object.業務内容;

import java.time.LocalDate;
import java.time.LocalTime;

public class アルバイト実施報告書 implements アルバイト実施報告書インターフェース {
    @Override
    public アルバイト情報 取得する(選択した科目のデータ 選択した科目のデータ){ // 2
        時間帯 労働時間帯 = new 時間帯(LocalTime.of(9,0,0), LocalTime.of(12,15,0));
        アルバイトの日にち アルバイトの日にち = new アルバイトの日にち(LocalDate.of(2026,4,7));
        業務内容 内容 = new 業務内容(1);

        return new アルバイト情報(労働時間帯, アルバイトの日にち, 内容);
    }
}
