ALTER TABLE lotto_plus_statement ALTER COLUMN lps_id RESTART WITH (SELECT MAX(lps_id) FROM lotto_plus_statement) + 1;
ALTER TABLE random_ticket ALTER COLUMN random_ticket_id RESTART WITH (SELECT MAX(random_ticket_id) FROM random_ticket) + 1;
ALTER TABLE math_ticket ALTER COLUMN math_ticket_id RESTART WITH (SELECT MAX(math_ticket_id) FROM math_ticket) + 1;
ALTER TABLE static_ticket ALTER COLUMN static_ticket_id RESTART WITH (SELECT MAX(static_ticket_id) FROM static_ticket) + 1;
ALTER TABLE stats ALTER COLUMN id RESTART WITH (SELECT MAX(id) FROM stats) + 1;
