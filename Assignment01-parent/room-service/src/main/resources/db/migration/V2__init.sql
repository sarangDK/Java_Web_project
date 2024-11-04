CREATE TABLE t_rooms (
    room_id SERIAL PRIMARY KEY,
    room_name VARCHAR(255) NOT NULL,
    room_capacity VARCHAR(255) NOT NULL,
    room_availability BOOLEAN NOT NULL,
    room_features VARCHAR(255) NOT NULL
);