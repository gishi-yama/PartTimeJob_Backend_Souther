package com.example.Backend.infrastracture.subjectsテーブルへの登録;

import com.example.Backend.domain.subjectsテーブルへの登録.record.*;
import com.example.Backend.domain.subjectsテーブルへの登録.時間割インターフェース;
import com.example.Backend.domain.subjectsテーブルへの登録.講義時間対応表;
import com.helger.commons.io.resource.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository("subject用時間割")
public class 時間割 implements 時間割インターフェース {

    @Override
    public 日時付き講義情報リスト 取得する(教員IDあり講義情報リスト 教員IDあり講義情報リスト) {

        List<日時付き講義情報> 日時付き講義情報リスト = new ArrayList<>();
        List<String[]> 講義データ一覧 = new ArrayList<>();

        // クラスパスにあるファイルを読み込む
        ClassPathResource resource = new ClassPathResource("科目情報.txt");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(resource.getInputStream()),
                        StandardCharsets.UTF_8))) {

            // テキストファイルを全行読み取りListに格納
            String tmp;
            List<String> lines = new ArrayList<>();
            while((tmp = reader.readLine()) != null){
                System.out.println(tmp);
                lines.add(tmp);
            }

            // テキストファイルから取り出した1行分の情報を「,」で分割し、String[]形式に変換した上でListに収納
            for (String line : lines) {
                if (line.isBlank()) continue; // 空行は無視
                String[] items = line.split(",");
                for (int i = 0; i < items.length; i++) {
                    items[i] = items[i].trim();// 空白をカット
                }
                講義データ一覧.add(items);
            }

            // 講義の曜日・時間帯の情報を付加しレコードを作成
            for (教員IDあり講義情報 教員IDあり講義情報 : 教員IDあり講義情報リスト.教員IDあり講義情報リスト()) {
                for (String[] 講義データ : 講義データ一覧) {
                    // 要素数が正しいかつ科目名が一致しているものを探す
                    if (講義データ.length == 4 && 教員IDあり講義情報.科目名().科目名().equals(講義データ[0])) {

                        講義の曜日 講義の曜日 = new 講義の曜日(講義データ[1]);
                        // enumを使用して講義時間帯を得る
                        講義時間帯 講義時間帯1 = 講義時間対応表.講義時間帯を得る(Integer.parseInt(講義データ[2]));
                        講義時間帯 講義時間帯2 = 講義時間対応表.講義時間帯を得る(Integer.parseInt(講義データ[3]));
                        講義時間帯 講義時間帯 = new 講義時間帯(講義時間帯1.開始時間(), 講義時間帯2.終了時間());
                        
                        日時付き講義情報 日時付き講義情報 = new 日時付き講義情報(教員IDあり講義情報.科目ID(),
                                教員IDあり講義情報.科目名(),
                                教員IDあり講義情報.教員ID(),
                                講義の曜日,
                                講義時間帯);

                        日時付き講義情報リスト.add(日時付き講義情報);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // ファイル読み込みに失敗した場合でも空のリストを返すか、エラーログを出力
        }
        return new 日時付き講義情報リスト(日時付き講義情報リスト);
    }
}
