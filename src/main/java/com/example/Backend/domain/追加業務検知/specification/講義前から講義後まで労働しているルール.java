package com.example.Backend.domain.追加業務検知.specification;

import com.example.Backend.domain.追加業務検知.record.時間帯;

import java.util.function.BiPredicate;


public class 講義前から講義後まで労働しているルール {

    private final 時間帯 講義時間帯;
    private final 時間帯 労働時間帯;
    private final BiPredicate<時間帯,時間帯> 労働開始時間が講義開始時間より早い = (講義時間帯, 労働時間帯) -> 労働時間帯.開始時間().isBefore(講義時間帯.開始時間()); // a.isBefore(b) : aはbより後か
    private final BiPredicate<時間帯,時間帯> 労働終了時間が講義終了時間より遅い = (講義時間帯, 労働時間帯) -> 労働時間帯.終了時間().isAfter(講義時間帯.終了時間()); // a.isAfter(b) : aはbより後か

    public 講義前から講義後まで労働しているルール(時間帯 講義時間帯, 時間帯 労働時間帯){
        this.講義時間帯 = 講義時間帯;
        this.労働時間帯 = 労働時間帯;
    }

    public boolean 判定する(){
       return 労働開始時間が講義開始時間より早い.and(労働終了時間が講義終了時間より遅い).test(講義時間帯, 労働時間帯);
    }
}
