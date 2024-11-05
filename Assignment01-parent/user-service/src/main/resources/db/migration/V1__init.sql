CREATE TABLE t_types (
                         type_id BIGSERIAL NOT NULL,
                         type_name VARCHAR(255),
                         PRIMARY KEY (type_id)
);
CREATE TABLE t_users (
                          user_id BIGSERIAL NOT NULL,
                          user_name VARCHAR(255) ,
                          user_email VARCHAR(255),
                          type_id BIGINT NOT NULL,
                          FOREIGN KEY (type_id) REFERENCES t_types(type_id) ON DELETE CASCADE,
                          PRIMARY KEY (user_id)
);
