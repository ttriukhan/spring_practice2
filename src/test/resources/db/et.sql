DELETE FROM events;
DELETE FROM tickets;

INSERT INTO events (id, name, event_place, event_date, start_time, end_time, price) VALUES
    ('1', 'K-POP Party', 'JuiceBar', '2022-04-13', '17:00', '22:00', 490.00),
    ('2', 'AnimeCon', 'Kyiv', '2023-07-01', '13:00', '22:00', 400.00),
    ('3', 'GikFest', 'Kyiv', '2024-08-15', '11:00', '19:00', 300.00),
    ('4', 'AnimeFest', 'Kyiv', '2024-07-10', '12:00', '20:00', 200.00),
    ('5', 'Party', 'Odesa', '2025-08-05', '15:00', '19:00', 100.00),
    ('6', 'ComicCon', 'Poltava', '2025-08-17', '13:00', '18:00', 500.00),
    ('7', 'GameCon', 'Poltava', '2025-08-17', '13:00', '18:00', 500.00),
    ('8', 'AnimeFest2', 'Kyiv', '2024-07-11', '12:00', '20:00', 200.00),
    ('9', 'AnimeFest3', 'Kyiv', '2024-07-12', '12:00', '20:00', 200.00);


-- Insert tickets for the 'AnimeFest' event (event_id = 4)
INSERT INTO tickets (id, owner_name, owner_instagram, owner_age, is_vip, event_id) VALUES
    ('2100', 'Paul', 'paul_instagram', 30, TRUE, 4),
    ('2101', 'Quincy', 'quincy_instagram', 28, FALSE, 4),
    ('2102', 'Rita', 'rita_instagram', 25, TRUE, 4);

-- Insert tickets for the 'Party' event (event_id = 5)
INSERT INTO tickets (id, owner_name, owner_instagram, owner_age, is_vip, event_id) VALUES
    ('100', 'Ursula', 'ursula_instagram', 22, TRUE, 5),
    ('101', 'Victor', 'victor_instagram', 23, FALSE, 5),
    ('102', 'Wendy', 'wendy_instagram', 27, TRUE, 5),
    ('103', 'Xander', 'xander_instagram', 24, FALSE, 5),
    ('104', 'Yvonne', 'yvonne_instagram', 20, TRUE, 5);

-- Insert tickets for the 'ComicCon' event (event_id = 6)
INSERT INTO tickets (id, owner_name, owner_instagram, owner_age, is_vip, event_id) VALUES
    ('1100', 'Zack', 'zack_instagram', 30, FALSE, 6),
    ('1101', 'Anna', 'anna_instagram', 25, TRUE, 6),
    ('1102', 'Ben', 'ben_instagram', 26, FALSE, 6),
    ('1103', 'Clara', 'clara_instagram', 22, TRUE, 6),
    ('1104', 'David', 'david_comic_instagram', 28, FALSE, 6);
