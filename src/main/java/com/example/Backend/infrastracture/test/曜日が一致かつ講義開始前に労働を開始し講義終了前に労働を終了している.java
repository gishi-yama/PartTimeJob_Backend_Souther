package com.example.Backend.infrastracture.test;

import com.example.Backend.domain.record.*;
import com.example.Backend.domain.アルバイト実施報告書インターフェース;
import com.example.Backend.domain.選択した科目のデータ;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public class 曜日が一致かつ講義開始前に労働を開始し講義終了前に労働を終了している implements アルバイト実施報告書インターフェース {
    @Override
    public アルバイト情報 取得する(選択した科目のデータ 選択した科目のデータ){ // 2
        時間帯 労働時間帯 = new 時間帯(LocalTime.of(8,30,0), LocalTime.of(12,0,0));
        アルバイトの日にち アルバイトの日にち = new アルバイトの日にち(LocalDate.of(2026,4,13));
        業務内容 内容 = new 業務内容(1);

        return new アルバイト情報(労働時間帯, アルバイトの日にち, 内容);
    }
}
