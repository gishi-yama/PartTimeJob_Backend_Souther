package com.example.Backend.domain.specification;

public interface ルール<T> {
    boolean 判定する(T オブジェクト);
}
