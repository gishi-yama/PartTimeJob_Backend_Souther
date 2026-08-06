package com.example.Backend.domain.追加業務検知;

import com.example.Backend.domain.追加業務検知.record.曜日なしアルバイト情報;
import com.example.Backend.domain.追加業務検知.record.選択した科目のデータ;

import java.util.List;

public interface アルバイト実施報告書インターフェース {
    public List<曜日なしアルバイト情報> 取得する(選択した科目のデータ 選択した科目のデータ); // 2
}
