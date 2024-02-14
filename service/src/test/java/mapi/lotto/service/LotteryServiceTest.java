package mapi.lotto.service;

import mapi.lotto.model.result.LotteryResult;
import mapi.lotto.model.result.LottoNumbers;
import mapi.lotto.model.result.PlusNumbers;
import mapi.lotto.model.ticket.*;
import mapi.lotto.repository.LotteryResultRepository;
import mapi.lotto.repository.LotteryTicketRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.*;

class LotteryServiceTest {

    @Test
    void getCurrentYearAndNewTickets_noNewTickets_returnsCurrentYearTickets() {
        // given
        var ticketRepository = mock(LotteryTicketRepository.class);
        when(ticketRepository.findAllByTicketTypeAndLotteryResult_LotteryDateBetween(eq("random"), any(), any()))
                .thenReturn(createMockRandomTickets());
        when(ticketRepository.findAllByTicketTypeAndLotteryResult_LotteryDateBetween(eq("static"), any(), any()))
                .thenReturn(createMockStaticTickets());
        when(ticketRepository.findAllByTicketTypeAndLotteryResult_LotteryDateBetween(eq("math"), any(), any()))
                .thenReturn(createMockMathTickets());
        // no new tickets
        when(ticketRepository.findAllByTicketTypeAndLotteryResult(eq("random"), isNull()))
                .thenReturn(Collections.emptyList());
        when(ticketRepository.findAllByTicketTypeAndLotteryResult(eq("static"), isNull()))
                .thenReturn(Collections.emptyList());
        when(ticketRepository.findAllByTicketTypeAndLotteryResult(eq("math"), isNull()))
                .thenReturn(Collections.emptyList());
        // system under test
        LotteryService lotteryService = new LotteryServiceImpl(null, ticketRepository);
        // when
        List<LotteryTicket> randomResult = lotteryService.getCurrentYearAndNewTickets("random");
        List<LotteryTicket> mathResult = lotteryService.getCurrentYearAndNewTickets("math");
        List<LotteryTicket> staticResult = lotteryService.getCurrentYearAndNewTickets("static");
        // then
        assertThat(randomResult).isNotEmpty()
                .allMatch(RandomTicket.class::isInstance)
                .allMatch(ticket -> ticket.getTicketNumbers().getN1() == 1)
                .extracting(LotteryTicket::getLotteryResult).isNotEmpty();
        assertThat(mathResult).isNotEmpty()
                .allMatch(MathTicket.class::isInstance)
                .allMatch(ticket -> ticket.getTicketNumbers().getN1() == 2)
                .extracting(LotteryTicket::getLotteryResult).isNotEmpty();
        assertThat(staticResult).isNotEmpty()
                .allMatch(StaticTicket.class::isInstance)
                .allMatch(ticket -> ticket.getTicketNumbers().getN1() == 3)
                .extracting(LotteryTicket::getLotteryResult).isNotEmpty();
    }

    @Test
    void getCurrentYearAndNewTickets_onlyNewTicketsExist_returnsNewTickets() {
        // given
        var ticketRepository = mock(LotteryTicketRepository.class);
        when(ticketRepository.findAllByTicketTypeAndLotteryResult_LotteryDateBetween(eq("random"), any(), any()))
                .thenReturn(Collections.emptyList());
        when(ticketRepository.findAllByTicketTypeAndLotteryResult_LotteryDateBetween(eq("static"), any(), any()))
                .thenReturn(Collections.emptyList());
        when(ticketRepository.findAllByTicketTypeAndLotteryResult_LotteryDateBetween(eq("math"), any(), any()))
                .thenReturn(Collections.emptyList());
        // new tickets exist
        when(ticketRepository.findAllByTicketTypeAndLotteryResult(eq("random"), isNull()))
                .thenReturn(List.of(new RandomTicket(new TicketNumbers(40, 41, 42, 43, 44, 45))));
        when(ticketRepository.findAllByTicketTypeAndLotteryResult(eq("static"), isNull()))
                .thenReturn(List.of(new StaticTicket(new TicketNumbers(41, 42, 43, 44, 45, 46))));
        when(ticketRepository.findAllByTicketTypeAndLotteryResult(eq("math"), isNull()))
                .thenReturn(List.of(new MathTicket(new TicketNumbers(42, 43, 44, 45, 46, 47))));
        // system under test
        LotteryService lotteryService = new LotteryServiceImpl(null, ticketRepository);
        // when
        List<LotteryTicket> randomResult = lotteryService.getCurrentYearAndNewTickets("random");
        List<LotteryTicket> mathResult = lotteryService.getCurrentYearAndNewTickets("math");
        List<LotteryTicket> staticResult = lotteryService.getCurrentYearAndNewTickets("static");
        // then
        assertThat(randomResult).isNotEmpty()
                .allMatch(RandomTicket.class::isInstance)
                .anyMatch(ticket -> ticket.getLotteryResult() == null)
                .map(LotteryTicket::getTicketNumbers)
                .map(TicketNumbers::toArray)
                .containsExactly(new int[]{40, 41, 42, 43, 44, 45});
        assertThat(staticResult).isNotEmpty()
                .allMatch(StaticTicket.class::isInstance)
                .anyMatch(ticket -> ticket.getLotteryResult() == null)
                .map(LotteryTicket::getTicketNumbers)
                .map(TicketNumbers::toArray)
                .containsExactly(new int[]{41, 42, 43, 44, 45, 46});
        assertThat(mathResult).isNotEmpty()
                .allMatch(MathTicket.class::isInstance)
                .anyMatch(ticket -> ticket.getLotteryResult() == null)
                .map(LotteryTicket::getTicketNumbers)
                .map(TicketNumbers::toArray)
                .containsExactly(new int[]{42, 43, 44, 45, 46, 47});
    }

