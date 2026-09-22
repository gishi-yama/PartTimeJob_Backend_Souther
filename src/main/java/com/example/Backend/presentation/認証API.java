package com.example.Backend.presentation;

import com.example.Backend.domain.認証.教員ID;
import com.example.Backend.domain.認証.教員パスワード;
import com.example.Backend.domain.認証.認証結果;
import com.example.Backend.infrastracture.認証.認証サーバ;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class 認証API {

    private 認証サーバ 認証サーバ;

    public 認証API(認証サーバ 認証サーバ) {
        this.認証サーバ = 認証サーバ;
    }

    @PostMapping("/認証") // HTTPのPOSTリクエストを受け取るための専用アノテーション
    public 認証結果 post(String 入力されたID, String 入力されたパスワード) {
        System.out.println("入力されたID：" + 入力されたID);
        System.out.println("入力されたパスワード：" + 入力されたパスワード);

        認証結果 認証結果 = 認証サーバ.認証する(new 教員ID(入力されたID), new 教員パスワード(入力されたパスワード));
        System.out.println("認証結果：" + 認証結果.認証可否().認証可否());

        return 認証結果;
    }
}
