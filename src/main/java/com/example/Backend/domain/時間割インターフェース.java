package com.example.Backend.domain;

import com.example.Backend.domain.object.時間帯;
import com.example.Backend.domain.object.講義情報;
import com.example.Backend.domain.object.通常労働時間帯と追加労働時間帯;

public interface 時間割インターフェース {
    public 講義情報 取得する(選択した科目のデータ 選択した科目のデータ); // 1
    public 通常労働時間帯と追加労働時間帯 通常労働時間帯を作る(時間帯 労働時間帯); // 3.3.1
    public 通常労働時間帯と追加労働時間帯 追加労働時間帯を作る(時間帯 労働時間帯); // 3.2, 3.3.3
    public 通常労働時間帯と追加労働時間帯 通常労働時間帯と追加労働時間帯を作る(時間帯 労働時間帯, 時間帯 講義時間帯); // 3.3.2
    //      ↑は通常労働時間帯と追加労働時間帯をまとめたレコード
}
