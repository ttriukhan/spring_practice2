CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    event_place VARCHAR(255),
    event_date DATE,
    start_time TIME,
    end_time TIME,
    price NUMERIC(10, 2)
);

INSERT INTO events (name, event_place, event_date, start_time, end_time, price) VALUES
    ('K-POP Party', 'JuiceBar', '2025-04-13', '17:00', '22:00', 490.00),
    ('AnimeCon', 'Kyiv', '2025-07-01', '13:00', '22:00', 400.00),
    ('GikFest', 'Kyiv', '2025-08-15', '11:00', '19:00', 300.00),
    ('AnimeFest', 'Kyiv', '2025-07-10', '12:00', '20:00', 200.00),
    ('Party', 'Odesa', '2025-08-05', '15:00', '19:00', 100.00),
    ('ComicCon', 'Poltava', '2025-08-17', '13:00', '18:00', 500.00);

CREATE TABLE tickets (
    id SERIAL PRIMARY KEY,
    owner_name VARCHAR(255) NOT NULL,
    owner_instagram VARCHAR(255) NOT NULL,
    owner_age INT NOT NULL,
    is_vip BOOLEAN,
    event_id SERIAL,
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events(id)
);
