-- Reset tables
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE user_role;
TRUNCATE TABLE role;
TRUNCATE TABLE common_code;
TRUNCATE TABLE common_code_group;
SET FOREIGN_KEY_CHECKS = 1;

-- Roles (user_role concept)
INSERT INTO role (role_code, role_name, role_type, description, is_active)
VALUES
  ('APPLICANT', '신청/저장 버튼 가능', 'ACTION', NULL, 1),
  ('RECEIPT_MANAGER', '접수 처리', 'ACTION', NULL, 1),
  ('REVIEWER', '검토', 'ACTION', NULL, 1),
  ('APPROVER', '승인', 'ACTION', NULL, 1),
  ('ADMIN_PORTAL_USER', '행정 포털 접근', 'ACCESS', NULL, 1),
  ('ADMIN_SYSTEM_MANAGER', '시스템 관리자', 'ACCESS', NULL, 1)
ON DUPLICATE KEY UPDATE
  role_name = VALUES(role_name),
  role_type = VALUES(role_type),
  description = VALUES(description),
  is_active = VALUES(is_active);

-- Region hierarchy (level 1 -> level 2) sample
INSERT INTO common_code_group (group_code, group_name, parent_group_id, level, sort_order, is_active)
VALUES ('REGION', 'Region', NULL, 1, 1, 1)
ON DUPLICATE KEY UPDATE
  group_name = VALUES(group_name),
  parent_group_id = VALUES(parent_group_id),
  level = VALUES(level),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

SET @region_id := (
  SELECT id
  FROM common_code_group
  WHERE group_code = 'REGION'
);

INSERT INTO common_code_group (group_code, group_name, parent_group_id, level, sort_order, is_active)
VALUES
  ('SEOUL', 'Seoul', @region_id, 2, 1, 1),
  ('BUSAN', 'Busan', @region_id, 2, 2, 1)
ON DUPLICATE KEY UPDATE
  group_name = VALUES(group_name),
  parent_group_id = VALUES(parent_group_id),
  level = VALUES(level),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

-- Level 1 codes (used as next group_code)
INSERT INTO common_code (group_id, code, code_name, child_group_code, sort_order, is_active)
VALUES
  ((SELECT id FROM common_code_group WHERE group_code = 'REGION'), 'SEOUL', 'Seoul', 'SEOUL', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'REGION'), 'BUSAN', 'Busan', 'BUSAN', 2, 1)
ON DUPLICATE KEY UPDATE
  code_name = VALUES(code_name),
  child_group_code = VALUES(child_group_code),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

-- Level 2 codes for Seoul
INSERT INTO common_code (group_id, code, code_name, sort_order, is_active)
VALUES
  ((SELECT id FROM common_code_group WHERE group_code = 'SEOUL'), 'GANGDONG', 'Gangdong-gu', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'SEOUL'), 'GANGSEO', 'Gangseo-gu', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'SEOUL'), 'GANGNAM', 'Gangnam-gu', 3, 1)
ON DUPLICATE KEY UPDATE
  code_name = VALUES(code_name),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

-- Level 2 codes for Busan
INSERT INTO common_code (group_id, code, code_name, sort_order, is_active)
VALUES
  ((SELECT id FROM common_code_group WHERE group_code = 'BUSAN'), 'NAMGU', 'Nam-gu', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'BUSAN'), 'SEOGU', 'Seo-gu', 2, 1)
ON DUPLICATE KEY UPDATE
  code_name = VALUES(code_name),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

-- Contest status (global)
INSERT INTO common_code_group (group_code, group_name, parent_group_id, level, sort_order, is_active)
VALUES ('CONTEST_STATUS', '공모 상태', NULL, 1, 3, 1)
ON DUPLICATE KEY UPDATE
  group_name = VALUES(group_name),
  parent_group_id = VALUES(parent_group_id),
  level = VALUES(level),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

INSERT INTO common_code (group_id, code, code_name, sort_order, is_active)
VALUES
  ((SELECT id FROM common_code_group WHERE group_code = 'CONTEST_STATUS'), 'CONTEST_PREPARE', '공모전', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'CONTEST_STATUS'), 'CONTEST_OPEN', '공모중', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'CONTEST_STATUS'), 'CONTEST_CLOSED', '공모 종료', 3, 1)
