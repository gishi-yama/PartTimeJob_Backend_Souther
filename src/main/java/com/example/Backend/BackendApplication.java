package com.example.Backend;

import com.example.Backend.domain.選択した科目のデータ;
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
	public CommandLineRunner run(追加業務検知 業務検知サービス) {
		return args -> {
			System.out.println("--- 業務検知バッチ処理を開始します ---");

			// TODO: テスト用のダミーデータを生成してください
			選択した科目のデータ テストデータ = new 選択した科目のデータ(/* 必要な引数 */);

			// メソッドの実行（ここで内部の「労働情報表示」が呼ばれ、シェルに出力されます）
			業務検知サービス.実行する(テストデータ);

			System.out.println("--- 業務検知バッチ処理を終了しました ---");
		};
	}
}
