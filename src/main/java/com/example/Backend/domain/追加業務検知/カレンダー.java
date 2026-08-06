package com.example.Backend.domain.追加業務検知;

import com.example.Backend.domain.追加業務検知.record.アルバイトの曜日;
import com.example.Backend.domain.追加業務検知.record.曜日なしアルバイト情報;
import com.example.Backend.domain.追加業務検知.record.曜日付きアルバイト情報;
import org.springframework.stereotype.Service;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class カレンダー {
    public List<曜日付きアルバイト情報> 算出する(List<曜日なしアルバイト情報> 曜日なしアルバイト情報リスト) { // 3

        ArrayList<曜日付きアルバイト情報> 曜日付きアルバイト情報リスト = new ArrayList<>();

        for (曜日なしアルバイト情報 曜日なしアルバイト情報 : 曜日なしアルバイト情報リスト) {
            // 曜日を英語で算出
            var dayOfWeek = 曜日なしアルバイト情報.アルバイトの日にち().アルバイトの日にち().getDayOfWeek();

            // 日本語の短縮表記（〇）を取得
            String 曜日 = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.JAPAN);

            // 情報を詰めなおし
            曜日付きアルバイト情報 曜日付きアルバイト情報
                    = new 曜日付きアルバイト情報(曜日なしアルバイト情報.学籍番号(),
                                             曜日なしアルバイト情報.アルバイトの日にち(),
                                             new アルバイトの曜日(曜日),
                                             曜日なしアルバイト情報.労働時間帯(),
                                             曜日なしアルバイト情報.業務内容());
            曜日付きアルバイト情報リスト.add(曜日付きアルバイト情報);
        }

        return 曜日付きアルバイト情報リスト;
    }
}
