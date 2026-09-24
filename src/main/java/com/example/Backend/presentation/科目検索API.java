package com.example.Backend.presentation;

import com.example.Backend.domain.追加業務検知.record.教員ID;
import com.example.Backend.domain.追加業務検知.record.科目名リスト;
import com.example.Backend.domain.追加業務検知.科目検索インターフェース;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class 科目検索API {
    private 科目検索インターフェース 科目検索;

    public 科目検索API(科目検索インターフェース 科目検索) {
        this.科目検索 = 科目検索;
    }

    @PostMapping("/科目検索")
    public 科目名リスト search(String 教員ID){
        科目名リスト 結果 = 科目検索.取得する(new 教員ID(教員ID));
        return 結果;
    }
}
