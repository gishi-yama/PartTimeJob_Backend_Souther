package com.example.Backend.domain.specification;

import com.example.Backend.domain.object.時間帯;

import java.time.LocalTime;

public class 労働終了時間を得る implements ルール<時間帯>{
    private final ルール<LocalTime> ルール;

    public 労働終了時間を得る(ルール<LocalTime> ルール) {
        this.ルール = ルール;
    }

    @Override
    public boolean 判定する(時間帯 労働時間帯) {
        return ルール.判定する(労働時間帯.終了時間());
    }

}
