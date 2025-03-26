CREATE TABLE events_summary (
    month_year VARCHAR(7),
    min_month_price NUMERIC(10, 2),
    max_month_price NUMERIC(10, 2)
);

INSERT INTO events_summary (month_year, min_month_price, max_month_price)
SELECT
    TO_CHAR(event_date, 'YYYY-MM') AS month_year,  -- Use TO_CHAR for date formatting in PostgreSQL
    MIN(price) AS min_month_price,
    MAX(price) AS max_month_price
FROM
    events
GROUP BY
    TO_CHAR(event_date, 'YYYY-MM');
