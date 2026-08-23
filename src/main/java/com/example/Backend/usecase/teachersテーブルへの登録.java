package com.example.Backend.usecase;

import com.example.Backend.domain.teachersテーブルへの登録.record.名前リスト;
import com.example.Backend.domain.teachersテーブルへの登録.record.教員アカウントリスト;
import com.example.Backend.domain.teachersテーブルへの登録.シラバスインターフェース;
import com.example.Backend.domain.teachersテーブルへの登録.パスワードマネージャー;
import com.example.Backend.domain.teachersテーブルへの登録.教員登録フォームインターフェース;
import org.springframework.stereotype.Service;

@Service
public class teachersテーブルへの登録 {

    private final シラバスインターフェース シラバス;
    private final パスワードマネージャー パスワードマネージャー;
    private final 教員登録フォームインターフェース 教員登録フォーム;

    public teachersテーブルへの登録(シラバスインターフェース シラバス,
                                    パスワードマネージャー パスワードマネージャー,
                                    教員登録フォームインターフェース 教員登録フォーム) {
        this.シラバス = シラバス;
        this.パスワードマネージャー = パスワードマネージャー;
        this.教員登録フォーム = 教員登録フォーム;
    }

    public void 実行する() {

        名前リスト 名前リスト = シラバス.取得する();

        教員アカウントリスト 教員アカウントリスト = パスワードマネージャー.作成する(名前リスト);

        教員登録フォーム.登録する(教員アカウントリスト);
    }

    public void 初期化する() {

        教員登録フォーム.削除する();
    }
}
