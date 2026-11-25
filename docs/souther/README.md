# 追加業務検知を Souther で書き直す — 事例メモ

学生アシスタント（アルバイト）が働いた時間帯を、担当科目の講義時間帯と突き合わせます。そのうえで「講義時間内の通常労働」と「講義時間外の追加労働」に分割します。この処理（`domain/追加業務検知`）を [Souther](https://github.com/souther-lang/souther) 0.4.1 で書き直しました。

- ブランチ: `souther`（`main` は書き直し前のまま）
- 既存の Java 版（`/結果表示` とそのクラス群）は**変更していません**。Souther 版は `/結果表示/souther` として並べて置き、比較できるようにしています。

| 何を見るか | ファイル |
|---|---|
| Souther のモデル本体 | [`src/main/souther/追加業務検知.sou`](../../src/main/souther/追加業務検知.sou) |
| example（コンパイル時に評価される具体例） | [`src/main/souther/追加業務検知.examples.sou`](../../src/main/souther/追加業務検知.examples.sou) |
| `souther examples` の報告（追加前 / 追加後） | [`examples-report-1-before.txt`](examples-report-1-before.txt) / [`examples-report-2-after.txt`](examples-report-2-after.txt) |
| Java から使う側（DB 実装・API） | `infrastracture/追加業務検知souther/*`, `presentation/追加業務検知SoutherAPI.java` |
| 既存 Java 版との照合テスト | `src/test/java/.../domain/追加業務検知/労働時間帯分割の特性Test.java` |

`.sou` の各定義には `// 【Souther: 機能名】` というコメントを付けています。コードのどこでどの機能を使っているかは、そのコメントで追えます。

---

## 1. 何が変わったか（Java → Souther 対応表）

| Java（書き直し前） | Souther（書き直し後） | 使った機能 |
|---|---|---|
| `record 科目名(String)` などの文字列ラッパー。空文字も通る | `data 科目名 = String invariant 空でない = ...` | newtype、名前付き invariant |
| `講義の曜日(String)` と `アルバイトの曜日(String)` を `String.equals` で比較 | `data 曜日 = 月 \| 火 \| 水 \| 木 \| 金 \| 土 \| 日` | sum（unit case の列挙） |
| `record 時間帯(LocalTime, LocalTime)`。開始 ≥ 終了も作れる | `data 時間帯 = { 開始: Time, 終了: Time } invariant 開始は終了より前` | product + invariant |
| `曜日付きアルバイト情報`。`曜日なし` の 5 フィールドを手で書き写していた | `{ ...アルバイト実施報告, 曜日: 曜日 }` | field spread |
| `通常労働時間帯と追加労働時間帯`。null を許す 4 フィールドの組み合わせ（16 通り） | `労働時間帯の区分` = 6 つの case の sum。各 case は必要なフィールドだけを持つ | sum、bare sum 出力 |
| （表明なし） | `invariant 隙間なく続く = 前の追加労働時間帯.終了 == 通常労働時間帯.開始` | 複数フィールドにまたがる invariant（**コンパイル時に検査される**） |
| `specification/*.java` の 7 ルールを `if` で入れ子にし、null を代入して結果を組み立てる | `guard` 2 つと `if` 2 段で 6 つの case を返す | guard、if、constructs |
| （表明なし） | `ensures 労働全体を保つ = ...`（分割の前後で元の時間帯の両端が保たれる） | ensures |
| `カレンダー`。`Locale.JAPAN` の表示名で曜日文字列を作る | `曜日を求める`。基準日（月曜）からの日数を 7 で割った余りで決める | 純粋なヘルパ、`unreachable "理由"` |
| `時間割インターフェース` / `アルバイト実施報告書インターフェース` | 実装を書かない `behavior`。Java は生成された抽象基底クラスを継承する | 注入 behavior、`depends on`、`bind` |
| `時間割.取得する` は科目が無いと `.single()` の例外 | 出力 `講義情報 \| 時間割に科目がない` | 業務上の失敗を値として返す |
| テストなし（`contextLoads` のみ） | example 39 行。fake で DB を差し替える | example、fake、`examples for` ファイル |
| `労働情報表示`。`NullPointerException` を catch して「何も入っていません」を表示 | Encoder が `{"type": "講義前にはみ出した労働", ...}` を出す | 自動生成される Encoder |

規模の目安（空行・コメントを除く）は次のとおりです。

- Java 版: record・specification・カレンダー・労働時間帯分割で 34 ファイル、約 240 行
- Souther 版: モデル 1 ファイル約 190 行、example 約 270 行

---

## 2. `souther examples` が足りない example を教えてくれた

### 最初の example（17 行）

最初は Java の分岐 4.1〜4.4 を一通りなぞる例を書きました。分岐網羅は満たしています。

```
労働時間帯を分割する     implemented   rows 11   pending 0
    signature   out specified 6/6  observed 6/6  verified 6/6
    partition   axes 2   equivalence partitions 3/14
    border      borders 6   obligations 16/25
    branch      10/10
...
adequacy: not satisfied
36 gaps marked `!`: what a strict build refuses over.
```

分岐は 10/10 通っていますが、レポートは次の不足を `!` で指摘しました（[全文](examples-report-1-before.txt)）。

- **曜日の網羅**: `no row uses '月'`, `no row is in '火' at 講義.講義の曜日` …
  - 曜日の一致判定 `==` を sum の順序上の「線」として読み、隣の曜日（OFF point）と離れた曜日（OUT point）にも行を要求します。
- **1 秒単位の境界**: `no row is at the ON point (comparison@249:17)`
  - `read as 労働時間帯.終了 = 講義.講義時間帯.開始 + 1` という形で、具体的な値も示されます。
  - 「ちょうど 10:40 に終わった」行は書いていましたが、「10:40:01 に終わった」行はありませんでした。
  - Time は秒まで持つので、`>` と `>=` の取り違えを見分けるには 1 秒内側の行が必要です。
- **invariant の境界**: 1 文字の科目名、1 秒だけの時間帯
- **差し替え検査（replacement）**: `一件を検知する` は行が 1 つしかありませんでした。そのため「常に同じ値を返す」実装に置き換えても誰も気付かない、と指摘されました。

`souther examples --generate` は、足りない行の入力を `<?>` 付きで書き出します。期待値だけを人が埋めればよい状態になります。

### 追加後（39 行）

```
労働時間帯を分割する     implemented   rows 23   pending 0
    signature   out specified 6/6  observed 6/6  verified 6/6
    partition   axes 2   equivalence partitions 14/14
    border      borders 6   obligations 25/25
    branch      10/10
...
adequacy: undetermined
```

`!` は 0 件になり、`souther examples --strict` が終了コード 0 で通ります（[全文](examples-report-2-after.txt)）。

`undetermined` のまま残っている理由もレポートに書かれています。2 つの時刻を比べる条件（`労働.終了 > 講義時間.開始`）について、どちらの値で境界が決まるかをコンパイラが読み切れなかった、というものです。測れなかった部分を「合格」と言わずに、測れていないと明示する設計です。

git の履歴でもこの流れを追えます。

```
d61b30c 追加業務検知を Souther で書き直す（モデルと最初の example）
60be4d6 souther examples の指摘に沿って example を追加する
f800850 Souther 版を Maven ビルドと Spring に組み込む
```

---

## 3. Java 版で見つかった問題（実測）

Souther では、型や invariant がそもそも作らせない入力があります。同じ入力を元の Java 版に与えると何が起きるかを、実際に動かして確かめました。`労働時間帯分割の特性Test` に記録しています。

| 入力 | Java 版の結果（通常 / 前の追加 / 後ろの追加 / 全て追加） | Souther 版 |
|---|---|---|
| 長さ 0 の労働 10:40〜10:40（講義 10:40〜12:10） | `10:40~10:40 / - / - / 10:40~10:40`。**通常労働と追加労働の両方が立つ** | `時間帯` の invariant で構築できない（E1903） |
| 開始と終了が逆 11:30〜11:00 | `11:30~11:00 / - / - / -`。逆向きのまま通常労働として素通り | 同上 |
| 曜日の表記ゆれ（講義 `"水"`、労働 `"水曜"`） | 講義のない曜日として全て追加労働 | `曜日` は 7 つの case のどれかなので、Decoder が弾く |
| 時間割に科目が無い | `JdbcClient.single()` が例外を投げる | `時間割に科目がない` を返す。呼び出し側の `match` で扱わないとコンパイルエラー（E1201） |

正常な入力については、**Souther の example と同じ 23 ケースで Java 版も同じ結果を返す**ことを確かめています（`労働時間帯分割の特性Test`、23/23 一致）。書き直しで振る舞いは変わっていません。

---

## 4. コンパイラが止めてくれる誤り（デモ）

モデルをわざと壊して `souther compile` した結果です。いずれも実行前、コンパイルの時点で止まります。

### 4.1 分割した時間帯に隙間ができる → invariant をコンパイル時に検査（E2010）

`講義前にはみ出した労働` の通常労働の開始を、誤って `労働.開始` にした場合です。

```
-- INVARIANT  E2010 ----------------------------m.sou:262:13
262|             講義前にはみ出した労働 {
145|     invariant 隙間なく続く = 前の追加労働時間帯.終了 == 通常労働時間帯.開始

Constructing `講義前にはみ出した労働` here violates `隙間なく続く` on a reachable path:
under what is assumed where it stands, the value being built is one that clause rejects.
```

example が 1 行もなくても止まります。`if 労働.開始 < 講義時間.開始` の分岐の中では「前の追加の終了 ≠ 通常の開始」になることを、コンパイラが分岐条件から導いています。

### 4.2 時間帯の開始と終了を取り違える → 同じく E2010

`前の追加労働時間帯 = 時間帯 { 開始 = 講義時間.開始, 終了 = 労働.開始 }`（逆）とした場合です。

```
-- INVARIANT  E2010 ----------------------------m.sou:256:29
256|                 前の追加労働時間帯 = 時間帯 { 開始 = 講義時間.開始, 終了 = 労働.開始 },
76|     invariant 開始は終了より前 = 開始 < 終了
The value being built is one this clause rejects.
```

### 4.3 `>` を `>=` にする → example が止める（E1905）

`guard 労働.終了 > 講義時間.開始` を `>=` にした場合です。「講義の開始ちょうどに終わった」行で、長さ 0 の通常労働時間帯を作ろうとして invariant 違反になります。

```
-- EXAMPLE  E1905 -----------追加業務検知.examples.sou:74:12
This example does not hold.
It is:
    aborted: invariant violated on ...時間帯: 開始は終了より前
But it needs to be:
    講義時間外の労働 { 追加労働時間帯 = 時間帯 { 終了 = Time("10:40:00"), 開始 = Time("09:00:00") } }
Hint: 講義の開始ちょうどに終わった → 重なりなし（境界）
```

### 4.4 「時間割に科目がない」の扱いを忘れる → 網羅性（E1201）

```
-- NON-EXHAUSTIVE MATCH  E1201 ------------------m.sou:297:5
This `match` on union `時間割に科目がない | 講義情報` does not cover every case.
Hint: Add a case for: 時間割に科目がない
```

### 4.5 Java 版の不具合入力を example に書く → 入力が作れない（E1903）

```
-- EXAMPLE  E1903 ------------------------z.examples.sou:3:7
Input #2 of `労働時間帯を分割する` could not be built:
the value the row writes did not produce a value: invariant violated on ...時間帯: 開始は終了より前
```

### 4.6 Maven ビルドでも同じ

`souther-maven-plugin` が `mvn compile` の中で example を評価します。成り立たない example が 1 行でもあると `BUILD FAILURE` になります（`曜日を付ける` の期待値を `水` から `木` に変えて確認しました）。

```
[ERROR] -- EXAMPLE  E1905 -----------追加業務検知.examples.sou:31:12
Hint: At $.曜日 the row states 木, but the behavior answered with 水.
[ERROR] Failed to execute goal org.souther-lang:souther-maven-plugin:0.2.0:compile ... Souther reported 1 error.
```

---

## 5. Java との境界

```text
HTTP ──▶ 追加業務検知SoutherAPI ──▶ 追加業務を検知する（Souther）
          科目名.decoder()           │ depends on
          ...Result.encoder()        ├─▶ Souther時間割            （Java: JdbcClient）
                                     └─▶ Southerアルバイト実施報告書 （Java: JdbcClient）
```

- **依存の注入**: `追加業務を検知する.bind(時間割, アルバイト実施報告書)`。DB 実装は、生成された抽象クラス（`時間割から講義情報を取得する` など）を `extends` します。
- **値の構築**: Souther の型はコンストラクタが非公開です。Java からは `__construct`（`Result` を返す）か Decoder を通すしかなく、どちらも invariant を検査します。DB に開始 ≥ 終了の行があれば、Java 版のように黙って計算を続けず、`DataIntegrityViolationException` で止まります。
- **結果のケース分け**: 生成される `追加業務を検知するResult` は sealed interface です。Java の `switch` パターンマッチで扱えます。
- **JSON**: `追加業務検知の結果一覧.encoder().encode(...)` が Map / List を返し、Spring がそのまま JSON にします。

---

## 6. 再現手順

```bash
# Souther CLI（Homebrew）
brew install souther-lang/souther/souther

# example の評価と網羅レポート（--strict で不足があれば終了コード非 0）
souther examples src/main/souther/追加業務検知.sou src/main/souther/追加業務検知.examples.sou --strict

# 足りない行を <?> 付きで書き出す
souther examples src/main/souther/追加業務検知.sou src/main/souther/追加業務検知.examples.sou --generate

# 1 つの behavior を JSON 入力で試す（引数が複数なら JSON 配列）
souther run src/main/souther/追加業務検知.sou --behavior 労働時間帯を分割する \
  --input '[{"講義の曜日":"水","講義時間帯":{"開始":"10:40","終了":"12:10"}}, {"開始":"10:00","終了":"13:00"}, "水"]'
# => {"前の追加労働時間帯":{"開始":"10:00","終了":"10:40"},"通常労働時間帯":{"開始":"10:40","終了":"12:10"},
#     "後の追加労働時間帯":{"開始":"12:10","終了":"13:00"},"type":"講義の前後にはみ出した労働"}
# 長さ 0 の時間帯を渡すと: input #2 could not be decoded — (root): invariant violated on ...時間帯: 開始は終了より前

# Maven（JDK 25 が必要）
JAVA_HOME=$(/usr/libexec/java_home -v 25) ./mvnw test -Dtest='労働時間帯分割の特性Test,追加業務検知SoutherJava連携Test'
```

---

## 7. 制約・気付いたこと

- **JDK 25 が必要**: Souther の生成クラスと souther-runtime は Java 25 のクラスファイルです。そのため `java.version` を 21 → 25 に上げました。
  - `mvn compile` と上記テストは JDK 25 で通っています。
  - アプリ全体の起動（Vaadin 24.4 / PostgreSQL 込み）は、DB を用意していないため未確認です。
  - `BackendApplicationTests`（`contextLoads`）も DB が必要なので実行していません。
- **example が 1 行も無い状態の `souther examples` で内部エラー**: souther 0.4.1 で、`FixtureException: 講義時間帯: invariant violated ...` が出ました。網羅の探索が invariant を満たさない候補値を作ってしまうようです。example を書けば出ません。
- **`--lang ja` を付けてもレポートは英語**: 0.4.1 では、コンパイルエラー・レポートとも英語でした。
- **曜日の表記**: `曜日` の Decoder は case 名（`"月"`〜`"日"`）を受け付けます。DB の `day_of_the_week` がこの表記で入っている前提です。`"月曜"` などの別表記なら変換が要ります。
- **元の SQL について（未修正）**: 既存実装では、`subjects` の科目名の列を `name`（アルバイト実施報告書）と `subject_name`（時間割）で別々に参照しています。Souther 版の DB 実装も元の SQL をそのまま使っています。
