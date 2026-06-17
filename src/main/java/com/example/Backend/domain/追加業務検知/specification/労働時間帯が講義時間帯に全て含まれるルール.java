package com.example.Backend.domain.追加業務検知.specification;

import com.example.Backend.domain.追加業務検知.record.時間帯;

import java.util.function.BiPredicate;


public class 労働時間帯が講義時間帯に全て含まれるルール {

    private final 時間帯 講義時間帯;
    private final 時間帯 労働時間帯;
    private final BiPredicate<時間帯,時間帯> 労働開始時間が講義開始時間より遅いまたは同じ = (講義時間帯,労働時間帯) -> 労働時間帯.開始時間().isAfter(講義時間帯.開始時間()) || 労働時間帯.開始時間().equals(講義時間帯.開始時間()); // a.isBefore(b) : aはbより後か
    private final BiPredicate<時間帯,時間帯> 労働終了時間が講義終了時間より早いまたは同じ = (講義時間帯,労働時間帯) -> 労働時間帯.終了時間().isBefore(講義時間帯.終了時間()) || 労働時間帯.終了時間().equals(講義時間帯.終了時間()); // a.isBefore(b) : aはbより前か

    public 労働時間帯が講義時間帯に全て含まれるルール(時間帯 講義時間帯, 時間帯 労働時間帯){
        this.講義時間帯 = 講義時間帯;
        this.労働時間帯 = 労働時間帯;
    }

    public boolean 判定する(){
        return 労働開始時間が講義開始時間より遅いまたは同じ.and(労働終了時間が講義終了時間より早いまたは同じ).test(講義時間帯, 労働時間帯);
    }
}