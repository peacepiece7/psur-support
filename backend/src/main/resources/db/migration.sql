-- Migration: roles/user_roles -> role/user_role
-- Review and run manually against the target database.

SET FOREIGN_KEY_CHECKS = 0;

-- Rename tables
RENAME TABLE roles TO role;
RENAME TABLE user_roles TO user_role;

-- Role table updates
ALTER TABLE role
  CHANGE COLUMN code role_code VARCHAR(50) NOT NULL,
  CHANGE COLUMN name role_name VARCHAR(100) NOT NULL;

ALTER TABLE role
  ADD COLUMN role_type VARCHAR(20) NOT NULL DEFAULT 'ACCESS' AFTER role_name;

UPDATE role
SET role_type = 'ACTION'
WHERE role_code IN ('APPLICANT', 'RECEIPT_MANAGER', 'REVIEWER', 'APPROVER');

UPDATE role
SET role_type = 'ACCESS'
WHERE role_code IN ('ADMIN_PORTAL_USER', 'ADMIN_SYSTEM_MANAGER');

ALTER TABLE role
  MODIFY COLUMN updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE role
  ALTER COLUMN role_type DROP DEFAULT;

ALTER TABLE role
  DROP COLUMN deleted_at;

ALTER TABLE role
  DROP INDEX uk_roles_code,
  ADD UNIQUE KEY uk_role_role_code (role_code),
  ADD CONSTRAINT chk_role_role_type CHECK (role_type IN ('ACTION', 'ACCESS'));

-- user_role table updates
ALTER TABLE user_role
  ADD COLUMN updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  AFTER assigned_at;

ALTER TABLE user_role
  DROP FOREIGN KEY fk_user_roles_role_id,
  ADD CONSTRAINT fk_user_roles_role_id FOREIGN KEY (role_id) REFERENCES role(id);

ALTER TABLE user_role
  DROP INDEX uk_user_roles_user_role,
  DROP INDEX idx_user_roles_role_id,
  ADD UNIQUE KEY uk_user_role_user_role (user_id, role_id),
  ADD KEY idx_user_role_user_id (user_id),
  ADD KEY idx_user_role_role_id (role_id);

SET FOREIGN_KEY_CHECKS = 1;

-- Add child_group_code to common_code
ALTER TABLE common_code
  ADD COLUMN child_group_code VARCHAR(50) NULL AFTER code_name;

UPDATE common_code
SET child_group_code = 'OPERATING_SPORT_BALL'
WHERE code = 'BALL_SPORTS'
  AND group_id = (SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT');

UPDATE common_code
SET child_group_code = 'OPERATING_SPORT_ATHLETICS'
WHERE code = 'ATHLETICS'
  AND group_id = (SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT');

UPDATE common_code
SET child_group_code = 'OPERATING_SPORT_GYMNASTICS'
WHERE code = 'GYMNASTICS'
  AND group_id = (SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT');

UPDATE common_code
SET child_group_code = 'OPERATING_SPORT_WATER'
WHERE code = 'WATER_SPORTS'
  AND group_id = (SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT');

UPDATE common_code
SET child_group_code = 'OPERATING_SPORT_ICE'
WHERE code = 'ICE_SPORTS'
  AND group_id = (SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT');

UPDATE common_code
SET child_group_code = 'OPERATING_SPORT_SNOW'
WHERE code = 'SNOW_SPORTS'
  AND group_id = (SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT');

UPDATE common_code
SET child_group_code = 'OPERATING_SPORT_OTHER'
WHERE code = 'OTHER_SPORTS'
  AND group_id = (SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT');

UPDATE common_code
SET child_group_code = 'OPERATING_SPORT_LEISURE_EXTREME'
WHERE code = 'LEISURE_EXTREME'
  AND group_id = (SELECT id FROM common_code_group WHERE group_code = 'OPERATING_SPORT');

-- Add process_instance_id to reg_sports_club_apply
ALTER TABLE reg_sports_club_apply
  ADD COLUMN process_instance_id BIGINT NULL AFTER id;

CREATE INDEX idx_rsca_process_instance_id ON reg_sports_club_apply (process_instance_id);
