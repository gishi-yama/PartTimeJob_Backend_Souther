package com.example.Backend.domain.追加業務検知;

import com.example.Backend.domain.追加業務検知.record.アルバイトの日にち;
import com.example.Backend.domain.追加業務検知.record.業務内容;
import com.example.Backend.domain.追加業務検知.record.通常労働時間帯と追加労働時間帯;
import org.springframework.stereotype.Service;

@Service
public class 労働情報表示 {
    public void 労働情報を出力する(アルバイトの日にち アルバイトの日にち,
                                   業務内容 業務内容,
                                   通常労働時間帯と追加労働時間帯 通常労働時間帯と追加労働時間帯){

        System.out.println(アルバイトの日にち.アルバイトの日にち());
        System.out.println("　　　 業務内容 : " + 業務内容.業務内容());
        try {
            System.out.println("　 通常労働時間 : " + 通常労働時間帯と追加労働時間帯.通常労働時間帯().開始時間() + " ~ " + 通常労働時間帯と追加労働時間帯.通常労働時間帯().終了時間());
        }catch(NullPointerException e){
            System.out.println("　 通常労働時間 : 何も入っていません");
        }
        try {
            System.out.println("前にはみ出している追加労働時間帯 : " + 通常労働時間帯と追加労働時間帯.前にはみ出している追加労働時間帯().開始時間() + " ~ " + 通常労働時間帯と追加労働時間帯.前にはみ出している追加労働時間帯().終了時間());
        }catch(NullPointerException e){
            System.out.println("前にはみ出している追加労働時間帯 : 何も入っていません");
        }
        try {
            System.out.println("後ろにはみ出している追加労働時間帯 : " + 通常労働時間帯と追加労働時間帯.後ろにはみ出している追加労働時間帯().開始時間() + " ~ " + 通常労働時間帯と追加労働時間帯.後ろにはみ出している追加労働時間帯().終了時間());
        }catch(NullPointerException e){
            System.out.println("後ろにはみ出している追加労働時間帯 : 何も入っていません");
        }
        try {
            System.out.println("講義時間帯に全て含まれていない追加労働時間帯 : " + 通常労働時間帯と追加労働時間帯.講義時間帯に全て含まれていない追加労働時間帯().開始時間() + " ~ " + 通常労働時間帯と追加労働時間帯.講義時間帯に全て含まれていない追加労働時間帯().終了時間());
        }catch(NullPointerException e){
            System.out.println("講義時間帯に全て含まれていない追加労働時間帯 : 何も入っていません");
        }
    } // 4
}
