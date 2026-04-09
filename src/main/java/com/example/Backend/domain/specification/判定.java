package com.example.Backend.domain.specification;

public interface 判定<T, S> {
    boolean 判定する(T オブジェクト1, S オブジェクト2);
}