    private static List<LotteryTicket> createMockRandomTickets() {
        LotteryResult[] results = createThreeLotteryResults();
        var randoms = new RandomTicket[]{
                new RandomTicket(new TicketNumbers(1, 2, 3, 4, 5, 6), results[0]),
                new RandomTicket(new TicketNumbers(1, 12, 13, 14, 15, 16), results[1]),
                new RandomTicket(new TicketNumbers(1, 22, 23, 24, 25, 26), results[2])
        };
        return Arrays.asList(randoms);
    }

    private static List<LotteryTicket> createMockMathTickets() {
        LotteryResult[] results = createThreeLotteryResults();
        var maths = new MathTicket[]{
                new MathTicket(new TicketNumbers(2, 12, 13, 14, 15, 16), results[0]),
                new MathTicket(new TicketNumbers(2, 22, 23, 24, 25, 26), results[1]),
                new MathTicket(new TicketNumbers(2, 32, 33, 34, 35, 36), results[2])
        };
        return Arrays.asList(maths);
    }

    private static List<LotteryTicket> createMockStaticTickets() {
        LotteryResult[] results = createThreeLotteryResults();
        var statics = new StaticTicket[]{
                new StaticTicket(new TicketNumbers(3, 12, 13, 14, 15, 16), results[0]),
                new StaticTicket(new TicketNumbers(3, 22, 23, 24, 25, 26), results[1]),
                new StaticTicket(new TicketNumbers(3, 32, 33, 34, 35, 36), results[2])
        };
        return Arrays.asList(statics);
    }

    private static LotteryResult[] createThreeLotteryResults() {
        LocalDate now = LocalDate.now();
        var results = new LotteryResult[]{
                new LotteryResult((now.minusDays(2)),
                        new LottoNumbers(21, 22, 23, 24, 25, 26),
                        new PlusNumbers(1, 2, 3, 4, 5, 6)
                ),
                new LotteryResult(now.minusDays(1),
                        new LottoNumbers(11, 12, 13, 14, 15, 16),
                        new PlusNumbers(1, 2, 3, 4, 5, 6)
                ),
                new LotteryResult(now,
                        new LottoNumbers(1, 2, 3, 4, 5, 6),
                        new PlusNumbers(1, 2, 3, 4, 5, 6)
                )
        };
        return results;
    }

    @Test
    void getCurrentYearResults_returnsCorrectResults() {
        // given
        var resultRepository = mock(LotteryResultRepository.class);
        LocalDate now = LocalDate.now();
        LocalDate startOfCurrentYear = now.with(firstDayOfYear());
        LocalDate endOfCurrentYear = now.with(lastDayOfYear());
        LotteryResult result = new LotteryResult(
                startOfCurrentYear,
                new LottoNumbers(1, 2, 3, 4, 5, 6),
                new PlusNumbers(11, 12, 13, 14, 15, 16)
        );
        when(resultRepository.findByLotteryDateBetweenOrderByLotteryDate(
                eq(startOfCurrentYear),
                eq(endOfCurrentYear))
        ).thenReturn(List.of(result));
        // system under test
        LotteryService lotteryService = new LotteryServiceImpl(resultRepository, null);
        // when
        List<LotteryResult> statementResult = lotteryService.getCurrentYearResults();
        // then
        assertThat(statementResult)
                .map(LotteryResult::getLotteryDate)
                .first()
                .isEqualTo(startOfCurrentYear);
    }

    @Test
    void saveTicket_incorrectName_returnsFalse() {
        LotteryService lotteryService = new LotteryServiceImpl(null, null);
        assertThatIllegalArgumentException().isThrownBy(() -> lotteryService.saveTicketIfNewNotExist("xyz", new TicketNumbers(1, 2, 3, 4, 5, 6)));
    }

