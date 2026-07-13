package com.example.Backend.domain.追加業務検知;

import com.example.Backend.domain.追加業務検知.record.講義情報;
import com.example.Backend.domain.追加業務検知.record.選択した科目のデータ;

public interface 時間割インターフェース {
    public 講義情報 取得する(選択した科目のデータ 選択した科目のデータ); // 1
}
