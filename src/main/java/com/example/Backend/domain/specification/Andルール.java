package com.example.Backend.domain.specification;

public class Andルール<T> implements ルール<T>{
    private final ルール<T> 左;
    private final ルール<T> 右;

    public Andルール(ルール<T> 左, ルール<T> 右) {
        this.左 = 左;
        this.右 = 右;
    }

    @Override
    public boolean 判定する(T オブジェクト) {
        return 左.判定する(オブジェクト) && 右.判定する(オブジェクト);
    }
}
