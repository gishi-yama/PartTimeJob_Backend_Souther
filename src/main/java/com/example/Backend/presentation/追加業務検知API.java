package com.example.Backend.presentation;

import com.example.Backend.domain.追加業務検知.record.追加業務検知の結果リスト;
import com.example.Backend.domain.追加業務検知.record.選択した科目のデータ;
import com.example.Backend.usecase.追加業務検知;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

// RESTful Webサービスを構築する際に、JSONやXMLなどのデータをAPIレスポンスとして直接返却するためのアノテーション
@RestController
public class 追加業務検知API {

    private 追加業務検知 追加業務検知;

    public 追加業務検知API(追加業務検知 ユースケース) {
        this.追加業務検知 = ユースケース;
    }

    @PostMapping("/結果表示") // HTTPのPOSTリクエストを受け取るための専用アノテーション
    public 追加業務検知の結果リスト post(String 科目名) {
        System.out.println("postメソッドでの科目出力：" + 科目名);
        追加業務検知の結果リスト 結果 = 追加業務検知.実行する(new 選択した科目のデータ(科目名));
        return 結果;
    }

}
