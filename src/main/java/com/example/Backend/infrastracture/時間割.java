package com.example.Backend.infrastracture;

import com.example.Backend.domain.record.時間帯;
import com.example.Backend.domain.record.講義の曜日;
import com.example.Backend.domain.record.講義情報;
import com.example.Backend.domain.時間割インターフェース;
import com.example.Backend.domain.選択した科目のデータ;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public class 時間割 implements 時間割インターフェース {

    @Override
    public 講義情報 取得する(選択した科目のデータ 選択した科目のデータ) { // 1
        時間帯 講義時間帯 = new 時間帯(LocalTime.of(9, 0, 0), LocalTime.of(12, 15, 0));
        講義の曜日 講義の曜日 = new 講義の曜日("月");
        return new 講義情報(講義時間帯, 講義の曜日);
    }

}
