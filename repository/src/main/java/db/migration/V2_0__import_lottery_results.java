package db.migration;

import mapi.lotto.model.result.LotteryResult;
import mapi.lotto.model.result.LottoNumbers;
import mapi.lotto.model.result.PlusNumbers;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class V2_0__import_lottery_results extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        JdbcTemplate template = new JdbcTemplate();

        InputStream lottoCsvStream = V2_0__import_lottery_results.class.getResourceAsStream("lotto-results-2017-2024.csv");
        InputStream plusCsvStream = V2_0__import_lottery_results.class.getResourceAsStream("plus-results-2017-2024.csv");

        List<LotteryResult> resultsToImport;

        try (BufferedReader lottoReader = new BufferedReader(new InputStreamReader(lottoCsvStream));
             BufferedReader plusReader = new BufferedReader(new InputStreamReader(plusCsvStream))) {

            var resultMap = lottoReader.lines()
                    .skip(1)
                    .map(line -> line.split(";"))
                    .collect(Collectors.toMap(record -> record[0], this::parseToLotteryResult));


            plusReader.lines()
                    .skip(1)
                    .map(line -> line.split(";"))
                    .forEach(record -> resultMap.get(record[0]).setPlusNumbers(
                            new PlusNumbers(
                                    Integer.valueOf(record[4]),
                                    Integer.valueOf(record[5]),
                                    Integer.valueOf(record[6]),
                                    Integer.valueOf(record[7]),
                                    Integer.valueOf(record[8]),
                                    Integer.valueOf(record[9])
                            )));

            resultsToImport = resultMap.values().stream().collect(Collectors.toList());
        }

        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true))
                .batchUpdate("INSERT INTO lottery_result" +
                                " (lottery_date, l1, l2, l3, l4, l5, l6, p1, p2, p3, p4, p5, p6)" +
                                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(PreparedStatement ps, int i) throws SQLException {
                                LotteryResult result = resultsToImport.get(i);
                                ps.setObject(1, result.getLotteryDate());
                                ps.setInt(2, result.getLottoNumbers().getL1());
                                ps.setInt(3, result.getLottoNumbers().getL2());
                                ps.setInt(4, result.getLottoNumbers().getL3());
                                ps.setInt(5, result.getLottoNumbers().getL4());
                                ps.setInt(6, result.getLottoNumbers().getL5());
                                ps.setInt(7, result.getLottoNumbers().getL6());
                                ps.setInt(8, result.getPlusNumbers().getP1());
                                ps.setInt(9, result.getPlusNumbers().getP2());
                                ps.setInt(10, result.getPlusNumbers().getP3());
                                ps.setInt(11, result.getPlusNumbers().getP4());
                                ps.setInt(12, result.getPlusNumbers().getP5());
                                ps.setInt(13, result.getPlusNumbers().getP6());
                            }

                            @Override
                            public int getBatchSize() {
                                return resultsToImport.size();
                            }
                        }
                );
    }

    private LotteryResult parseToLotteryResult(String[] record) {
        LocalDate date = LocalDate.parse("%s-%02d-%02d".formatted(record[3], Integer.valueOf(record[2]), Integer.valueOf(record[1])));
        return new LotteryResult(
                date,
                new LottoNumbers(
                        Integer.valueOf(record[4]),
                        Integer.valueOf(record[5]),
                        Integer.valueOf(record[6]),
                        Integer.valueOf(record[7]),
                        Integer.valueOf(record[8]),
                        Integer.valueOf(record[9])
                ),
                PlusNumbers.EMPTY);
    }
}