ON DUPLICATE KEY UPDATE
  code_name = VALUES(code_name),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

-- Registered sports club application status
INSERT INTO common_code_group (group_code, group_name, parent_group_id, level, sort_order, is_active)
VALUES ('REG_SPORTS_CLUB_APPLY_STATUS', '등록 스포츠클럽 신청 프로세스 상태', NULL, 1, 4, 1)
ON DUPLICATE KEY UPDATE
  group_name = VALUES(group_name),
  parent_group_id = VALUES(parent_group_id),
  level = VALUES(level),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

INSERT INTO common_code (group_id, code, code_name, sort_order, is_active)
VALUES
  ((SELECT id FROM common_code_group WHERE group_code = 'REG_SPORTS_CLUB_APPLY_STATUS'), 'SAVED', '저장', 0, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'REG_SPORTS_CLUB_APPLY_STATUS'), 'APPLY', '신청', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'REG_SPORTS_CLUB_APPLY_STATUS'), 'RECEIVED', '접수', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'REG_SPORTS_CLUB_APPLY_STATUS'), 'RECEIVED_REJECTED', '접수반려', 3, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'REG_SPORTS_CLUB_APPLY_STATUS'), 'REVIEW', '검토', 4, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'REG_SPORTS_CLUB_APPLY_STATUS'), 'REVIEW_REJECTED', '검토반려', 5, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'REG_SPORTS_CLUB_APPLY_STATUS'), 'APPROVED', '승인', 6, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'REG_SPORTS_CLUB_APPLY_STATUS'), 'APPROVED_REJECTED', '승인반려', 7, 1)
ON DUPLICATE KEY UPDATE
  code_name = VALUES(code_name),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

-- Sports club roles
INSERT INTO common_code_group (group_code, group_name, parent_group_id, level, sort_order, is_active)
VALUES ('CLUB_ROLE', '스포츠클럽 권한', NULL, 1, 5, 1)
ON DUPLICATE KEY UPDATE
  group_name = VALUES(group_name),
  parent_group_id = VALUES(parent_group_id),
  level = VALUES(level),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

INSERT INTO common_code (group_id, code, code_name, sort_order, is_active)
VALUES
  ((SELECT id FROM common_code_group WHERE group_code = 'CLUB_ROLE'), 'REG_CLUB', '등록스포츠클럽', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'CLUB_ROLE'), 'DESIGNATED_CLUB', '지정스포츠클럽', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'CLUB_ROLE'), 'PRE_DESIGNATED_CLUB', '예비지정스포츠클럽', 3, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'CLUB_ROLE'), 'PUBLIC_CLUB', '공공스포츠클럽', 4, 1)
ON DUPLICATE KEY UPDATE
  code_name = VALUES(code_name),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

INSERT INTO common_code_group (group_code, group_name, parent_group_id, level, sort_order, is_active)
VALUES ('OPERATING_SPORT', '운영 종목', NULL, 1, 2, 1)
ON DUPLICATE KEY UPDATE
  group_name = VALUES(group_name),
  parent_group_id = VALUES(parent_group_id),
  level = VALUES(level),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

SET @operating_sport_id := (
  SELECT id
  FROM common_code_group
  WHERE group_code = 'OPERATING_SPORT'
);

INSERT INTO common_code_group (group_code, group_name, parent_group_id, level, sort_order, is_active)
VALUES
  ('OPERATING_SPORT_BALL', '구기', @operating_sport_id, 2, 1, 1),
  ('OPERATING_SPORT_ATHLETICS', '육상', @operating_sport_id, 2, 2, 1),
  ('OPERATING_SPORT_GYMNASTICS', '체조', @operating_sport_id, 2, 3, 1),
  ('OPERATING_SPORT_WATER', '수상', @operating_sport_id, 2, 4, 1),
  ('OPERATING_SPORT_ICE', '빙상', @operating_sport_id, 2, 5, 1),
  ('OPERATING_SPORT_SNOW', '설상', @operating_sport_id, 2, 6, 1),
  ('OPERATING_SPORT_OTHER', '기타', @operating_sport_id, 2, 7, 1),
  ('OPERATING_SPORT_LEISURE_EXTREME', '레저/익스트림', @operating_sport_id, 2, 8, 1)
