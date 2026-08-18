DROP TABLE IF EXISTS ptj_requests;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS subjects;

CREATE TABLE users (
      student_number      VARCHAR(20)   PRIMARY KEY,  -- 学籍番号（主キー）
      name                VARCHAR(255)  NOT NULL,    -- 名前
      password            VARCHAR(255)  NOT NULL,    -- パスワード
      type                VARCHAR(2)    NOT NULL     -- SA or TA
       -- typeに入る値を制限する制約
           CONSTRAINT check_user_type CHECK (student_type IN ('SA', 'TA')) -- 制約
);

CREATE TABLE subjects (
      id             INTEGER       GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- 登録した順に連番でidが生成される
      name           VARCHAR(100)  NOT NULL,
      teacher_name   VARCHAR(50)   NOT NULL,
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