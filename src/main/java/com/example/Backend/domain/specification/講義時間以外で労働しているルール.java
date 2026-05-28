package com.example.Backend.domain.specification;

import com.example.Backend.domain.record.時間帯;

import java.util.function.BiPredicate;


public class 講義時間以外で労働しているルール {

    private final 時間帯 講義時間帯;
    private final 時間帯 労働時間帯;
    private final BiPredicate<時間帯, 時間帯> 労働終了時間が講義開始時間より早いまたは同じ = (講義時間帯, 労働時間帯) -> 労働時間帯.終了時間().isBefore(講義時間帯.開始時間()) || 労働時間帯.終了時間().equals(講義時間帯.開始時間()); // a.isBefore(b) : aはbより前か
    private final BiPredicate<時間帯, 時間帯> 労働開始時間が講義終了時間より遅いまたは同じ = (講義時間帯, 労働時間帯) -> 労働時間帯.開始時間().isAfter(講義時間帯.終了時間()) ||  労働時間帯.開始時間().equals(講義時間帯.終了時間()); // a.isBefore(b) : aはbより後か

    public 講義時間以外で労働しているルール(時間帯 講義時間帯, 時間帯 労働時間帯) {
        this.講義時間帯 = 講義時間帯;
        this.労働時間帯 = 労働時間帯;
    }

    public boolean 判定する() {
        return 労働終了時間が講義開始時間より早いまたは同じ.or(労働開始時間が講義終了時間より遅いまたは同じ).test(講義時間帯, 労働時間帯);
    }
}
