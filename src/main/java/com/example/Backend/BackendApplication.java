package com.example.Backend;

import com.example.Backend.domain.追加業務検知.record.追加業務検知の結果リスト;
import com.example.Backend.domain.追加業務検知.record.選択した科目のデータ;
import com.example.Backend.domain.追加業務検知.労働情報表示;
import com.example.Backend.infrastracture.subjectsテーブルへの登録.シラバス;
import com.example.Backend.usecase.teachersテーブルへの登録;
import com.example.Backend.usecase.追加業務検知;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	// 起動時に自動で実行されるBeanを定義
	@Bean
	public CommandLineRunner run(追加業務検知 業務検知サービス, teachersテーブルへの登録 教員登録サービス, シラバス シラバス) {
		return args -> {

			シラバス.科目名表示();

			教員登録サービス.初期化する();
			教員登録サービス.実行する();

			System.out.println("--- 業務検知バッチ処理を開始します ---");

			// TODO: テスト用のダミーデータを生成してください
			選択した科目のデータ テストデータ = new 選択した科目のデータ("Javaプログラミング"/* 必要な引数 */);

			// メソッドの実行（ここで内部の「労働情報表示」が呼ばれ、シェルに出力されます）
			追加業務検知の結果リスト 結果 = 業務検知サービス.実行する(テストデータ);

			労働情報表示 労働情報表示 = new 労働情報表示();
			労働情報表示.労働情報を出力する(結果);

			System.out.println("--- 業務検知バッチ処理を終了しました ---");
		};
	}
}
