SELECT SETVAL(pg_get_serial_sequence('lotto_plus_statement', 'lps_id'), (SELECT MAX(lps_id) FROM lotto_plus_statement));
SELECT SETVAL(pg_get_serial_sequence('random_ticket', 'random_ticket_id'), (SELECT MAX(random_ticket_id) FROM random_ticket));
SELECT SETVAL(pg_get_serial_sequence('math_ticket', 'math_ticket_id'), (SELECT MAX(math_ticket_id) FROM math_ticket));
SELECT SETVAL(pg_get_serial_sequence('static_ticket', 'static_ticket_id'), (SELECT MAX(static_ticket_id) FROM static_ticket));
SELECT SETVAL(pg_get_serial_sequence('stats', 'id'), (SELECT MAX(id) FROM stats));