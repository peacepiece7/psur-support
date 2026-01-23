-- Reset tables
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE common_code;
TRUNCATE TABLE common_code_group;
TRUNCATE TABLE sports_category;
SET FOREIGN_KEY_CHECKS = 1;

-- Common code groups
INSERT INTO common_code_group (group_code, group_name, parent_group_id, level, sort_order, is_active)
VALUES ('CLUB_ROLE', '클럽 역할', NULL, 1, 1, 1)
ON DUPLICATE KEY UPDATE
  group_name = VALUES(group_name),
  parent_group_id = VALUES(parent_group_id),
  level = VALUES(level),
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
  ('OPERATING_SPORT_SOCCER', '축구', @operating_sport_id, 2, 1, 1),
  ('OPERATING_SPORT_BASKETBALL', '농구', @operating_sport_id, 2, 2, 1),
  ('OPERATING_SPORT_VOLLEYBALL', '배구', @operating_sport_id, 2, 3, 1),
  ('OPERATING_SPORT_BASEBALL', '야구', @operating_sport_id, 2, 4, 1),
  ('OPERATING_SPORT_HANDBALL', '핸드볼', @operating_sport_id, 2, 5, 1),
  ('OPERATING_SPORT_RUGBY', '럭비', @operating_sport_id, 2, 6, 1),
  ('OPERATING_SPORT_HOCKEY', '하키', @operating_sport_id, 2, 7, 1),
  ('OPERATING_SPORT_TENNIS', '테니스', @operating_sport_id, 2, 8, 1),
  ('OPERATING_SPORT_BADMINTON', '배드민턴', @operating_sport_id, 2, 9, 1),
  ('OPERATING_SPORT_TABLE_TENNIS', '탁구', @operating_sport_id, 2, 10, 1),
  ('OPERATING_SPORT_SQUASH', '스쿼시', @operating_sport_id, 2, 11, 1),
  ('OPERATING_SPORT_RACQUETBALL', '라켓볼', @operating_sport_id, 2, 12, 1),
  ('OPERATING_SPORT_PICKLEBALL', '피클볼', @operating_sport_id, 2, 13, 1),
  ('OPERATING_SPORT_TAEKWONDO', '태권도', @operating_sport_id, 2, 14, 1),
  ('OPERATING_SPORT_JUDO', '유도', @operating_sport_id, 2, 15, 1),
  ('OPERATING_SPORT_KENDO', '검도', @operating_sport_id, 2, 16, 1),
  ('OPERATING_SPORT_HAPKIDO', '합기도', @operating_sport_id, 2, 17, 1),
  ('OPERATING_SPORT_SSIREUM', '씨름', @operating_sport_id, 2, 18, 1),
  ('OPERATING_SPORT_BOXING', '복싱', @operating_sport_id, 2, 19, 1),
  ('OPERATING_SPORT_WRESTLING', '레슬링', @operating_sport_id, 2, 20, 1),
  ('OPERATING_SPORT_KARATE', '가라테', @operating_sport_id, 2, 21, 1),
  ('OPERATING_SPORT_JIU_JITSU', '주짓수', @operating_sport_id, 2, 22, 1),
  ('OPERATING_SPORT_MUAY_THAI', '무에타이', @operating_sport_id, 2, 23, 1),
  ('OPERATING_SPORT_ATHLETICS', '육상', @operating_sport_id, 2, 24, 1),
  ('OPERATING_SPORT_MARATHON', '마라톤', @operating_sport_id, 2, 25, 1),
  ('OPERATING_SPORT_TRAIL_RUNNING', '트레일러닝', @operating_sport_id, 2, 26, 1),
  ('OPERATING_SPORT_TRIATHLON', '철인3종', @operating_sport_id, 2, 27, 1),
  ('OPERATING_SPORT_CYCLING', '사이클', @operating_sport_id, 2, 28, 1),
  ('OPERATING_SPORT_HIKING', '등산', @operating_sport_id, 2, 29, 1),
  ('OPERATING_SPORT_CAMPING', '캠핑', @operating_sport_id, 2, 30, 1),
  ('OPERATING_SPORT_SKATING', '스케이트', @operating_sport_id, 2, 31, 1),
  ('OPERATING_SPORT_SKATEBOARDING', '스케이트보드', @operating_sport_id, 2, 32, 1),
  ('OPERATING_SPORT_SURFING', '서핑', @operating_sport_id, 2, 33, 1),
  ('OPERATING_SPORT_PADDLEBOARD', '패들보드', @operating_sport_id, 2, 34, 1),
  ('OPERATING_SPORT_SWIMMING', '수영', @operating_sport_id, 2, 35, 1),
  ('OPERATING_SPORT_WATER_POLO', '수구', @operating_sport_id, 2, 36, 1),
  ('OPERATING_SPORT_DIVING', '다이빙', @operating_sport_id, 2, 37, 1),
  ('OPERATING_SPORT_CANOE', '카누', @operating_sport_id, 2, 38, 1),
  ('OPERATING_SPORT_ROWING', '조정', @operating_sport_id, 2, 39, 1),
  ('OPERATING_SPORT_YACHT', '요트', @operating_sport_id, 2, 40, 1),
  ('OPERATING_SPORT_ICE_SKATING', '빙상', @operating_sport_id, 2, 41, 1),
  ('OPERATING_SPORT_ARCHERY', '양궁', @operating_sport_id, 2, 42, 1),
  ('OPERATING_SPORT_SHOOTING', '사격', @operating_sport_id, 2, 43, 1),
  ('OPERATING_SPORT_BOWLING', '볼링', @operating_sport_id, 2, 44, 1),
  ('OPERATING_SPORT_BILLIARDS', '당구', @operating_sport_id, 2, 45, 1),
  ('OPERATING_SPORT_GOLF', '골프', @operating_sport_id, 2, 46, 1),
  ('OPERATING_SPORT_GATEBALL', '게이트볼', @operating_sport_id, 2, 47, 1),
  ('OPERATING_SPORT_YOGA', '요가', @operating_sport_id, 2, 48, 1),
  ('OPERATING_SPORT_PILATES', '필라테스', @operating_sport_id, 2, 49, 1),
  ('OPERATING_SPORT_GYMNASTICS', '체조', @operating_sport_id, 2, 50, 1),
  ('OPERATING_SPORT_DANCE_SPORTS', '댄스스포츠', @operating_sport_id, 2, 51, 1),
  ('OPERATING_SPORT_AEROBICS', '에어로빅', @operating_sport_id, 2, 52, 1),
  ('OPERATING_SPORT_CHEERLEADING', '치어리딩', @operating_sport_id, 2, 53, 1),
  ('OPERATING_SPORT_CROSSFIT', '크로스핏', @operating_sport_id, 2, 54, 1),
  ('OPERATING_SPORT_POWERLIFTING', '파워리프팅', @operating_sport_id, 2, 55, 1),
  ('OPERATING_SPORT_BODYBUILDING', '보디빌딩', @operating_sport_id, 2, 56, 1),
  ('OPERATING_SPORT_JUMP_ROPE', '줄넘기', @operating_sport_id, 2, 57, 1),
  ('OPERATING_SPORT_DRONE', '드론', @operating_sport_id, 2, 58, 1),
  ('OPERATING_SPORT_ESPORTS', 'e스포츠', @operating_sport_id, 2, 59, 1),
  ('OPERATING_SPORT_PARA_SPORTS', '장애인스포츠', @operating_sport_id, 2, 60, 1),
  ('OPERATING_SPORT_NEW_SPORTS', '뉴스포츠', @operating_sport_id, 2, 61, 1),
  ('OPERATING_SPORT_LIFESTYLE_SPORTS', '생활체육', @operating_sport_id, 2, 62, 1)
