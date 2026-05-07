package com.example.Backend.domain.specification;

public class Notルール<T> implements ルール<T> {
    private final ルール<T> ルール;

    public Notルール(ルール<T> ルール) {
        this.ルール = ルール;
    }

    @Override
    public boolean 判定する(T オブジェクト) {
        return !ルール.判定する(オブジェクト);
    }
}
