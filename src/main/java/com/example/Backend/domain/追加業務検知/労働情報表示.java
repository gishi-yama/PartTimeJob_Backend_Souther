package com.example.Backend.domain.追加業務検知;

import com.example.Backend.domain.追加業務検知.record.追加業務検知の結果;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class 労働情報表示 {
    public void 労働情報を出力する(List<追加業務検知の結果> 追加業務検知の結果リスト){

        for(追加業務検知の結果 追加業務検知の結果 : 追加業務検知の結果リスト) {
            System.out.println(追加業務検知の結果.学籍番号());
            System.out.println("＜" + 追加業務検知の結果.アルバイトの日にち() + "＞");
            System.out.println("\t\t\t\t\t\t\t　業務内容 : " + 追加業務検知の結果.業務内容());
            try {
                System.out.println("\t\t\t\t\t\t　 通常労働時間 : " + 追加業務検知の結果.通常労働時間帯と追加労働時間帯().通常労働時間帯().開始時間() + " ~ " + 追加業務検知の結果.通常労働時間帯と追加労働時間帯().通常労働時間帯().終了時間());
            } catch (NullPointerException e) {
                System.out.println("\t\t\t\t\t\t　 通常労働時間 : 何も入っていません");
            }
            try {
                System.out.println("　　　　　　前にはみ出している追加労働時間帯 : " + 追加業務検知の結果.通常労働時間帯と追加労働時間帯().前にはみ出している追加労働時間帯().開始時間() + " ~ " + 追加業務検知の結果.通常労働時間帯と追加労働時間帯().前にはみ出している追加労働時間帯().終了時間());
            } catch (NullPointerException e) {
                System.out.println("　　　　　　前にはみ出している追加労働時間帯 : 何も入っていません");
            }
            try {
                System.out.println("　　　　　後ろにはみ出している追加労働時間帯 : " + 追加業務検知の結果.通常労働時間帯と追加労働時間帯().後ろにはみ出している追加労働時間帯().開始時間() + " ~ " + 追加業務検知の結果.通常労働時間帯と追加労働時間帯().後ろにはみ出している追加労働時間帯().終了時間());
            } catch (NullPointerException e) {
                System.out.println("　　　　　後ろにはみ出している追加労働時間帯 : 何も入っていません");
            }
            try {
                System.out.println("講義時間帯に全て含まれていない追加労働時間帯 : " + 追加業務検知の結果.通常労働時間帯と追加労働時間帯().講義時間帯に全て含まれていない追加労働時間帯().開始時間() + " ~ " + 追加業務検知の結果.通常労働時間帯と追加労働時間帯().講義時間帯に全て含まれていない追加労働時間帯().終了時間());
            } catch (NullPointerException e) {
                System.out.println("講義時間帯に全て含まれていない追加労働時間帯 : 何も入っていません");
            }
            System.out.println();
        }
    }
}
