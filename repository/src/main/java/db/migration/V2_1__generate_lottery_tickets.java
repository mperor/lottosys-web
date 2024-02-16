package db.migration;

import mapi.lotto.model.LotteryNumbers;
import mapi.lotto.model.result.LottoNumbers;
import mapi.lotto.model.result.PlusNumbers;
import mapi.lotto.model.ticket.TicketNumbers;
import mapi.lotto.util.TicketType;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class V2_1__generate_lottery_tickets extends BaseJavaMigration {

    @Override
    public void migrate(Context context) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));
        List<Result> results = jdbcTemplate.queryForStream(
                        "SELECT id, l1, l2, l3, l4, l5, l6, p1, p2, p3, p4, p5, p6 FROM lottery_result order by lottery_date asc",
                        (rowMapper, rowNum) -> new Result(
                                rowMapper.getLong(1),
                                new LottoNumbers(
                                        rowMapper.getInt(2),
                                        rowMapper.getInt(3),
                                        rowMapper.getInt(4),
                                        rowMapper.getInt(5),
                                        rowMapper.getInt(6),
                                        rowMapper.getInt(7)
                                ),
                                new PlusNumbers(
                                        rowMapper.getInt(8),
                                        rowMapper.getInt(9),
                                        rowMapper.getInt(10),
                                        rowMapper.getInt(11),
                                        rowMapper.getInt(12),
                                        rowMapper.getInt(13)
                                )
                        ))
                .collect(Collectors.toList());

        insertRandomTickets(jdbcTemplate, results);
        insertStaticTickets(jdbcTemplate, results);
        insertMathTickets(jdbcTemplate, results);
    }

    private void insertRandomTickets(JdbcTemplate jdbcTemplate, List<Result> lotteryIds) {
        jdbcTemplate
                .batchUpdate("INSERT INTO lottery_ticket" +
                                " (n1, n2, n3, n4, n5, n6, lottery_result_id, ticket_type)" +
                                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(PreparedStatement ps, int i) throws SQLException {
                                var id = lotteryIds.get(i).id;
                                var ticketNumbers = TicketNumbers.generateRandomNumbers();
                                ps.setInt(1, ticketNumbers.getN1());
                                ps.setInt(2, ticketNumbers.getN2());
                                ps.setInt(3, ticketNumbers.getN3());
                                ps.setInt(4, ticketNumbers.getN4());
                                ps.setInt(5, ticketNumbers.getN5());
                                ps.setInt(6, ticketNumbers.getN6());
                                ps.setLong(7, id);
                                ps.setString(8, TicketType.Constants.RANDOM);
                            }

                            @Override
                            public int getBatchSize() {
                                return lotteryIds.size();
                            }
                        }
                );
    }

    private void insertStaticTickets(JdbcTemplate jdbcTemplate, List<Result> lotteryIds) {
        jdbcTemplate
                .batchUpdate("INSERT INTO lottery_ticket" +
                                " (n1, n2, n3, n4, n5, n6, lottery_result_id, ticket_type)" +
                                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                        new BatchPreparedStatementSetter() {

                            private LotteryNumbers lastLotteryNumbers;
                            private PlusNumbers lastPlusNumbers;
                            private TicketNumbers ticketNumbers;

                            @Override
                            public void setValues(PreparedStatement ps, int i) throws SQLException {
                                if (lastLotteryNumbers == null
                                        || ticketNumbers.countCommonNumbers(lastLotteryNumbers) >= 2
                                        || ticketNumbers.countCommonNumbers(lastPlusNumbers) >= 2) {
                                    ticketNumbers = TicketNumbers.generateRandomNumbers();
                                }

                                var id = lotteryIds.get(i).id;
                                ps.setInt(1, ticketNumbers.getN1());
                                ps.setInt(2, ticketNumbers.getN2());
                                ps.setInt(3, ticketNumbers.getN3());
                                ps.setInt(4, ticketNumbers.getN4());
                                ps.setInt(5, ticketNumbers.getN5());
                                ps.setInt(6, ticketNumbers.getN6());
                                ps.setLong(7, id);
                                ps.setString(8, TicketType.Constants.STATIC);

                                lastLotteryNumbers = lotteryIds.get(i).lottoNumbers;
                                lastPlusNumbers = lotteryIds.get(i).plusNumbers;
                            }

                            @Override
                            public int getBatchSize() {
                                return lotteryIds.size();
                            }
                        }
                );
    }

    private void insertMathTickets(JdbcTemplate jdbcTemplate, List<Result> lotteryIds) {
        jdbcTemplate
                .batchUpdate("INSERT INTO lottery_ticket" +
                                " (n1, n2, n3, n4, n5, n6, lottery_result_id, ticket_type)" +
                                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(PreparedStatement ps, int i) throws SQLException {
                                var id = lotteryIds.get(i).id;
                                var ticketNumbers = TicketNumbers.generateDeltaNumbers();
                                ps.setInt(1, ticketNumbers.getN1());
                                ps.setInt(2, ticketNumbers.getN2());
                                ps.setInt(3, ticketNumbers.getN3());
                                ps.setInt(4, ticketNumbers.getN4());
                                ps.setInt(5, ticketNumbers.getN5());
                                ps.setInt(6, ticketNumbers.getN6());
                                ps.setLong(7, id);
                                ps.setString(8, TicketType.Constants.MATH);
                            }

                            @Override
                            public int getBatchSize() {
                                return lotteryIds.size();
                            }
                        }
                );
    }

    private record Result(Long id, LottoNumbers lottoNumbers, PlusNumbers plusNumbers) {
    }

}
