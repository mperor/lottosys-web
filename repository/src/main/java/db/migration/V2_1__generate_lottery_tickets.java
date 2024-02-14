package db.migration;

import mapi.lotto.model.LotteryNumbers;
import mapi.lotto.model.result.LottoNumbers;
import mapi.lotto.model.result.PlusNumbers;
import mapi.lotto.model.ticket.TicketNumbers;
import mapi.lotto.util.TicketType;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class V2_1__generate_lottery_tickets extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
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
                                var numberIterator = generateRandomNumbers(6).iterator();

                                ps.setInt(1, numberIterator.next());
                                ps.setInt(2, numberIterator.next());
                                ps.setInt(3, numberIterator.next());
                                ps.setInt(4, numberIterator.next());
                                ps.setInt(5, numberIterator.next());
                                ps.setInt(6, numberIterator.next());
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
                                    var numberIterator = generateRandomNumbers(6).iterator();
                                    ticketNumbers = new TicketNumbers(
                                            numberIterator.next(),
                                            numberIterator.next(),
                                            numberIterator.next(),
                                            numberIterator.next(),
                                            numberIterator.next(),
                                            numberIterator.next()
                                    );
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
                                var numberIterator = generateDeltaNumbers().iterator();

                                ps.setInt(1, numberIterator.next());
                                ps.setInt(2, numberIterator.next());
                                ps.setInt(3, numberIterator.next());
                                ps.setInt(4, numberIterator.next());
                                ps.setInt(5, numberIterator.next());
                                ps.setInt(6, numberIterator.next());
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

    private List<Integer> generateRandomNumbers(int limit) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.ints(1, 50)
                .distinct()
                .limit(6)
                .sorted()
                .boxed()
                .collect(Collectors.toList());
    }

    private List<Integer> generateDeltaNumbers() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return Stream.of(
                        random.nextInt(1, 3),
                        random.nextInt(1, 6),
                        random.nextInt(1, 7),
                        random.nextInt(4, 8),
                        random.nextInt(6, 10),
                        random.nextInt(8, 15)
                ).map(integer -> Pair.of(integer, random.nextDouble()))
                .sorted(Comparator.comparingDouble(Pair::getSecond))
                .map(Pair::getFirst)
                .collect(sumPairsOfNumbersToList());
    }

    private static Collector<Integer, ArrayList<Integer>, ArrayList<Integer>> sumPairsOfNumbersToList() {
        return Collector.of(
                ArrayList::new,
                (list, element) -> {
                    if (!list.isEmpty())
                        list.add(list.get(list.size() - 1) + element);
                    else
                        list.add(element);
                },
                (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                }
        );
    }

    private record Result(Long id, LottoNumbers lottoNumbers, PlusNumbers plusNumbers) {
    }

}
