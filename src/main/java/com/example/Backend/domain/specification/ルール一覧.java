package com.example.Backend.domain.specification;

import com.example.Backend.domain.object.アルバイトの曜日;
import com.example.Backend.domain.object.講義の曜日;

public class ルール一覧 {
    public static ルール<アルバイトの曜日> 曜日ルールを得る(講義の曜日 講義) {
        // 将来的には「曜日ルールを得る」メソッドの引数にする or SQl文で取得する
        return new 曜日ルール(講義);
    }

}
