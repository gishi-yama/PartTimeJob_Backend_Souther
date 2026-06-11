package com.example.Backend.infrastracture;

import com.example.Backend.domain.record.アルバイトの日にち;
import com.example.Backend.domain.record.アルバイト情報;
import com.example.Backend.domain.record.時間帯;
import com.example.Backend.domain.record.業務内容;
import com.example.Backend.domain.アルバイト実施報告書インターフェース;
import com.example.Backend.domain.選択した科目のデータ;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public class アルバイト実施報告書 implements アルバイト実施報告書インターフェース {
    @Override
    public アルバイト情報 取得する(選択した科目のデータ 選択した科目のデータ){ // 2
        時間帯 労働時間帯 = new 時間帯(LocalTime.of(9,0,0), LocalTime.of(12,15,0));
        アルバイトの日にち アルバイトの日にち = new アルバイトの日にち(LocalDate.of(2026,4,7));
        業務内容 内容 = new 業務内容("授業補助");

        return new アルバイト情報(労働時間帯, アルバイトの日にち, 内容);
    }
}
