DROP TABLE IF EXISTS ptj_requests;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS subjects;

CREATE TABLE users (
      student_number      VARCHAR(20)   PRIMARY KEY,  -- 学籍番号（主キー）
      name                VARCHAR(255)  NOT NULL,    -- 名前
      password            VARCHAR(255)  NOT NULL,    -- パスワード
      type                VARCHAR(2)    NOT NULL     -- SA or TA
       -- typeに入る値を制限する制約
           CONSTRAINT check_user_type CHECK (type IN ('SA', 'TA')) -- 制約
);

CREATE TABLE subjects (
      id             INTEGER       GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- 登録した順に連番でidが生成される
      name           VARCHAR(100)  NOT NULL,
      teacher_name   VARCHAR(50)   NOT NULL
);

CREATE TABLE ptj_requests (
      ptj_id               INTEGER       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
      user_student_number  VARCHAR(20)   NOT NULL,
      subject_id           INTEGER       NOT NULL,
      ptj_date             DATE          NOT NULL,
      start_time           TIME          NOT NULL,
      finish_time          TIME          NOT NULL,
      break_time_minutes   FLOAT         NOT NULL,
      office_hours         FLOAT         NOT NULL,
      duties               VARCHAR(255)  NOT NULL,

      FOREIGN KEY (user_student_number) REFERENCES users (student_number),
      FOREIGN KEY (subject_id) REFERENCES subjects (id)  ON UPDATE CASCADE
);

INSERT INTO users VALUES
        ('b2240400','江刺家','123','SA'),
        ('b2241170','城','987','SA'),
        ('b2240010','相原','683','TA'),
        ('b2241470','丹川元','872', 'TA');

INSERT INTO subjects VALUES
        (DEFAULT, 'Javaプログラミング', '山川 広人'),
        (DEFAULT, '線形代数Ⅰ(A)', '藤井 忍');

INSERT INTO ptj_requests VALUES
        (DEFAULT, 'b2240400', 1, '2026/7/7', '15:00', '18:15', 0.0,3.25, '授業補助'),
        (DEFAULT, 'b2241170', 1, '2026/7/7', '14:45', '18:30', 0.0,3.75, '授業補助'),
        (DEFAULT, 'b2240010', 2, '2026/7/10', '10:00', '12:00', 0.0,2, '採点'),
        (DEFAULT, 'b2241470', 2, '2026/7/10', '12:45', '14:45', 0.0,2, '授業補助'),
        (DEFAULT, 'b2240400', 1, '2026/7/14', '15:00', '18:30', 0.25,3.25, '授業補助'),
        (DEFAULT, 'b2241170', 1, '2026/7/13', '19:00', '20:00', 0.0,1, '授業準備');

