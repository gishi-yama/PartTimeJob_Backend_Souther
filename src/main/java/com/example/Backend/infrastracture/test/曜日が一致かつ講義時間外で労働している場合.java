package com.example.Backend.infrastracture.test;

import com.example.Backend.domain.追加業務検知.record.アルバイトの日にち;
import com.example.Backend.domain.追加業務検知.record.アルバイト情報;
import com.example.Backend.domain.追加業務検知.record.時間帯;
import com.example.Backend.domain.追加業務検知.record.業務内容;
import com.example.Backend.domain.追加業務検知.アルバイト実施報告書インターフェース;
import com.example.Backend.domain.追加業務検知.選択した科目のデータ;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public class 曜日が一致かつ講義時間外で労働している場合 implements アルバイト実施報告書インターフェース {
    @Override
    public アルバイト情報 取得する(選択した科目のデータ 選択した科目のデータ) {
        時間帯 労働時間帯 = new 時間帯(LocalTime.of(13, 15, 0), LocalTime.of(14, 45, 0));
        アルバイトの日にち アルバイトの日にち = new アルバイトの日にち(LocalDate.of(2026, 4, 6));
        業務内容 内容 = new 業務内容("採点業務");
        return new アルバイト情報(労働時間帯, アルバイトの日にち, 内容);
    }
}