    @Test
    void saveTicket_newTicketAlreadySaved_returnsFalse() {
        var ticketRepository = mock(LotteryTicketRepository.class);
        // new tickets exists
        when(ticketRepository.findAllByTicketTypeAndLotteryResult(eq("random"), isNull()))
                .thenReturn(List.of(new RandomTicket(new TicketNumbers(40, 41, 42, 43, 44, 45))));
        when(ticketRepository.findAllByTicketTypeAndLotteryResult(eq("static"), isNull()))
                .thenReturn(List.of(new StaticTicket(new TicketNumbers(41, 42, 43, 44, 45, 46))));
        when(ticketRepository.findAllByTicketTypeAndLotteryResult(eq("math"), isNull()))
                .thenReturn(List.of(new MathTicket(new TicketNumbers(42, 43, 44, 45, 46, 47))));

        LotteryService lotteryService = new LotteryServiceImpl(null, null);
        assertThatIllegalArgumentException().isThrownBy(() -> lotteryService.saveTicketIfNewNotExist("xyz", new TicketNumbers(1, 2, 3, 4, 5, 6)));
    }

    @Test
    void saveTicketIfNewNotExist_newNotExist_returnsTrue() {
        // given
        var ticketRepository = mock(LotteryTicketRepository.class);
        when(ticketRepository.save(any())).thenAnswer(in -> in.getArgument(0));
        when(ticketRepository.existsByTicketTypeAndLotteryResultIsNull(any())).thenReturn(false);
        LotteryService lotteryService = new LotteryServiceImpl(null, ticketRepository);
        // when + then
        assertThat(lotteryService.saveTicketIfNewNotExist("random", new TicketNumbers(11, 12, 13, 14, 15, 16))).isTrue();
        assertThat(lotteryService.saveTicketIfNewNotExist("static", new TicketNumbers(21, 22, 23, 24, 25, 26))).isTrue();
        assertThat(lotteryService.saveTicketIfNewNotExist("math", new TicketNumbers(31, 32, 33, 34, 35, 36))).isTrue();
    }

    @Test
    void saveNewResultAndBindNewTickets_isNotNewLotteryResult_returnsFalse() {
        // given
        var resultRepository = mock(LotteryResultRepository.class);
        when(resultRepository.existsByLotteryDateGreaterThanEqual(notNull()))
                .thenReturn(true);
        LotteryService lotteryService = new LotteryServiceImpl(resultRepository, null);
        // when + then
        assertThat(lotteryService.saveNewResultAndBindNewTickets(new LottoNumbers(1, 2, 3, 4, 5, 6), new PlusNumbers(1, 2, 3, 4, 5, 6), LocalDate.now()))
                .isFalse();
    }

    @Test
    void saveNewResultAndBindNewTickets_isNewLotteryResultAndNewTicketsNotExist_returnsTrue() {
        // given
        var resultRepository = mock(LotteryResultRepository.class);
        when(resultRepository.existsByLotteryDateGreaterThanEqual(notNull()))
                .thenReturn(false);
        when(resultRepository.save(any())).thenAnswer(in -> in.getArgument(0));
        var ticketRepository = mock(LotteryTicketRepository.class);
        when(ticketRepository.findAllByLotteryResult(isNull()))
                .thenReturn(Collections.emptyList());
        LotteryService lotteryService = new LotteryServiceImpl(resultRepository, ticketRepository);
        // when + then
        assertThat(lotteryService.saveNewResultAndBindNewTickets(new LottoNumbers(1, 2, 3, 4, 5, 6), new PlusNumbers(1, 2, 3, 4, 5, 6), LocalDate.now()))
                .isTrue();
    }

    @Test
    void saveNewResultAndBindNewTickets_isNewLotteryResultAndNewTicketsExist_returnsTrue() {
        // given
        var resultRepository = mock(LotteryResultRepository.class);
        when(resultRepository.existsByLotteryDateGreaterThanEqual(notNull()))
                .thenReturn(false);
        when(resultRepository.save(any())).thenAnswer(in -> in.getArgument(0));
        var ticketRepository = mock(LotteryTicketRepository.class);
        List<LotteryTicket> newTickets = List.of(
                new RandomTicket(new TicketNumbers(1, 2, 3, 4, 5, 6)),
                new MathTicket(new TicketNumbers(1, 12, 13, 14, 15, 16)),
                new StaticTicket(new TicketNumbers(1, 22, 23, 24, 25, 26))
        );
        when(ticketRepository.findAllByLotteryResult(isNull())).thenReturn(newTickets);
        LotteryService lotteryService = new LotteryServiceImpl(resultRepository, ticketRepository);
        // when + then
        assertThat(lotteryService.saveNewResultAndBindNewTickets(new LottoNumbers(1, 2, 3, 4, 5, 6), new PlusNumbers(1, 2, 3, 4, 5, 6), LocalDate.now()))
                .isTrue();
        assertThat(newTickets).map(LotteryTicket::getLotteryResult)
                .isNotEmpty();
    }

}