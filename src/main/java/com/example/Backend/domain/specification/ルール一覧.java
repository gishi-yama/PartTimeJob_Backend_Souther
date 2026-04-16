package com.example.Backend.domain.specification;

import com.example.Backend.domain.object.アルバイトの曜日;
import com.example.Backend.domain.object.時間帯;
import com.example.Backend.domain.object.講義の曜日;

public class ルール一覧 {
    public static ルール<アルバイトの曜日> 曜日ルールを得る(講義の曜日 講義) {
        // 将来的には「曜日ルールを得る」メソッドの引数にする or SQl文で取得する
        return new 曜日ルール(講義);
    }

    public static ルール<時間帯> 時間帯がすべて含まれるかルールを得る(時間帯 講義時間帯){
        return new Andルール(
                new Andルール(new 時間が同じルール(講義時間帯.開始時間()), new 時間が遅いルール(講義時間帯.開始時間())),
                new Andルール(new 時間が同じルール(講義時間帯.終了時間()), new 時間が早いルール(講義時間帯.開始時間())));
    }
}
