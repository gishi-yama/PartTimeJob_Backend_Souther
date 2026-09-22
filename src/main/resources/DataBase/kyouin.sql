DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS teachers;

CREATE TABLE teachers (
      teacher_id        VARCHAR(255)  PRIMARY KEY,
      teacher_name      VARCHAR(255)  NOT NULL,
      teacher_password  VARCHAR(255)  NOT NULL,
      teacher_type      VARCHAR(5)    NOT NULL CONSTRAINT check_teacher_type CHECK (teacher_type IN ('USER', 'ADMIN')) -- 制約
);

CREATE TABLE subjects (
      subject_id             INTEGER       PRIMARY KEY,
      subject_name           VARCHAR(100)  NOT NULL,
      teacher_id             VARCHAR(255)  NOT NULL,
      day_of_the_week        VARCHAR(1)    NOT NULL,
      scheduled_start_time   TIME          NOT NULL,
      scheduled_finish_time  TIME          NOT NULL,

      FOREIGN KEY (teacher_id)  REFERENCES teachers (teacher_id)  ON UPDATE CASCADE, -- 外部キー制約
      CONSTRAINT check_day_of_the_week CHECK (day_of_the_week IN ('月', '火', '水', '木', '金')), -- 制約
      CONSTRAINT check_time CHECK (scheduled_finish_time > scheduled_start_time) -- 開始時間が終了時間より早いことを強制する制約
);
