-- ============================================================
-- 교내 시설물 민원 시스템 DB 스키마
-- MySQL 8.0 기준
-- ============================================================

-- 기존 테이블이 있다면 삭제 (FK 참조 순서 역순으로)
DROP TABLE IF EXISTS complaint_likes;
DROP TABLE IF EXISTS complaints;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS users;

-- ============================================================
-- 1. users : 유저 및 관리자
-- ============================================================
CREATE TABLE users (
    user_id     BIGINT       NOT NULL AUTO_INCREMENT COMMENT '사용자 고유 식별자',
    username    VARCHAR(50)  NOT NULL COMMENT '로그인 아이디',
    password    VARCHAR(255) NOT NULL COMMENT 'BCrypt 암호화된 비밀번호',
    name        VARCHAR(50)  NOT NULL COMMENT '사용자 이름',
    role        ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER' COMMENT '권한 (일반 유저, 관리자)',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입일시',

    PRIMARY KEY (user_id),
    UNIQUE KEY uk_users_username (username)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '유저 및 관리자';


-- ============================================================
-- 2. locations : 장소 정보
-- ============================================================
CREATE TABLE locations (
    location_id   BIGINT      NOT NULL AUTO_INCREMENT COMMENT '장소 고유 식별자',
    building_name VARCHAR(50) NOT NULL COMMENT '건물명 (예: 정보공학관, 도서관)',
    room_number   VARCHAR(20) NOT NULL COMMENT '호수 (예: 501호)',

    PRIMARY KEY (location_id),
    -- 같은 건물의 같은 호수가 중복 등록되는 것을 방지
    UNIQUE KEY uk_locations_building_room (building_name, room_number)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '장소 정보';


-- ============================================================
-- 3. items : 비품/시설물 정보
-- ============================================================
CREATE TABLE items (
    item_id       BIGINT      NOT NULL AUTO_INCREMENT COMMENT '비품 고유 식별자',
    location_id   BIGINT      NOT NULL COMMENT '비품이 위치한 장소 ID',
    item_category VARCHAR(50) NOT NULL COMMENT '비품 카테고리 (예: 모니터, 마우스, 책상, 키보드)',
    installed_at  DATE        NULL COMMENT '설치/교체 일자 (노후화 예측 계산용)',

    PRIMARY KEY (item_id),
    KEY idx_items_location_id (location_id),

    CONSTRAINT fk_items_location
        FOREIGN KEY (location_id) REFERENCES locations (location_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '비품/시설물 정보';


-- ============================================================
-- 4. complaints : 민원
-- ============================================================
CREATE TABLE complaints (
    complaint_id   BIGINT        NOT NULL AUTO_INCREMENT COMMENT '민원 고유 식별자',
    user_id        BIGINT        NOT NULL COMMENT '작성자 ID',
    location_id    BIGINT        NOT NULL COMMENT '고장 장소 ID',
    item_id        BIGINT        NULL COMMENT '고장 비품 ID (없을 수 있음)',
    title          VARCHAR(100)  NOT NULL COMMENT '제목',
    content        VARCHAR(300)  NOT NULL COMMENT '상세 내용 (300자 제한)',
    image_url      VARCHAR(255)  NULL COMMENT '첨부 사진 파일 경로',
    failure_reason ENUM('AGING', 'DAMAGE', 'DEFECT', 'UNKNOWN') NOT NULL COMMENT '고장 원인 (노후화, 파손, 초기불량, 미상)',
    status         ENUM('RECEIVED', 'IN_PROGRESS', 'COMPLETED') NOT NULL DEFAULT 'RECEIVED' COMMENT '처리 상태',
    like_count     INT           NOT NULL DEFAULT 0 COMMENT '공감 수 (중복 예방 및 긴급도 판단용)',
    admin_comment  TEXT          NULL COMMENT '관리자 답변',
    created_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성일시',
    updated_at     DATETIME      NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',

    PRIM›ARY KEY (complaint_id),
    KEY idx_complaints_user_id (user_id),
    KEY idx_complaints_location_id (location_id),
    KEY idx_complaints_item_id (item_id),
    KEY idx_complaints_status (status),
    KEY idx_complaints_created_at (created_at),

    CONSTRAINT fk_complaints_user
        FOREIGN KEY (user_id) REFERENCES users (user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_complaints_location
        FOREIGN KEY (location_id) REFERENCES locations (location_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_complaints_item
        FOREIGN KEY (item_id) REFERENCES items (item_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '민원';


-- ============================================================
-- 5. complaint_likes : 민원 공감 (중복 방지)
-- ============================================================
CREATE TABLE complaint_likes (
    like_id       BIGINT NOT NULL AUTO_INCREMENT COMMENT '공감 고유 ID',
    complaint_id  BIGINT NOT NULL COMMENT '민원 ID',
    user_id       BIGINT NOT NULL COMMENT '공감한 유저 ID',

    PRIMARY KEY (like_id),
    -- 유저당 한 민원에 1회만 공감 가능하도록 제한
    UNIQUE KEY uk_complaint_likes_complaint_user (complaint_id, user_id),
    KEY idx_complaint_likes_user_id (user_id),

    CONSTRAINT fk_complaint_likes_complaint
        FOREIGN KEY (complaint_id) REFERENCES complaints (complaint_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_complaint_likes_user
        FOREIGN KEY (user_id) REFERENCES users (user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '민원 공감 (중복 방지)';
