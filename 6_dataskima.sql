-- 1. 데이터베이스(저장소) 생성 및 선택
CREATE DATABASE IF NOT EXISTS dongeui_civil_db;
USE dongeui_civil_db;

-- 외래키 제약조건을 잠시 해제하여 순서 상관없이 기존 테이블을 깔끔하게 삭제
SET foreign_key_checks = 0;

DROP TABLE IF EXISTS answer;
DROP TABLE IF EXISTS sympathy;
DROP TABLE IF EXISTS complaint;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS member;

-- 외래키 제약조건 다시 켜기
SET foreign_key_checks = 1;


-- =====================================================================
-- 2. 회원 테이블 (member)
-- =====================================================================
-- 시스템을 사용하는 학생 및 관리자의 정보를 관리합니다.
CREATE TABLE member (
    member_id INT AUTO_INCREMENT PRIMARY KEY,     -- 회원 고유 번호 (기본키)
    student_id VARCHAR(20) NOT NULL UNIQUE,       -- 학번 또는 사번 (중복 불가)
    password VARCHAR(255) NOT NULL,             -- 비밀번호
    name VARCHAR(50) NOT NULL,                  -- 이름
    role VARCHAR(20) NOT NULL                   -- 권한 (STUDENT 또는 ADMIN)
);


-- =====================================================================
-- 3. 장소 정보 테이블 (locations)
-- =====================================================================
-- 민원이 발생한 건물과 호수 정보를 관리합니다.
CREATE TABLE locations (
    location_id    BIGINT      NOT NULL AUTO_INCREMENT COMMENT '장소 고유 식별자',
    building_name VARCHAR(50) NOT NULL COMMENT '건물명 (예: 정보공학관)',
    room_number   VARCHAR(20) NOT NULL COMMENT '호수 (예: 501호)',

    PRIMARY KEY (location_id),
    -- 동일 건물의 같은 호수가 중복 등록되는 것을 방지
    UNIQUE KEY uk_locations_building_room (building_name, room_number)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '장소 정보';


-- =====================================================================
-- 4. 비품/시설물 정보 테이블 (items)
-- =====================================================================
-- 특정 장소에 설치된 비품(모니터, 책상 등) 정보를 관리합니다.
CREATE TABLE items (
    item_id        BIGINT      NOT NULL AUTO_INCREMENT COMMENT '비품 고유 식별자',
    location_id    BIGINT      NOT NULL COMMENT '비품이 위치한 장소 고유 식별자',
    item_category VARCHAR(50) NOT NULL COMMENT '비품 카테고리 (예: 모니터, 책상)',
    installed_at  DATE        NULL COMMENT '설치/교체 일자 (노후화 예측 계산용)',

    PRIMARY KEY (item_id),
    KEY idx_items_location_id (location_id),

    -- 장소가 삭제될 때 비품 데이터가 연쇄 삭제되지 않도록 보호
    CONSTRAINT fk_items_location
        FOREIGN KEY (location_id) REFERENCES locations (location_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '비품/시설물 정보';


-- =====================================================================
-- 5. 민원 접수 테이블 (complaint)
-- =====================================================================
-- 학생들이 접수한 민원의 핵심 내용과 긴급 여부, 공감 수를 관리합니다.
CREATE TABLE complaint (
    complaint_id INT AUTO_INCREMENT PRIMARY KEY, -- 민원 고유 번호 (자동 증가)
    location_id     BIGINT         NOT NULL COMMENT '고장 장소 ID',
    item_id         BIGINT         NULL COMMENT '고장 비품 ID (없을 수 있음)',
    title VARCHAR(100) NOT NULL,                 -- 민원 제목
    content         VARCHAR(300)  NOT NULL COMMENT '상세 내용 (300자 제한)',
    image_url       VARCHAR(255)  NULL COMMENT '첨부 사진 파일 경로',
    failure_reason ENUM('AGING', 'DAMAGE', 'DEFECT', 'UNKNOWN') NOT NULL COMMENT '고장 원인 (노후화, 파손, 초기불량, 미상)',
    status VARCHAR(20) DEFAULT '접수대기',        -- 처리 상태 (접수대기, 처리중 등)
    like_count      INT            NOT NULL DEFAULT 0 COMMENT '공감 수',
    is_urgent       BOOLEAN        DEFAULT FALSE COMMENT '긴급 여부 (TRUE: 긴급 사고)', -- 긴급 민원 구분 플래그
    member_id INT,                               -- 작성한 학생의 회원 번호
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 작성일시
    
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);


-- =====================================================================
-- 6. 민원 공감수 테이블 (sympathy)
-- =====================================================================
-- "고쳐줘요" 공감 기능에서 유저당 1회만 공감할 수 있도록 중복을 방지합니다.
CREATE TABLE sympathy (
    sympathy_id INT AUTO_INCREMENT PRIMARY KEY, -- 공감 고유 번호
    complaint_id INT NOT NULL,                     -- 공감을 받은 민원 번호
    member_id INT NOT NULL,                        -- 공감을 누른 회원 번호
    
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (complaint_id) REFERENCES complaint(complaint_id),
    -- [핵심] 한 명의 유저가 동일 민원에 1번만 공감하도록 유니크 제약 설정
    CONSTRAINT unique_sympathy UNIQUE (complaint_id, member_id) 
);


-- =====================================================================
-- 7. 관리자 답변 테이블 (answer)
-- =====================================================================
-- 관리자가 민원에 답변을 작성하면 저장하는 테이블입니다.
CREATE TABLE answer (
    answer_id INT AUTO_INCREMENT PRIMARY KEY,   -- 답변 고유 번호
    complaint_id INT,                           -- 답변 대상 민원 번호
    admin_id INT,                               -- 답변을 작성한 관리자 회원 번호
    content TEXT NOT NULL,                      -- 답변 내용
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 답변 등록일시
    
    FOREIGN KEY (complaint_id) REFERENCES complaint(complaint_id),
    FOREIGN KEY (admin_id) REFERENCES member(member_id)
);


-- =====================================================================
-- 8. 민원 게시물 우선 순위 조회 쿼리 (SELECT)
-- =====================================================================
-- 요구사항 반영: 
-- 1. 관리자 답변이 달린 민원은 목록에서 제외 (삭제된 것처럼 처리)
-- 2. 긴급 민원이 최상단 (is_urgent DESC)
-- 3.그다음 공감수 많은 순 (like_count DESC)
-- 4.최신순 (created_at DESC)
-- =====================================================================
SELECT 
    c.complaint_id,
    c.title,
    c.content,
    c.failure_reason,
    c.status,
    c.like_count,
    c.is_urgent,
    c.created_at,
    m.member_id AS writer_id
FROM complaint c
JOIN member m ON c.member_id = m.member_id
WHERE NOT EXISTS (
    -- 답변(answer) 테이블에 해당 민원 ID가 존재하면 미해결 목록에서 숨김 처리
    SELECT 1 
    FROM answer a 
    WHERE a.complaint_id = c.complaint_id
)
ORDER BY 
    c.is_urgent DESC,     -- 1순위: 긴급 사고 민원 상위 배치 (TRUE가 먼저 옴)
    c.like_count DESC,    -- 2순위: 공감수가 많은 순서대로 정렬
    c.created_at DESC;    -- 3순위: 최신 작성된 순서대로 정렬