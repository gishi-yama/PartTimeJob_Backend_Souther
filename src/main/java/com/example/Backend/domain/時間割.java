package com.example.Backend.domain;

import com.example.Backend.domain.object.時間帯;
import com.example.Backend.domain.object.講義の曜日;
import com.example.Backend.domain.object.講義情報;
import com.example.Backend.domain.object.通常労働時間帯と追加労働時間帯;

import java.time.LocalTime;

public class 時間割 implements 時間割インターフェース{
    @Override
    public 講義情報 取得する(選択した科目のデータ 選択した科目のデータ){ // 1
        時間帯 講義時間帯 = new 時間帯(LocalTime.of(9,0,0), LocalTime.of(12,15,0));
        講義の曜日 講義の曜日 = new 講義の曜日("月");
        return new 講義情報(講義時間帯, 講義の曜日);
    }

    @Override
    public 通常労働時間帯 通常労働時間帯を作る(時間帯 労働時間帯){ // 3.3.1
        return new 通常労働時間帯();
    }

    @Override
    public 追加労働時間帯 追加労働時間帯を作る(時間帯 労働時間帯){ // 3.2, 3.3.3
        return new 追加労働時間帯();
    }

    @Override
    public 通常労働時間帯と追加労働時間帯 通常労働時間帯と追加労働時間帯を作る(時間帯 労働時間帯, 時間帯 講義時間帯){ // 3.3.2
        通常労働時間帯 通常労働時間帯 = new 通常労働時間帯();
        追加労働時間帯 追加労働時間帯 = new 追加労働時間帯();
        return new 通常労働時間帯と追加労働時間帯(通常労働時間帯, 追加労働時間帯);
//        return null;
    }
}
