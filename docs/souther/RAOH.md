# Souther × Raoh — 境界の読み込みを Raoh で書く

ブランチは `souther-raoh` です。`souther` ブランチから分けています。

[Raoh](https://github.com/raoh-project/raoh-java) は、型のない入力（JSON、`Map`、DB の行）を型付きの値に読み込む Java のデコーダライブラリです。考え方は parse, don't validate で、失敗は例外ではなく値（`Err`）として返し、JSON Pointer のパスを付けて全部まとめて報告します。

## 1. なぜ Souther と組み合わせるのか

**Souther が生成する `型.decoder()` は、Raoh の `Decoder` そのものです。**

```java
public static net.unit8.raoh.decode.Decoder<Map<String, Object>, 時間帯> decoder();   // 時間帯.java（生成物）
public static net.unit8.raoh.decode.Decoder<Object, 学籍番号> decoder();             // 学籍番号.java（生成物）
```

そのため、Raoh の `combine` / `list` / `nested` の部品として、Souther の型をそのまま混ぜて使えます。役割は次のように分かれます。

| 担当 | 扱う問題 | 例 |
|---|---|---|
| **Raoh**（Java 側に書く） | 境界の**形** | DB の列名、`java.sql.Time` → `LocalTime`、リクエストの外枠、配列 |
| **Souther**（.sou に書く） | モデルの**意味** | `invariant 開始は終了より前`、`学籍番号` は空でない、曜日は 7 つの case のどれか |

`souther` ブランチでは、この境界を `RowMapper` と `__construct` で 1 つずつ組み立てていました。そのため、最初に見つかった不正な値で例外になっていました。

## 2. 変えたところ

| ファイル | 内容 |
|---|---|
| [`JDBCの値.java`](../../src/main/java/com/example/Backend/infrastracture/追加業務検知souther/JDBCの値.java)（新規） | JDBC の型の読み替え（`時刻`、`日付`）と、2 つの列から Souther の `時間帯` を作る部品 |
| [`Southerアルバイト実施報告書.java`](../../src/main/java/com/example/Backend/infrastracture/追加業務検知souther/Southerアルバイト実施報告書.java) | `RowMapper` ＋ `__construct` をやめ、`combine(...)` で行を宣言。`報告の行.list()` で全行を一度に読む |
| [`Souther時間割.java`](../../src/main/java/com/example/Backend/infrastracture/追加業務検知souther/Souther時間割.java) | 同じく `combine` で読む。曜日と時間帯の問題を同時に報告する |
| `Souther値.java`（削除） | Raoh の `Result` と `orElseThrow` に置き換えた |
| [`追加業務検知試算API.java`](../../src/main/java/com/example/Backend/presentation/追加業務検知試算API.java)（新規） | `POST /結果表示/souther/試算`。DB を使わず、JSON で渡した講義と報告を分割する |
| [`追加業務検知SoutherAPI.java`](../../src/main/java/com/example/Backend/presentation/追加業務検知SoutherAPI.java) | 科目名の 400 応答を Raoh の `Issues` の JSON 形式（path / code / message / meta）にした。`Ok` / `Err` は switch で分岐 |
| `追加業務検知.sou` | `exposing` に `一件を検知する` を追加（試算 API から呼ぶため）。モデル本体は変更なし |
| `pom.xml` | `net.unit8.raoh:raoh:0.9.0` を明示的な依存にした（souther-runtime 0.5.0 が使う版と同じ） |

### DB の行 → Souther の型

```java
final Decoder<Map<String, Object>, アルバイト実施報告> 報告の行 = combine(
        field("学籍番号", 学籍番号.decoder()),          // Souther: invariant 空でない
        field("学生の名前", 学生の名前.decoder()),
        field("アルバイトの日にち", JDBCの値.日付),     // Raoh: java.sql.Date → LocalDate
        flat(JDBCの値.時間帯("開始時間", "終了時間")),  // Raoh で 2 列を束ね、Souther: 開始は終了より前
        field("業務内容", 業務内容.decoder())
).map(this::アルバイト実施報告);                       // 生成された基底クラスのファクトリ

return JDBCの値.読めなければ止める(報告の行.list().decode(jdbcClient...listOfRows()), "ptj_requests の行");
```

DB に不正な行が複数あっても、例外のメッセージに全部入ります。

```
/1/学籍番号 too_short
/2   invariant_violation   （時間帯: 開始は終了より前）
/3/学籍番号 too_short
/3   invariant_violation
```

- `/1` などは何行目かを表します（0 始まり）。
- 時間帯の 2 件は、問題の場所が列ではなく行全体なので、パスは行番号だけになります。

### 試算 API（HTTP の入力 → Souther の型）

```java
static final Decoder<Map<String, Object>, 試算の入力> 試算の入力 = combine(
        field("講義", nested(講義情報.decoder())),                 // 中身は Souther の Decoder
        field("報告", list(nested(アルバイト実施報告.decoder())))   // 外枠（配列）は Raoh
).map(試算の入力::new);
```

**Java 版の不具合入力 3 種**（曜日の表記ゆれ、長さ 0、開始と終了の逆転）に空の学籍番号を加えて送ると、1 回の 400 で全部が返ります（実測）。

```json
{ "問題": [
  { "path": "/講義/講義の曜日",   "code": "not_allowed",
    "message": "must be one of [土, 日, 月, 木, 水, 火, 金]", "meta": { "actual": "水曜", ... } },
  { "path": "/報告/0/労働時間帯", "code": "invariant_violation",
    "message": "invariant violated on ...時間帯: 開始は終了より前",
    "meta": { "clause": "開始は終了より前", "type": "時間帯", ... } },
  { "path": "/報告/1/学籍番号",   "code": "too_short", "message": "must be at least 1 characters", ... },
  { "path": "/報告/1/労働時間帯", "code": "invariant_violation", ... }
] }
```

`meta.clause` には、.sou に書いた invariant の名前（`開始は終了より前`）がそのまま入ります。業務ルールの名前が、API の利用者にまで届きます。

正しい入力なら、`区分.type` に case 名を持つ JSON が 200 で返ります（例: `"講義の前後にはみ出した労働"`）。

## 3. 確認したこと

- `mvn test` で次の 4 クラス、計 39 件が成功しました。
  - `DB行の読み込みTest`（新規 5 件）
  - `追加業務検知試算APITest`（新規 3 件）
  - 既存の `労働時間帯分割の特性Test` と `追加業務検知SoutherJava連携Test`
- `souther examples --strict` は不足 0 件、終了コード 0 です。
- DB の行は、JDBC が実際に返す `java.sql.Time` / `java.sql.Date` を入れた Map で試しています。
- **H2 で起動して HTTP で確認しました**（起動手順は [README の 6 章](README.md#6-再現手順)）。
  - `/結果表示/souther`: 正常は 200。空の科目名は 400（`/科目名 too_short`）。時間割にない科目は 404。
  - 試算 API: 不正な入力 1 件で、3 つの問題が 400 でまとめて返ります（`/講義/講義の曜日`、`/報告/0/学籍番号`、`/報告/0/労働時間帯`）。正常な入力は 200 です。
- **未確認**: PostgreSQL に実際につないでの読み込み。

```bash
curl -X POST 'http://localhost:8082/結果表示/souther/試算' -H 'Content-Type: application/json' -d '{
  "講義": {"講義の曜日": "水曜", "講義時間帯": {"開始": "10:40", "終了": "12:10"}},
  "報告": [{"学籍番号": "", "学生の名前": "千歳 太郎", "日にち": "2026-11-25",
            "労働時間帯": {"開始": "11:30", "終了": "11:00"}, "業務内容": "演習補助"}]}'
```

## 4. 気付いたこと

- Souther の `Time` / `Date` の Decoder は、文字列（`"10:40"`）と `java.time` を受け付けます。JDBC の `java.sql.Time` / `java.sql.Date` は `expected time` で弾かれます。
  - そのため、JDBC の型の読み替えだけは Raoh 側（`JDBCの値`）で書いています。
- 生成される型のコンストラクタは公開されていません。Java から値を作る方法は次の 3 つで、どれも invariant を検査します。
  - Decoder
  - `__construct`
  - 注入 behavior の基底クラスにある protected のファクトリ
- 列挙値の誤りのエラーコードは `not_allowed`、文字列長の誤りは `too_short` です（Raoh 0.9.0）。