ON DUPLICATE KEY UPDATE
  group_name = VALUES(group_name),
  parent_group_id = VALUES(parent_group_id),
  level = VALUES(level),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

-- Operating sports (categories)
INSERT INTO common_code (group_id, code, code_name, child_group_code, sort_order, is_active)
VALUES
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'BALL_SPORTS', '구기', 'OPERATING_SPORT_BALL', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'ATHLETICS', '육상', 'OPERATING_SPORT_ATHLETICS', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'GYMNASTICS', '체조', 'OPERATING_SPORT_GYMNASTICS', 3, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'WATER_SPORTS', '수상', 'OPERATING_SPORT_WATER', 4, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'ICE_SPORTS', '빙상', 'OPERATING_SPORT_ICE', 5, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'SNOW_SPORTS', '설상', 'OPERATING_SPORT_SNOW', 6, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'OTHER_SPORTS', '기타', 'OPERATING_SPORT_OTHER', 7, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'LEISURE_EXTREME', '레저/익스트림', 'OPERATING_SPORT_LEISURE_EXTREME', 8, 1)
ON DUPLICATE KEY UPDATE
  code_name = VALUES(code_name),
  child_group_code = VALUES(child_group_code),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

-- Operating sports (items)
INSERT INTO common_code (group_id, code, code_name, sort_order, is_active)
VALUES
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BALL'), 'SOCCER', '축구', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BALL'), 'BASKETBALL', '농구', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BALL'), 'BASEBALL', '야구', 3, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BALL'), 'VOLLEYBALL', '배구', 4, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BALL'), 'RUGBY', '럭비', 5, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BALL'), 'TENNIS', '테니스', 6, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BALL'), 'TABLE_TENNIS', '탁구', 7, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BALL'), 'GOLF', '골프', 8, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BALL'), 'HANDBALL', '핸드볼', 9, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ATHLETICS'), 'RUNNING', '달리기', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ATHLETICS'), 'MARATHON', '마라톤', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ATHLETICS'), 'LONG_JUMP', '멀리뛰기', 3, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ATHLETICS'), 'HIGH_JUMP', '높이뛰기', 4, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_GYMNASTICS'), 'ARTISTIC_GYMNASTICS', '기계체조', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_GYMNASTICS'), 'RHYTHMIC_GYMNASTICS', '리듬체조', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_WATER'), 'SWIMMING', '수영', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_WATER'), 'DIVING', '다이빙', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_WATER'), 'SYNCHRONIZED_SWIMMING', '싱크로나이즈드 스위밍', 3, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_WATER'), 'YACHT', '요트', 4, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_WATER'), 'ROWING', '조정', 5, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_WATER'), 'SURFING', '서핑', 6, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ICE'), 'FIGURE_SKATING', '피겨 스케이팅', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ICE'), 'SPEED_SKATING', '스피드 스케이딩', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ICE'), 'ICE_HOCKEY', '아이스 하키', 3, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SNOW'), 'SKI', '스키', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SNOW'), 'SNOWBOARD', '스노우보드', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_OTHER'), 'ESPORTS', 'e스포츠', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_OTHER'), 'BADUK', '바둑', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_OTHER'), 'CHESS', '체스', 3, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_OTHER'), 'POKER', '포커', 4, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_OTHER'), 'BODYBUILDING', '보디빌딩', 5, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_OTHER'), 'CROSSFIT', '크로스핏', 6, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_OTHER'), 'HYROX', '하이록스', 7, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_LEISURE_EXTREME'), 'HORSE_RIDING', '승마', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_LEISURE_EXTREME'), 'PARKOUR', '파쿠르', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_LEISURE_EXTREME'), 'DANCE_SPORTS', '댄스스포츠', 3, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_LEISURE_EXTREME'), 'SKY_DIVING', '스카이 다이빙', 4, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_LEISURE_EXTREME'), 'BUNGEE_JUMPING', '번지점프', 5, 1)
ON DUPLICATE KEY UPDATE
  code_name = VALUES(code_name),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);
