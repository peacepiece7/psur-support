CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  login_id VARCHAR(50) NOT NULL,
  username VARCHAR(100) NOT NULL,
  email VARCHAR(255) NULL,
  telno VARCHAR(30) NULL,
  password_hash VARCHAR(255) NOT NULL,
  ci VARCHAR(100) NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  last_login_at DATETIME NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at DATETIME NULL,
  UNIQUE KEY uk_users_login_id (login_id),
  KEY idx_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS role (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  role_code VARCHAR(50) NOT NULL,
  role_name VARCHAR(100) NOT NULL,
  role_type VARCHAR(20) NOT NULL,
  description VARCHAR(255) NULL,
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_role_role_code (role_code),
  CONSTRAINT chk_role_role_type CHECK (role_type IN ('ACTION', 'ACCESS'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS user_role (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  assigned_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at DATETIME NULL,
  UNIQUE KEY uk_user_role_user_role (user_id, role_id),
  KEY idx_user_role_role_id (role_id),
  CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_user_roles_role_id FOREIGN KEY (role_id) REFERENCES role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS common_code_group (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  group_code VARCHAR(50) NOT NULL,
  group_name VARCHAR(100) NOT NULL,
  parent_group_id BIGINT NULL,
  level INT NOT NULL,
  sort_order INT NOT NULL DEFAULT 0,
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  description VARCHAR(255) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at DATETIME NULL,
  UNIQUE KEY uk_ccg_group_code (group_code),
  KEY idx_ccg_parent_group_id (parent_group_id),
  CONSTRAINT fk_ccg_parent_group_id FOREIGN KEY (parent_group_id) REFERENCES common_code_group(id),
  CONSTRAINT chk_ccg_level CHECK (level BETWEEN 1 AND 3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS common_code (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  group_id BIGINT NOT NULL,
  code VARCHAR(50) NOT NULL,
  code_name VARCHAR(100) NOT NULL,
  child_group_code VARCHAR(50) NULL,
  sort_order INT NOT NULL DEFAULT 0,
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  description VARCHAR(255) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at DATETIME NULL,
  UNIQUE KEY uk_common_code_group_code (group_id, code),
  KEY idx_common_code_group_id (group_id),
  CONSTRAINT fk_common_code_group_id FOREIGN KEY (group_id) REFERENCES common_code_group(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sports_club (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  location VARCHAR(255) NULL,
  representative_name VARCHAR(100) NULL,
  representative_telno VARCHAR(30) NULL,
  club_no VARCHAR(50) NULL,
  business_no VARCHAR(50) NULL,
  club_role_code_id BIGINT NULL,
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at DATETIME NULL,
  UNIQUE KEY uk_sports_club_club_no (club_no),
  UNIQUE KEY uk_sports_club_business_no (business_no),
  KEY idx_sports_club_role_code_id (club_role_code_id),
  CONSTRAINT fk_sports_club_role_code_id FOREIGN KEY (club_role_code_id) REFERENCES common_code(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sports_club_category (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  club_id BIGINT NOT NULL,
  category_id BIGINT NOT NULL,
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at DATETIME NULL,
  UNIQUE KEY uk_sports_club_category (club_id, category_id),
  KEY idx_scc_category_id (category_id),
  CONSTRAINT fk_scc_club_id FOREIGN KEY (club_id) REFERENCES sports_club(id),
  CONSTRAINT fk_scc_category_id FOREIGN KEY (category_id) REFERENCES common_code(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS reg_sports_club_apply (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  status_code_id BIGINT NOT NULL,
  applied_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  applicant_name VARCHAR(100) NOT NULL,
  applicant_telno VARCHAR(30) NOT NULL,
  applicant_email VARCHAR(255) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at DATETIME NULL,
  KEY idx_rsca_status_code_id (status_code_id),
  CONSTRAINT fk_rsca_status_code_id FOREIGN KEY (status_code_id) REFERENCES common_code(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS reg_sports_club_application (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  apply_id BIGINT NOT NULL,
  name VARCHAR(200) NOT NULL,
  location VARCHAR(255) NULL,
  representative_name VARCHAR(100) NULL,
  representative_telno VARCHAR(30) NULL,
  business_no VARCHAR(50) NULL,
  club_role_code_id BIGINT NOT NULL,
  operating_sport_parent_code_id BIGINT NULL,
  operating_sport_child_code_id BIGINT NULL,
  approved_club_id BIGINT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at DATETIME NULL,
  UNIQUE KEY uk_rsca_apply_id (apply_id),
  KEY idx_rsca_club_role_code_id (club_role_code_id),
  KEY idx_rsca_operating_sport_parent_code_id (operating_sport_parent_code_id),
  KEY idx_rsca_operating_sport_child_code_id (operating_sport_child_code_id),
  KEY idx_rsca_approved_club_id (approved_club_id),
  CONSTRAINT fk_rsca_apply_id FOREIGN KEY (apply_id) REFERENCES reg_sports_club_apply(id),
  CONSTRAINT fk_rsca_club_role_code_id FOREIGN KEY (club_role_code_id) REFERENCES common_code(id),
  CONSTRAINT fk_rsca_operating_sport_parent_code_id FOREIGN KEY (operating_sport_parent_code_id) REFERENCES common_code(id),
  CONSTRAINT fk_rsca_operating_sport_child_code_id FOREIGN KEY (operating_sport_child_code_id) REFERENCES common_code(id),
  CONSTRAINT fk_rsca_approved_club_id FOREIGN KEY (approved_club_id) REFERENCES sports_club(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS reg_sports_club_application_category (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  application_id BIGINT NOT NULL,
  category_id BIGINT NOT NULL,
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at DATETIME NULL,
  UNIQUE KEY uk_rscac_application_category (application_id, category_id),
  KEY idx_rscac_category_id (category_id),
  CONSTRAINT fk_rscac_application_id FOREIGN KEY (application_id) REFERENCES reg_sports_club_application(id),
  CONSTRAINT fk_rscac_category_id FOREIGN KEY (category_id) REFERENCES common_code(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS reg_sports_club_apply_history (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  apply_id BIGINT NOT NULL,
  status_code_id BIGINT NOT NULL,
  handler_name VARCHAR(100) NULL,
  handler_telno VARCHAR(30) NULL,
  handler_email VARCHAR(255) NULL,
  memo VARCHAR(1000) NULL,
  processed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at DATETIME NULL,
  KEY idx_rscah_apply_id (apply_id),
  KEY idx_rscah_status_code_id (status_code_id),
  CONSTRAINT fk_rscah_apply_id FOREIGN KEY (apply_id) REFERENCES reg_sports_club_apply(id),
  CONSTRAINT fk_rscah_status_code_id FOREIGN KEY (status_code_id) REFERENCES common_code(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
