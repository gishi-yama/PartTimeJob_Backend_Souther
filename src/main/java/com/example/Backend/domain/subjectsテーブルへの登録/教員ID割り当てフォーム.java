package com.example.Backend.domain.subjectsテーブルへの登録;

import com.example.Backend.domain.subjectsテーブルへの登録.record.*;

import java.util.ArrayList;
import java.util.List;

public class 教員ID割り当てフォーム {

    public 教員IDあり講義情報リスト 作成する(講義情報リスト 講義情報リスト, 教員証リスト 教員証リスト){

        List<教員IDあり講義情報> 教員IDあり講義情報リスト = new ArrayList<>();

        for(講義情報 講義情報 : 講義情報リスト.講義情報リスト()){
            for(教員証 教員証 : 教員証リスト.教員証リスト()){
                if(講義情報.教員名().教員名().equals(教員証.教員名().教員名())){
                    教員IDあり講義情報 教員IDあり講義情報 = new 教員IDあり講義情報(講義情報.科目ID(),講義情報.科目名(),教員証.教員ID());
                    教員IDあり講義情報リスト.add(教員IDあり講義情報);
                    break;
                }
            }
        }
        return new 教員IDあり講義情報リスト(教員IDあり講義情報リスト);
    }
}
