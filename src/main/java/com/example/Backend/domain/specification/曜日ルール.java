package com.example.Backend.domain.specification;

import com.example.Backend.domain.object.講義の曜日;
import com.example.Backend.domain.object.アルバイトの曜日;

public class 曜日ルール implements ルール<アルバイトの曜日> {
    private final 講義の曜日 講義;

    曜日ルール(講義の曜日 講義) {
        this.講義 = 講義;
    }

    @Override
    public boolean 判定する(アルバイトの曜日 アルバイト){
        return 講義.曜日().equals(アルバイト.曜日()); // 講義の曜日 と アルバイトの曜日 が一致するか
    }
}
