package com.example.Backend.infrastracture.subjectsテーブルへの登録;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.example.Backend.domain.subjectsテーブルへの登録.record.教員IDあり講義情報;
import com.example.Backend.domain.subjectsテーブルへの登録.record.教員IDあり講義情報リスト;
import com.example.Backend.domain.subjectsテーブルへの登録.record.日時付き講義情報;
import com.example.Backend.domain.subjectsテーブルへの登録.record.日時付き講義情報リスト;
import com.example.Backend.domain.subjectsテーブルへの登録.時間割インターフェース;

public class 時間割 implements 時間割インターフェース {

    @Override
    public 日時付き講義情報リスト 取得する(教員IDあり講義情報リスト 教員IDあり講義情報リスト){
        
        Path path = Paths.get("sample.txt");
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                System.out.println(line);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


