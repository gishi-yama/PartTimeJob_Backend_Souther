package com.example.Backend.domain.specification;

import com.example.Backend.domain.object.アルバイトの曜日;
import com.example.Backend.domain.object.時間帯;
import com.example.Backend.domain.object.講義の曜日;

public class ルール一覧 {
    public static ルール<アルバイトの曜日> 曜日ルールを得る(講義の曜日 講義) {
        // 将来的には「曜日ルールを得る」メソッドの引数にする or SQl文で取得する
        return new 曜日ルール(講義);
    }

    public static ルール<時間帯> 労働時間帯が講義時間帯に全て含まれるかルールを得る(時間帯 講義時間帯) {
        return new Andルール( // ここで講義時間帯を受け取る
                new Orルール(new 開始時間を得る(new 時間が同じルール(講義時間帯.開始時間())), new 開始時間を得る(new 時間が遅いルール(講義時間帯.開始時間()))), // ここで講義開始時間を受け取りたい
                new Orルール(new 終了時間を得る(new 時間が同じルール(講義時間帯.終了時間())), new 終了時間を得る(new 時間が早いルール(講義時間帯.終了時間())))); // ここで講義終了時間を受け取りたい
    }

    public static ルール<時間帯> 労働時間帯が講義時間帯に含まれる部分と含まれない部分があるかルールを得る(時間帯 講義時間帯) {

    }
}
