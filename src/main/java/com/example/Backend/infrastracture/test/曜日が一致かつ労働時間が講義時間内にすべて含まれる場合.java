package com.example.Backend.infrastracture.test;

import com.example.Backend.domain.追加業務検知.record.*;
import com.example.Backend.domain.追加業務検知.アルバイト実施報告書インターフェース;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public class 曜日が一致かつ労働時間が講義時間内にすべて含まれる場合 implements アルバイト実施報告書インターフェース {

    @Override
    public アルバイト情報 取得する(選択した科目のデータ 選択した科目のデータ){ // 2
    時間帯 労働時間帯 = new 時間帯(LocalTime.of(9,40,0), LocalTime.of(12,0,0));
    アルバイトの日にち アルバイトの日にち = new アルバイトの日にち(LocalDate.of(2026,6,1));
    業務内容 内容 = new 業務内容("授業補助");

    return new アルバイト情報(労働時間帯, アルバイトの日にち, 内容);
    }
}
