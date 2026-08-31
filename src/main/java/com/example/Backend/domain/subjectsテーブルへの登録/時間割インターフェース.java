package com.example.Backend.domain.subjectsテーブルへの登録;

import com.example.Backend.domain.subjectsテーブルへの登録.record.教員IDあり講義情報リスト;
import com.example.Backend.domain.subjectsテーブルへの登録.record.日時付き講義情報リスト;

public interface 時間割インターフェース {
    public 日時付き講義情報リスト 取得する(教員IDあり講義情報リスト 教員IDあり講義情報リスト);
}
