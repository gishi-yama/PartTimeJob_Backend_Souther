package com.example.Backend.infrastracture.追加業務検知souther;

import org.springframework.dao.DataIntegrityViolationException;
import souther.runtime.InvariantFailure;
import souther.runtime.Result;

/**
 * DB から読んだ値を Souther の型に入れるときの共通処理。
 *
 * <p>Souther が生成した型はコンストラクタが公開されておらず、{@code __construct} か Decoder を通してしか作れない。
 * どちらも invariant を必ず検査し、違反は {@link Result.Err} として返る。
 * DB に invariant を満たさない行（例: 開始時刻 &gt;= 終了時刻）があれば、Java 版のように黙って計算を続けず、ここで止める。
 */
final class Souther値 {

    private Souther値() {
    }

    static <T> T 検査して取り出す(Result<T, InvariantFailure> 構築結果, String 何を) {
        return switch (構築結果) {
            case Result.Ok<T, InvariantFailure> ok -> ok.value();
            case Result.Err<T, InvariantFailure> err ->
                    throw new DataIntegrityViolationException(何を + " が Souther モデルの invariant を満たしません: " + err.error());
        };
    }

    static <T> T 検査して取り出す(net.unit8.raoh.Result<T> 復号結果, String 何を) {
        return 復号結果.orElseThrow(issues ->
                new DataIntegrityViolationException(何を + " を Souther の型に読み込めません: " + issues));
    }
}
