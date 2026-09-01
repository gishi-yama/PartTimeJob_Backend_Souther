package com.example.Backend.infrastracture.subjectsテーブルへの登録;

import com.example.Backend.domain.subjectsテーブルへの登録.record.教員IDあり講義情報リスト;
import com.example.Backend.domain.subjectsテーブルへの登録.record.日時付き講義情報;
import com.example.Backend.domain.subjectsテーブルへの登録.record.日時付き講義情報リスト;
import com.example.Backend.domain.subjectsテーブルへの登録.record.講義時間帯;
import com.example.Backend.domain.subjectsテーブルへの登録.時間割インターフェース;
import com.example.Backend.domain.subjectsテーブルへの登録.講義時間対応表;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class 時間割 implements 時間割インターフェース {

    @Override
    public 日時付き講義情報リスト 取得する(教員IDあり講義情報リスト 教員IDあり講義情報リスト){

        Path path = Paths.get("科目情報.txt");

        try {
            List<String> lines = Files.readAllLines(path);
            List<日時付き講義情報> 日時付き講義情報リスト = new ArrayList<>();

            for (String line : lines) {
                if (line.isBlank()) continue; // 空行は無視
                String[] items = line.split(","); // カンマ区切りの場合
                if (items.length == 4) {
                    String 科目名 = items[0].trim();
                    String 曜日   = items[1].trim();
                    int 開始時限 = Integer.parseInt(items[2].trim());
                    int 終了時限 = Integer.parseInt(items[3].trim());
                    講義時間帯 講義時間帯1 = 講義時間対応表.講義時間帯を得る(開始時限);
                    講義時間帯 講義時間帯2 = 講義時間対応表.講義時間帯を得る(終了時限);
                    講義時間帯 講義時間帯 = new 講義時間帯(講義時間帯1.開始時間(), 講義時間帯2.終了時間());
                    日時付き講義情報 日時付き講義情報 = new 日時付き講義情報();
                    日時付き講義情報リスト.add();
                }

                    日時Map.put(科目名, new 日時情報(曜日, 開始時限, 終了時限));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // ファイル読み込みに失敗した場合でも空のリストを返すか、エラーログを出力
        }

    }


