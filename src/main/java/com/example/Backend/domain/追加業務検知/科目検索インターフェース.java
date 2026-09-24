package com.example.Backend.domain.追加業務検知;


import com.example.Backend.domain.追加業務検知.record.教員ID;
import com.example.Backend.domain.追加業務検知.record.権限;
import com.example.Backend.domain.追加業務検知.record.科目名リスト;

public interface 科目検索インターフェース{
    public 科目名リスト 取得する(教員ID 教員ID, 権限 権限);
}
