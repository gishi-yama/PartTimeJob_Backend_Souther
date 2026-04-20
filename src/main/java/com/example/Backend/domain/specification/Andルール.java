package com.example.Backend.domain.specification;

//曜日と時間どっちも正しいかどうか検証
//左が開始時間　右が終了時間　Tオブジェクトが講義の曜日や実際の労働時間
public class Andルール<T> implements ルール<T> {
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


