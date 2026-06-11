package com.example.Backend.domain;

import com.example.Backend.domain.record.アルバイトの日にち;
import com.example.Backend.domain.record.業務内容;
import com.example.Backend.domain.record.通常労働時間帯と追加労働時間帯;
import org.springframework.stereotype.Service;

@Service
public class 労働情報表示 {
    public void 労働情報を出力する(アルバイトの日にち アルバイトの日にち, 業務内容 業務内容, 通常労働時間帯と追加労働時間帯 通常労働時間帯と追加労働時間帯){
        System.out.println(アルバイトの日にち.日にち());
        System.out.println("　　　 業務内容 : " + 業務内容.内容());
        try {
            System.out.println("　 通常労働時間 : "+通常労働時間帯と追加労働時間帯.通常労働時間帯().開始時間() + " ~ " + 通常労働時間帯と追加労働時間帯.通常労働時間帯().終了時間());
        }catch(NullPointerException e){
            System.out.println("何も入っていません");
        }
        try {
            System.out.println("追加労働時間帯1 : "+通常労働時間帯と追加労働時間帯.追加労働時間帯1().開始時間() + " ~ " + 通常労働時間帯と追加労働時間帯.追加労働時間帯1().終了時間());
        }catch(NullPointerException e){
            System.out.println("何も入っていません");
        }
        try {
            System.out.println("追加労働時間帯2 : "+通常労働時間帯と追加労働時間帯.追加労働時間帯2().開始時間() + " ~ " + 通常労働時間帯と追加労働時間帯.追加労働時間帯2().終了時間());
        }catch(NullPointerException e){
            System.out.println("何も入っていません");
        }
    } // 4
}