ON DUPLICATE KEY UPDATE
  group_name = VALUES(group_name),
  parent_group_id = VALUES(parent_group_id),
  level = VALUES(level),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

-- Common codes for club roles
INSERT INTO common_code (group_id, code, code_name, sort_order, is_active)
VALUES
  ((SELECT id FROM common_code_group WHERE group_code = 'CLUB_ROLE'), 'UNASSIGNED', '미지정', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'CLUB_ROLE'), 'DESIGNATED', '지정', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'CLUB_ROLE'), 'REGISTERED', '등록', 3, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'CLUB_ROLE'), 'PRE_DESIGNATED', '예비지정', 4, 1)
ON DUPLICATE KEY UPDATE
  code_name = VALUES(code_name),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

-- Operating sports (parent)
INSERT INTO common_code (group_id, code, code_name, sort_order, is_active)
VALUES
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'SOCCER', '축구', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'BASKETBALL', '농구', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'VOLLEYBALL', '배구', 3, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'BASEBALL', '야구', 4, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'HANDBALL', '핸드볼', 5, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'RUGBY', '럭비', 6, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'HOCKEY', '하키', 7, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'TENNIS', '테니스', 8, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'BADMINTON', '배드민턴', 9, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'TABLE_TENNIS', '탁구', 10, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'SQUASH', '스쿼시', 11, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'RACQUETBALL', '라켓볼', 12, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'PICKLEBALL', '피클볼', 13, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'TAEKWONDO', '태권도', 14, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'JUDO', '유도', 15, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'KENDO', '검도', 16, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'HAPKIDO', '합기도', 17, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'SSIREUM', '씨름', 18, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'BOXING', '복싱', 19, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'WRESTLING', '레슬링', 20, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'KARATE', '가라테', 21, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'JIU_JITSU', '주짓수', 22, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'MUAY_THAI', '무에타이', 23, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'ATHLETICS', '육상', 24, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'MARATHON', '마라톤', 25, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'TRAIL_RUNNING', '트레일러닝', 26, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'TRIATHLON', '철인3종', 27, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'CYCLING', '사이클', 28, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'HIKING', '등산', 29, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'CAMPING', '캠핑', 30, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'SKATING', '스케이트', 31, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'SKATEBOARDING', '스케이트보드', 32, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'SURFING', '서핑', 33, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'PADDLEBOARD', '패들보드', 34, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'SWIMMING', '수영', 35, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'WATER_POLO', '수구', 36, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'DIVING', '다이빙', 37, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'CANOE', '카누', 38, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'ROWING', '조정', 39, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'YACHT', '요트', 40, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'ICE_SKATING', '빙상', 41, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'ARCHERY', '양궁', 42, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'SHOOTING', '사격', 43, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'BOWLING', '볼링', 44, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'BILLIARDS', '당구', 45, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'GOLF', '골프', 46, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'GATEBALL', '게이트볼', 47, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'YOGA', '요가', 48, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'PILATES', '필라테스', 49, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'GYMNASTICS', '체조', 50, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'DANCE_SPORTS', '댄스스포츠', 51, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'AEROBICS', '에어로빅', 52, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'CHEERLEADING', '치어리딩', 53, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'CROSSFIT', '크로스핏', 54, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'POWERLIFTING', '파워리프팅', 55, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'BODYBUILDING', '보디빌딩', 56, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'JUMP_ROPE', '줄넘기', 57, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'DRONE', '드론', 58, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'ESPORTS', 'e스포츠', 59, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'PARA_SPORTS', '장애인스포츠', 60, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'NEW_SPORTS', '뉴스포츠', 61, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT'), 'LIFESTYLE_SPORTS', '생활체육', 62, 1)
ON DUPLICATE KEY UPDATE
  code_name = VALUES(code_name),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

