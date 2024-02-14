package mapi.lotto.repository;

import mapi.lotto.EmptyMigrationStrategyConfig;
import mapi.lotto.model.result.LotteryResult;
import mapi.lotto.model.result.LottoNumbers;
import mapi.lotto.model.result.PlusNumbers;
import mapi.lotto.model.ticket.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.Month;

import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static mapi.lotto.util.TicketType.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@EnableJpaRepositories(basePackages = "mapi.lotto.repository")
@EntityScan(basePackages = "mapi.lotto.model")
@ContextConfiguration(classes = {LotteryTicketRepository.class, LotteryResultRepository.class, EmptyMigrationStrategyConfig.class})
@DataJpaTest(properties = {
        "logging.level.org.springframework.test.context.transaction=DEBUG",
        "logging.level.org.hibernate.orm.jdbc.bind=TRACE",
})
class LotteryRepositoriesTest {

    @Autowired
    private LotteryTicketRepository ticketRepository;
    @Autowired
    private LotteryResultRepository resultRepository;

    @Test
    void findAllByTicketTypeOrderByIdDesc_noTickets_returnsEmptyList() {
        assertThat(ticketRepository.findAllByTicketTypeOrderByIdDesc(RANDOM))
                .isEmpty();
        assertThat(ticketRepository.findAllByTicketTypeOrderByIdDesc(MATH))
                .isEmpty();
        assertThat(ticketRepository.findAllByTicketTypeOrderByIdDesc(STATIC))
                .isEmpty();
    }

    @Test
    void findAllByTicketTypeOrderByIdDesc_someTicketExists_Tickets_returnsEmptyList() {
        ticketRepository.save(new RandomTicket(new TicketNumbers(1, 2, 3, 4, 5, 6)));
        ticketRepository.save(new StaticTicket(new TicketNumbers(1, 2, 3, 4, 5, 6)));
        ticketRepository.save(new MathTicket(new TicketNumbers(1, 2, 3, 4, 5, 6)));
        assertThat(ticketRepository.findAllByTicketTypeOrderByIdDesc(RANDOM))
                .hasSize(1);
        assertThat(ticketRepository.findAllByTicketTypeOrderByIdDesc(MATH))
                .hasSize(1);
        assertThat(ticketRepository.findAllByTicketTypeOrderByIdDesc(STATIC))
                .hasSize(1);
    }

    @Test
    void findAllByLotteryResult_ticketsWithNoResultExists_returnsTickets() {
        LotteryTicket[] tickets = new LotteryTicket[]{
                new RandomTicket(new TicketNumbers(1, 2, 3, 4, 5, 6)),
                new StaticTicket(new TicketNumbers(1, 2, 3, 4, 5, 6)),
                new MathTicket(new TicketNumbers(1, 2, 3, 4, 5, 6))
        };
        ticketRepository.saveAll(Arrays.stream(tickets).toList());

        assertThat(ticketRepository.findAllByLotteryResult(null))
                .contains(tickets);
    }

    @Test
    void findAllByLotteryResult_ticketsWithResultExists_returnsOnlyWithResult() {
        LotteryTicket[] noResultTickets = new LotteryTicket[]{
                new RandomTicket(new TicketNumbers(1, 2, 3, 4, 5, 6)),
                new StaticTicket(new TicketNumbers(1, 2, 3, 4, 5, 6)),
                new MathTicket(new TicketNumbers(1, 2, 3, 4, 5, 6))
        };

        LotteryResult result = new LotteryResult(
                LocalDate.now(),
                new LottoNumbers(1, 2, 3, 4, 5, 6),
                new PlusNumbers(1, 2, 3, 4, 5, 6)
        );
        var savedResult = resultRepository.save(result);

        LotteryTicket[] withResultTickets = new LotteryTicket[]{
                new RandomTicket(new TicketNumbers(1, 2, 3, 4, 5, 6), savedResult),
                new StaticTicket(new TicketNumbers(1, 2, 3, 4, 5, 6), savedResult),
                new MathTicket(new TicketNumbers(1, 2, 3, 4, 5, 6), savedResult)
        };

        ticketRepository.saveAll(Stream.of(noResultTickets, withResultTickets)
                .flatMap(Arrays::stream)
                .toList());

        assertThat(ticketRepository.findAllByLotteryResult(savedResult))
                .containsOnly(withResultTickets);
    }

