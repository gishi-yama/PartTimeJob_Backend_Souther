package com.example.Backend.domain.specification;

import com.example.Backend.domain.object.アルバイトの曜日;
import com.example.Backend.domain.object.講義の曜日;

import java.util.function.BiPredicate;

public class 曜日が一致するかルール {
    private final 講義の曜日 講義;
    private final アルバイトの曜日 アルバイト;
    private final BiPredicate<講義の曜日,アルバイトの曜日> 曜日が一致するか = (講義の曜日,アルバイトの曜日)->講義の曜日.曜日().equals(アルバイトの曜日.曜日());

    public 曜日が一致するかルール(講義の曜日 講義, アルバイトの曜日 アルバイト) {
        this.講義 = 講義;
        this.アルバイト = アルバイト;
    }

    public boolean 判定する(){
        return 曜日が一致するか.test(講義, アルバイト);
    }
}
