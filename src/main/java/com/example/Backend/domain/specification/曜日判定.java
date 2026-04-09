package com.example.Backend.domain.specification;

import com.example.Backend.domain.object.講義の曜日;
import com.example.Backend.domain.object.アルバイトの曜日;

public class 曜日判定 implements 判定<講義の曜日, アルバイトの曜日>{
    @Override
    public boolean 判定する(講義の曜日 講義, アルバイトの曜日 アルバイト){
        return 講義.曜日().equals(アルバイト.曜日());
    }
}