    @Test
    void findAllByTicketTypeAndLotteryResultLotteryDateIn2024_oneTicketIn2024_returnsOnlyOneTicket() {
        LocalDate firstDayOf2024 = LocalDate.of(2024, Month.JANUARY, 1);
        LotteryResult result2024 = new LotteryResult(
                firstDayOf2024,
                new LottoNumbers(1, 2, 3, 4, 5, 6),
                new PlusNumbers(1, 2, 3, 4, 5, 6)
        );

        LotteryResult result2023 = new LotteryResult(
                LocalDate.of(2023, Month.DECEMBER, 31),
                new LottoNumbers(1, 2, 3, 4, 5, 6),
                new PlusNumbers(1, 2, 3, 4, 5, 6)
        );

        resultRepository.save(result2023);
        resultRepository.save(result2024);

        var random2023 = new RandomTicket(new TicketNumbers(1, 2, 3, 4, 5, 6), result2023);
        var random2024 = new RandomTicket(new TicketNumbers(1, 2, 3, 4, 5, 6), result2024);
        var newRandomWithoutYear = new RandomTicket(new TicketNumbers(1, 2, 3, 4, 5, 6));

        ticketRepository.save(random2023);
        ticketRepository.save(random2024);
        ticketRepository.save(newRandomWithoutYear);

        assertThat(ticketRepository.findAllByTicketTypeAndLotteryResult_LotteryDateBetween(
                RANDOM,
                firstDayOf2024,
                firstDayOf2024.with(TemporalAdjusters.lastDayOfYear())
        )).containsOnly(random2024);
    }

    @Test
    void existByTicketTypeAndLotteryResultIsNull_returnsTrueOnlyIfExist() {
        assertThat(ticketRepository.existsByTicketTypeAndLotteryResultIsNull(RANDOM)).isFalse();
        ticketRepository.save(new RandomTicket(new TicketNumbers(1, 2, 3, 4, 5, 6)));
        assertThat(ticketRepository.existsByTicketTypeAndLotteryResultIsNull(RANDOM)).isTrue();

        assertThat(ticketRepository.existsByTicketTypeAndLotteryResultIsNull(STATIC)).isFalse();
        ticketRepository.save(new StaticTicket(new TicketNumbers(1, 2, 3, 4, 5, 6)));
        assertThat(ticketRepository.existsByTicketTypeAndLotteryResultIsNull(STATIC)).isTrue();

        assertThat(ticketRepository.existsByTicketTypeAndLotteryResultIsNull(MATH)).isFalse();
        ticketRepository.save(new MathTicket(new TicketNumbers(1, 2, 3, 4, 5, 6)));
        assertThat(ticketRepository.existsByTicketTypeAndLotteryResultIsNull(MATH)).isTrue();
    }

    @Test
    void findByLotteryDateBetweenOrderByLotteryDate_returnsOnlyCurrentYearResults() {
        LocalDate startOf2023 = LocalDate.of(2023, Month.JANUARY, 1);
        LocalDate endOf2023 = LocalDate.of(2023, Month.DECEMBER, 31);
        var results2023 = List.of(
                new LotteryResult(
                        startOf2023,
                        new LottoNumbers(1, 2, 3, 4, 5, 6),
                        new PlusNumbers(1, 2, 3, 4, 5, 6)
                ),
                new LotteryResult(
                        endOf2023,
                        new LottoNumbers(1, 2, 3, 4, 5, 6),
                        new PlusNumbers(1, 2, 3, 4, 5, 6)
                )
        );

        LotteryResult result2022 = new LotteryResult(
                LocalDate.of(2022, Month.DECEMBER, 31),
                new LottoNumbers(1, 2, 3, 4, 5, 6),
                new PlusNumbers(1, 2, 3, 4, 5, 6)
        );
        LotteryResult result2024 = new LotteryResult(
                LocalDate.of(2024, Month.JANUARY, 1),
                new LottoNumbers(1, 2, 3, 4, 5, 6),
                new PlusNumbers(1, 2, 3, 4, 5, 6)
        );

        resultRepository.save(result2022);
        resultRepository.saveAll(results2023);
        resultRepository.save(result2024);

        assertThat(resultRepository.findByLotteryDateBetweenOrderByLotteryDate(startOf2023, endOf2023))
                .containsAll(results2023);
    }

    @Test
    void existsByLotteryDateGreaterThanEqual_returnsTrueOnlyIfNewerNotExist() {
        LotteryResult result2023 = new LotteryResult(
                LocalDate.of(2023, Month.JANUARY, 1),
                new LottoNumbers(1, 2, 3, 4, 5, 6),
                new PlusNumbers(1, 2, 3, 4, 5, 6)
        );
        LotteryResult result2024 = new LotteryResult(
                LocalDate.of(2024, Month.JANUARY, 1),
                new LottoNumbers(1, 2, 3, 4, 5, 6),
                new PlusNumbers(1, 2, 3, 4, 5, 6)
        );

        // before save any results
        assertThat(resultRepository.existsByLotteryDateGreaterThanEqual(null)).isFalse();
        assertThat(resultRepository.existsByLotteryDateGreaterThanEqual(LocalDate.of(2024, Month.JANUARY, 1))).isFalse();

        // after save results
        resultRepository.save(result2023);
        resultRepository.save(result2024);
        assertThat(resultRepository.existsByLotteryDateGreaterThanEqual(null)).isFalse();
        assertThat(resultRepository.existsByLotteryDateGreaterThanEqual(LocalDate.of(2023, Month.JANUARY, 1))).isTrue();
        assertThat(resultRepository.existsByLotteryDateGreaterThanEqual(LocalDate.of(2024, Month.JANUARY, 1))).isTrue();
        assertThat(resultRepository.existsByLotteryDateGreaterThanEqual(LocalDate.of(2024, Month.JANUARY, 2))).isFalse();
    }

}