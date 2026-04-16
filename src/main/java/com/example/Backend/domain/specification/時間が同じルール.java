package com.example.Backend.domain.specification;

import java.time.LocalTime;

//時間が早いか比較する
public class 時間が同じルール implements ルール<LocalTime>{
    private final LocalTime 基準の時間;
    public 時間が同じルール(LocalTime 基準の時間){
        this.基準の時間=基準の時間;
    }

    //労働時間が基準の時間よりも前
    @Override
    public boolean 判定する(LocalTime 時間){
        return 基準の時間.compareTo(時間)==0;
    }
}
