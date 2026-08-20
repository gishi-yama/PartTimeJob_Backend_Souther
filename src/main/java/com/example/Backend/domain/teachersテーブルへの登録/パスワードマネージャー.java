package com.example.Backend.domain.teachersテーブルへの登録;

import com.example.Backend.domain.teachersテーブルへの登録.record.*;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class パスワードマネージャー {
    // 文字の候補
    private static final String SRC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public 教員アカウントリスト 作成する(名前リスト 名前リスト) {
        List<教員アカウント> 教員アカウント一覧 = new ArrayList<>();
        List<名前> 名前一覧 = 名前リスト.名前リスト();

        for (名前 名前 : 名前一覧) {
            パスワード パスワード = this.生成する(8);

            教員アカウント 教員アカウント  = new 教員アカウント(名前, パスワード);
            教員アカウント一覧.add(教員アカウント);
        }

        return new 教員アカウントリスト(教員アカウント一覧);
    }

    public パスワード 生成する(int len) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(SRC.length());
            sb.append(SRC.charAt(index));
        }

        return new パスワード(sb.toString());
    }
}