-- Operating sports (child)
INSERT INTO common_code (group_id, code, code_name, sort_order, is_active)
VALUES
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SOCCER'), 'SOCCER', '축구', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SOCCER'), 'FUTSAL', '풋살', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SOCCER'), 'JOKGU', '족구', 3, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SOCCER'), 'BEACH_SOCCER', '비치사커', 4, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SOCCER'), 'FOOTBALL', '풋볼', 5, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BASKETBALL'), 'BASKETBALL', '농구', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BASKETBALL'), 'BASKETBALL_3X3', '3x3 농구', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BASKETBALL'), 'MINI_BASKETBALL', '미니농구', 3, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_VOLLEYBALL'), 'VOLLEYBALL', '배구', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_VOLLEYBALL'), 'BEACH_VOLLEYBALL', '비치발리볼', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_VOLLEYBALL'), 'SOFT_VOLLEYBALL', '소프트배구', 3, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BASEBALL'), 'BASEBALL', '야구', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BASEBALL'), 'SOFTBALL', '소프트볼', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BASEBALL'), 'TEEBALL', '티볼', 3, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_HANDBALL'), 'HANDBALL', '핸드볼', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_HANDBALL'), 'BEACH_HANDBALL', '비치핸드볼', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_RUGBY'), 'RUGBY', '럭비', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_RUGBY'), 'RUGBY_SEVENS', '럭비7인제', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_HOCKEY'), 'FIELD_HOCKEY', '필드하키', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_HOCKEY'), 'ICE_HOCKEY', '아이스하키', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_TENNIS'), 'TENNIS', '테니스', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_TENNIS'), 'SOFT_TENNIS', '소프트테니스', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BADMINTON'), 'BADMINTON', '배드민턴', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_TABLE_TENNIS'), 'TABLE_TENNIS', '탁구', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SQUASH'), 'SQUASH', '스쿼시', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_RACQUETBALL'), 'RACQUETBALL', '라켓볼', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_PICKLEBALL'), 'PICKLEBALL', '피클볼', 1, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_TAEKWONDO'), 'TAEKWONDO', '태권도', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_TAEKWONDO'), 'TAEKWONDO_POOMSAE', '품새', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_TAEKWONDO'), 'TAEKWONDO_KYORUGI', '겨루기', 3, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_JUDO'), 'JUDO', '유도', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_KENDO'), 'KENDO', '검도', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_HAPKIDO'), 'HAPKIDO', '합기도', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SSIREUM'), 'SSIREUM', '씨름', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BOXING'), 'BOXING', '복싱', 1, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_WRESTLING'), 'WRESTLING_FREESTYLE', '자유형 레슬링', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_WRESTLING'), 'WRESTLING_GRECO_ROMAN', '그레코로만형', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_KARATE'), 'KARATE', '가라테', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_JIU_JITSU'), 'JIU_JITSU_BJJ', '브라질리언 주짓수', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_MUAY_THAI'), 'MUAY_THAI', '무에타이', 1, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ATHLETICS'), 'ATHLETICS_SPRINT', '단거리', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ATHLETICS'), 'ATHLETICS_MIDDLE', '중거리', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ATHLETICS'), 'ATHLETICS_LONG', '장거리', 3, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ATHLETICS'), 'ATHLETICS_RELAY', '릴레이', 4, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ATHLETICS'), 'ATHLETICS_RACE_WALK', '경보', 5, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_MARATHON'), 'MARATHON', '마라톤', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_TRAIL_RUNNING'), 'TRAIL_RUNNING', '트레일러닝', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_TRIATHLON'), 'TRIATHLON', '트라이애슬론', 1, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_CYCLING'), 'CYCLING_ROAD', '로드사이클', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_CYCLING'), 'CYCLING_MTB', 'MTB', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_CYCLING'), 'CYCLING_BMX', 'BMX', 3, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_HIKING'), 'HIKING', '등산', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_HIKING'), 'SPORT_CLIMBING', '스포츠클라이밍', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_CAMPING'), 'CAMPING_SPORTS', '캠핑 스포츠', 1, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SKATING'), 'INLINE_SKATING', '인라인스케이트', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SKATING'), 'ROLLER_SKATING', '롤러스케이트', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SKATEBOARDING'), 'SKATEBOARDING', '스케이트보드', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SURFING'), 'SURFING', '서핑', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_PADDLEBOARD'), 'SUP', 'SUP', 1, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SWIMMING'), 'SWIMMING_FREESTYLE', '자유형', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SWIMMING'), 'SWIMMING_BACKSTROKE', '배영', 2, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SWIMMING'), 'SWIMMING_BREASTSTROKE', '평영', 3, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SWIMMING'), 'SWIMMING_BUTTERFLY', '접영', 4, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_WATER_POLO'), 'WATER_POLO', '수구', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_DIVING'), 'DIVING', '다이빙', 1, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_CANOE'), 'CANOE', '카누', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_CANOE'), 'KAYAK', '카약', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ROWING'), 'ROWING', '조정', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_YACHT'), 'YACHT', '요트', 1, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ICE_SKATING'), 'FIGURE_SKATING', '피겨스케이팅', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ICE_SKATING'), 'SPEED_SKATING', '스피드스케이팅', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ARCHERY'), 'ARCHERY', '양궁', 1, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SHOOTING'), 'AIR_RIFLE', '공기소총', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_SHOOTING'), 'AIR_PISTOL', '공기권총', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BOWLING'), 'BOWLING', '볼링', 1, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BILLIARDS'), 'POCKET_BILLIARDS', '포켓볼', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BILLIARDS'), 'CAROM', '캐롬', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_GOLF'), 'GOLF', '골프', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_GOLF'), 'SCREEN_GOLF', '스크린골프', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_GATEBALL'), 'GATEBALL', '게이트볼', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_YOGA'), 'YOGA', '요가', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_PILATES'), 'PILATES', '필라테스', 1, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_GYMNASTICS'), 'ARTISTIC_GYMNASTICS', '기계체조', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_GYMNASTICS'), 'RHYTHMIC_GYMNASTICS', '리듬체조', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_DANCE_SPORTS'), 'DANCE_SPORTS_LATIN', '라틴댄스', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_DANCE_SPORTS'), 'DANCE_SPORTS_MODERN', '모던댄스', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_AEROBICS'), 'AEROBICS', '에어로빅', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_CHEERLEADING'), 'CHEERLEADING', '치어리딩', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_CROSSFIT'), 'CROSSFIT', '크로스핏', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_POWERLIFTING'), 'POWERLIFTING', '파워리프팅', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_BODYBUILDING'), 'BODYBUILDING', '보디빌딩', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_JUMP_ROPE'), 'JUMP_ROPE', '줄넘기', 1, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_DRONE'), 'DRONE_RACING', '드론레이싱', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_ESPORTS'), 'ESPORTS', 'e스포츠', 1, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_PARA_SPORTS'), 'WHEELCHAIR_BASKETBALL', '휠체어농구', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_PARA_SPORTS'), 'BOCCIA', '보치아', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_NEW_SPORTS'), 'KIN_BALL', '킨볼', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_NEW_SPORTS'), 'FLOORBALL', '플로어볼', 2, 1),

  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_LIFESTYLE_SPORTS'), 'WALKING', '걷기', 1, 1),
  ((SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT_LIFESTYLE_SPORTS'), 'STRETCHING', '스트레칭', 2, 1)
ON DUPLICATE KEY UPDATE
  code_name = VALUES(code_name),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

-- Sports categories (level 1-2 examples)
INSERT INTO sports_category (name, parent_id, level, sort_order, is_active)
VALUES
  ('축구', NULL, 1, 1, 1),
  ('농구', NULL, 1, 2, 1),
  ('배구', NULL, 1, 3, 1)
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  level = VALUES(level),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);

SET @category_soccer_id := (
  SELECT id
  FROM sports_category
  WHERE name = '축구' AND level = 1
  ORDER BY id DESC
  LIMIT 1
);
SET @category_basketball_id := (
  SELECT id
  FROM sports_category
  WHERE name = '농구' AND level = 1
  ORDER BY id DESC
  LIMIT 1
);
SET @category_volleyball_id := (
  SELECT id
  FROM sports_category
  WHERE name = '배구' AND level = 1
  ORDER BY id DESC
  LIMIT 1
);

INSERT INTO sports_category (name, parent_id, level, sort_order, is_active)
VALUES
  ('풋살', @category_soccer_id, 2, 1, 1),
  ('족구', @category_soccer_id, 2, 2, 1),
  ('3x3 농구', @category_basketball_id, 2, 1, 1),
  ('비치발리볼', @category_volleyball_id, 2, 1, 1)
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  level = VALUES(level),
  sort_order = VALUES(sort_order),
  is_active = VALUES(is_active);
