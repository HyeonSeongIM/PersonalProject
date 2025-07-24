CREATE TABLE Member
(
    member_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    verifyKey VARCHAR(255) NOT NULL,
    username  VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL,
    role      VARCHAR(50)  NOT NULL,
    status    VARCHAR(50)
);

CREATE INDEX idx_member_verifyKey ON Member (verifyKey);
