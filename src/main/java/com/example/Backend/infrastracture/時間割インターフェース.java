package com.example.Backend.infrastracture;

import com.example.Backend.domain.record.講義情報;
import com.example.Backend.domain.選択した科目のデータ;

public interface 時間割インターフェース {
    public 講義情報 取得する(選択した科目のデータ 選択した科目のデータ); // 1
}
