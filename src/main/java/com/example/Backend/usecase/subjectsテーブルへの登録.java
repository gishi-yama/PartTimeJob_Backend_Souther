package com.example.Backend.usecase;

import com.example.Backend.domain.subjectsテーブルへの登録.record.教員IDあり講義情報リスト;
import com.example.Backend.domain.subjectsテーブルへの登録.record.教員証リスト;
import com.example.Backend.domain.subjectsテーブルへの登録.record.日時付き講義情報リスト;
import com.example.Backend.domain.subjectsテーブルへの登録.record.講義情報リスト;
import com.example.Backend.domain.subjectsテーブルへの登録.*;
import org.springframework.stereotype.Service;

@Service
public class subjectsテーブルへの登録 {
        private final シラバスインターフェース シラバス;
        private final 教員名簿インターフェース 教員名簿;
        private final 教員ID割り当てフォーム 教員ID割り当てフォーム;
        private final 時間割インターフェース 時間割;
        private final 科目登録フォームインターフェース 科目登録フォーム;

    public subjectsテーブルへの登録(シラバスインターフェース シラバス,
                                    教員名簿インターフェース 教員名簿,
                                    教員ID割り当てフォーム 教員ID割り当てフォーム,
                                    時間割インターフェース 時間割,
                                    科目登録フォームインターフェース 科目登録フォーム) {
        this.シラバス = シラバス;
        this.教員名簿 = 教員名簿;
        this.教員ID割り当てフォーム = 教員ID割り当てフォーム;
        this.時間割 = 時間割;
        this.科目登録フォーム = 科目登録フォーム;
    }

    public void 実行する(){
        講義情報リスト 講義情報リスト = シラバス.取得する();

        教員証リスト 教員証リスト = 教員名簿.取得する();

        教員IDあり講義情報リスト 教員IDあり講義情報リスト = 教員ID割り当てフォーム.作成する(講義情報リスト, 教員証リスト);

        日時付き講義情報リスト 日時付き講義情報リスト = 時間割.取得する(教員IDあり講義情報リスト);

        科目登録フォーム.登録する(日時付き講義情報リスト);
    }

    public void 初期化する(){
        科目登録フォーム.削除する();
    }
}
