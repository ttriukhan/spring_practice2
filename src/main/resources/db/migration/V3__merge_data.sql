CREATE TABLE events_full (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    event_place VARCHAR(255),
    event_date DATE,
    start_time TIME,
    end_time TIME,
    price NUMERIC(10, 2),
    min_month_price NUMERIC(10,2),
    max_month_price NUMERIC(10,2)
);

INSERT INTO events_full (id, name, event_place, event_date, start_time, end_time, price, min_month_price, max_month_price)
SELECT
    e.id,
    e.name,
    e.event_place,
    e.event_date,
    e.start_time,
    e.end_time,
    e.price,
    es.min_month_price,
    es.max_month_price
FROM
    events e
LEFT JOIN
    events_summary es
ON
    TO_CHAR(e.event_date, 'YYYY-MM') = es.month_year;

ALTER TABLE tickets
DROP CONSTRAINT fk_event;

ALTER TABLE tickets
    ADD CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events_full(id);

DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS events_summary;


ALTER TABLE events_full RENAME TO events;

-- DROP TABLE IF EXISTS tickets;
-- DROP TABLE IF EXISTS events;
-- DROP TABLE IF EXISTS events_summary;
