package com.example.Backend.usecase;

import com.example.Backend.domain.追加業務検知.record.*;
import com.example.Backend.domain.追加業務検知.アルバイト実施報告書インターフェース;
import com.example.Backend.domain.追加業務検知.カレンダー;
import com.example.Backend.domain.追加業務検知.労働時間帯分割;
import com.example.Backend.domain.追加業務検知.時間割インターフェース;
import org.springframework.stereotype.Service;

@Service
public class 追加業務検知 {

    private final 時間割インターフェース 時間割;
    private final アルバイト実施報告書インターフェース アルバイト実施報告書;
    private final カレンダー カレンダー;
    private final 労働時間帯分割 労働時間帯分割;

    public 追加業務検知(/*@Qualifier("時間割テスト")*/時間割インターフェース 時間割,
                        /*@Qualifier("曜日が一致かつ講義前から講義後まで労働している場合")*/アルバイト実施報告書インターフェース アルバイト実施報告書,
                        カレンダー カレンダー,
                        労働時間帯分割 労働時間帯分割) {
        this.時間割 = 時間割;
        this.アルバイト実施報告書 = アルバイト実施報告書;
        this.カレンダー = カレンダー;
        this.労働時間帯分割 = 労働時間帯分割;
    }

    public 追加業務検知の結果リスト 実行する(選択した科目のデータ 選択した科目のデータ) {
        講義情報 講義情報 = 時間割.取得する(選択した科目のデータ); // 1

        曜日なしアルバイト情報リスト 曜日なしアルバイト情報リスト = アルバイト実施報告書.取得する(選択した科目のデータ); // 2

        曜日付きアルバイト情報リスト 曜日付きアルバイト情報リスト = カレンダー.算出する(曜日なしアルバイト情報リスト); // 3

        return 労働時間帯分割.算出する(講義情報.講義の曜日(), 講義情報.講義時間帯(), 曜日付きアルバイト情報リスト); // 4

    }
}
