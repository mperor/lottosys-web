package mapi.lotto.service;

import mapi.lotto.model.*;
import mapi.lotto.repository.MathTicketRepository;
import mapi.lotto.repository.RandomTicketRepository;
import mapi.lotto.repository.StatementRepository;
import mapi.lotto.repository.StaticTicketRepository;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LottoServiceTest {

    @Test
    void getThisYearTicketsByType_noNewTickets_returnsCorrectTickets() {
        // given
        var statementRepository = mock(StatementRepository.class);
        when(statementRepository.findByDateBetweenOrderByDate(any(), any()))
                .thenReturn(createMockStatements());
        var randomRepository = mock(RandomTicketRepository.class);
        var staticRepository = mock(StaticTicketRepository.class);
        var mathRepository = mock(MathTicketRepository.class);
        // no new tickets
        when(randomRepository.findTopByLottoPlusStatement(null)).thenReturn(null);
        when(staticRepository.findTopByLottoPlusStatement(null)).thenReturn(null);
        when(mathRepository.findTopByLottoPlusStatement(null)).thenReturn(null);
        // system under test
        LottoService lottoService = new LottoServiceImpl(statementRepository, randomRepository, staticRepository, mathRepository, null);
        // when
        List<Ticket> randomResult = lottoService.getThisYearTickets("random");
        List<Ticket> staticResult = lottoService.getThisYearTickets("static");
        List<Ticket> mathResult = lottoService.getThisYearTickets("math");
        // then
        assertThat(randomResult).isNotEmpty()
                .allMatch(RandomTicket.class::isInstance)
                .allMatch(ticket -> ticket.getN1() == 1)
                .extracting(Ticket::getLottoPlusStatement).isNotEmpty();
        assertThat(staticResult).isNotEmpty()
                .allMatch(StaticTicket.class::isInstance)
                .allMatch(ticket -> ticket.getN1() == 2)
                .extracting(Ticket::getLottoPlusStatement).isNotEmpty();
        assertThat(mathResult).isNotEmpty()
                .allMatch(MathTicket.class::isInstance)
                .allMatch(ticket -> ticket.getN1() == 3)
                .extracting(Ticket::getLottoPlusStatement).isNotEmpty();
    }

    @Test
    void getThisYearTicketsByType_newTicketsExist_returnsNewTickets() {
        // given
        var statementRepository = mock(StatementRepository.class);
        when(statementRepository.findByDateBetweenOrderByDate(any(), any()))
                .thenReturn(createMockStatements());

        var randomRepository = mock(RandomTicketRepository.class);
        var staticRepository = mock(StaticTicketRepository.class);
        var mathRepository = mock(MathTicketRepository.class);
        // new tickets exist
        when(randomRepository.findTopByLottoPlusStatement(null)).thenReturn(new RandomTicket(40, 41, 42, 43, 44, 45));
        when(staticRepository.findTopByLottoPlusStatement(null)).thenReturn(new StaticTicket(41, 42, 43, 44, 45, 46));
        when(mathRepository.findTopByLottoPlusStatement(null)).thenReturn(new MathTicket(42, 43, 44, 45, 46, 47));
        // system under test
        LottoService lottoService = new LottoServiceImpl(statementRepository, randomRepository, staticRepository, mathRepository, null);
        // when
        List<Ticket> randomResult = lottoService.getThisYearTickets("random");
        List<Ticket> staticResult = lottoService.getThisYearTickets("static");
        List<Ticket> mathResult = lottoService.getThisYearTickets("math");
        // then
        assertThat(randomResult).isNotEmpty()
                .allMatch(RandomTicket.class::isInstance)
                .anyMatch(ticket -> ticket.getLottoPlusStatement() == null)
                .map(Ticket::getTicketSet)
                .contains(Set.of(40, 41, 42, 43, 44, 45));
        assertThat(staticResult).isNotEmpty()
                .allMatch(StaticTicket.class::isInstance)
                .anyMatch(ticket -> ticket.getLottoPlusStatement() == null)
                .map(Ticket::getTicketSet)
                .contains(Set.of(41, 42, 43, 44, 45, 46));
        assertThat(mathResult).isNotEmpty()
                .allMatch(MathTicket.class::isInstance)
                .anyMatch(ticket -> ticket.getLottoPlusStatement() == null)
                .map(Ticket::getTicketSet)
                .contains(Set.of(42, 43, 44, 45, 46, 47));
    }

    private static List<LottoPlusStatement> createMockStatements() {
        LocalDate now = LocalDate.now();
        return List.of(
                new LottoPlusStatement(Date.valueOf(now.minusDays(2)),
                        new int[]{21, 22, 23, 24, 25, 26},
                        new int[]{1, 2, 3, 4, 5, 6},
                        new RandomTicket(1, 12, 13, 14, 15, 16),
                        new StaticTicket(2, 22, 23, 24, 25, 26),
                        new MathTicket(3, 32, 33, 34, 35, 36)
                ),
                new LottoPlusStatement(Date.valueOf(now.minusDays(1)),
                        new int[]{11, 12, 13, 14, 15, 16},
                        new int[]{1, 2, 3, 4, 5, 6},
                        new RandomTicket(1, 12, 13, 14, 15, 16),
                        new StaticTicket(2, 22, 23, 24, 25, 26),
                        new MathTicket(3, 32, 33, 34, 35, 36)
                ),
                new LottoPlusStatement(Date.valueOf(now),
                        new int[]{1, 2, 3, 4, 5, 6},
                        new int[]{1, 2, 3, 4, 5, 6},
                        new RandomTicket(1, 12, 13, 14, 15, 16),
                        new StaticTicket(2, 22, 23, 24, 25, 26),
                        new MathTicket(3, 32, 33, 34, 35, 36)
                )
        );
    }

    @Test
    void getThisYearStatements_returnsCorrectResults() {
        var statementRepository = mock(StatementRepository.class);
        Date startOf2024 = Date.valueOf("2024-01-01");
        when(statementRepository.findByDateBetweenOrderByDate(startOf2024, Date.valueOf("2024-12-31")))
                .thenReturn(List.of(new LottoPlusStatement(startOf2024,
                        new int[]{1, 2, 3, 4, 5, 6},
                        new int[]{11, 12, 13, 14, 15, 16},
                        new RandomTicket(11, 21, 31, 41, 45, 46),
                        new StaticTicket(12, 22, 32, 42, 45, 46),
                        new MathTicket(13, 23, 33, 43, 45, 46)
                )));
        LottoService lottoService = new LottoServiceImpl(statementRepository, null, null, null, null);
        List<LottoPlusStatement> statementResult = lottoService.getThisYearStatements();
        assertThat(statementResult)
                .map(LottoPlusStatement::getDate)
                .first()
                .isEqualTo(startOf2024);
    }

    @Test
    void getTicketsByType_returnsTickets() {
        var randomRepository = mock(RandomTicketRepository.class);
        RandomTicket randomTicket = new RandomTicket(1, 2, 3, 4, 5, 6);
        when(randomRepository.findAll()).thenReturn(List.of(randomTicket));

        var staticRepository = mock(StaticTicketRepository.class);
        StaticTicket staticTicket = new StaticTicket(1, 2, 3, 4, 5, 6);
        when(staticRepository.findAll()).thenReturn(List.of(staticTicket));

        var mathRepository = mock(MathTicketRepository.class);
        MathTicket mathTicket = new MathTicket(1, 2, 3, 4, 5, 6);
        when(mathRepository.findAll()).thenReturn(List.of(mathTicket));

        LottoService lottoService = new LottoServiceImpl(null, randomRepository, staticRepository, mathRepository, null);
        assertThat(lottoService.getTickets("random")).anyMatch(randomTicket::equals);
        assertThat(lottoService.getTickets("static")).anyMatch(staticTicket::equals);
        assertThat(lottoService.getTickets("math")).anyMatch(mathTicket::equals);
    }

    @Test
    void saveTicket_incorrectInput_returnsFalse() {
        LottoService lottoService = new LottoServiceImpl(null, null, null, null, null);
        // too few numbers
        assertThat(lottoService.saveTicket("random", new int[]{})).isFalse();
        // too many numbers
        assertThat(lottoService.saveTicket("random", new int[]{1, 2, 3, 4, 5, 6, 7, 8})).isFalse();
        // non-unique numbers
        assertThat(lottoService.saveTicket("random", new int[]{1, 1, 2, 3, 4, 5})).isFalse();
        // numbers outside the range <1,49>
        assertThat(lottoService.saveTicket("random", new int[]{1, 2, 3, 4, 5, 56})).isFalse();
        // incorrect ticket type name
        assertThat(lottoService.saveTicket("xyz", new int[]{1, 2, 3, 4, 5, 56})).isFalse();
    }

    @Test
    void saveTicket_correctInput_returnsTrue() {
        var randomRepository = mock(RandomTicketRepository.class);
        when(randomRepository.save(any())).thenAnswer(in -> in.getArgument(0));
        var staticRepository = mock(StaticTicketRepository.class);
        when(staticRepository.save(any())).thenAnswer(in -> in.getArgument(0));
        var mathRepository = mock(MathTicketRepository.class);
        when(mathRepository.save(any())).thenAnswer(in -> in.getArgument(0));

        LottoService lottoService = new LottoServiceImpl(null, randomRepository, staticRepository, mathRepository, null);
        assertThat(lottoService.saveTicket("random", new int[]{11, 12, 13, 14, 15, 16})).isTrue();
        assertThat(lottoService.saveTicket("static", new int[]{21, 22, 23, 24, 25, 26})).isTrue();
        assertThat(lottoService.saveTicket("math", new int[]{31, 32, 33, 34, 35, 36})).isTrue();
    }

    @Test
    void saveStatement_incorrectInputDate_returnsFalse() {
        var statementRepository = mock(StatementRepository.class);
        LottoPlusStatement lastDrawStatement = new LottoPlusStatement(
                Date.valueOf("2024-01-01"),
                new int[]{1, 2, 3, 4, 5, 6},
                new int[]{11, 12, 13, 14, 15, 16},
                new RandomTicket(11, 21, 31, 41, 45, 46),
                new StaticTicket(12, 22, 32, 42, 45, 46),
                new MathTicket(13, 23, 33, 43, 45, 46)
        );
        when(statementRepository.findTopByOrderByDateDesc()).thenReturn(lastDrawStatement);

        LottoService lottoService = new LottoServiceImpl(statementRepository, null, null, null, null);
        int[] correctLottoNumbers = {1, 2, 3, 4, 5, 6};
        int[] correctPlusNumbers = {1, 2, 3, 4, 5, 6};

        // date is null
        assertThat(lottoService.saveStatement(correctLottoNumbers, correctPlusNumbers, null)).isFalse();
        // invalid date format
        assertThat(lottoService.saveStatement(correctLottoNumbers, correctPlusNumbers, "01-01-2024")).isFalse();
        // input date is before or equal the date of the last draw
        assertThat(lottoService.saveStatement(correctLottoNumbers, correctPlusNumbers, "2024-01-01")).isFalse();
    }

    @Test
    void saveStatement_incorrectNumbers_returnsFalse() {
        var statementRepository = mock(StatementRepository.class);
        LottoPlusStatement lastDrawStatement = new LottoPlusStatement(
                Date.valueOf("2024-01-01"),
                new int[]{1, 2, 3, 4, 5, 6},
                new int[]{11, 12, 13, 14, 15, 16},
                new RandomTicket(11, 21, 31, 41, 45, 46),
                new StaticTicket(12, 22, 32, 42, 45, 46),
                new MathTicket(13, 23, 33, 43, 45, 46)
        );
        when(statementRepository.findTopByOrderByDateDesc()).thenReturn(lastDrawStatement);

        var randomRepository = mock(RandomTicketRepository.class);
        when(randomRepository.findTopByOrderByRandomTicketIdDesc()).thenReturn(new RandomTicket(1, 2, 3, 4, 5, 6));

        var staticRepository = mock(StaticTicketRepository.class);
        when(staticRepository.findTopByOrderByStaticTicketIdDesc()).thenReturn(new StaticTicket(1, 2, 3, 4, 5, 6));

        var mathRepository = mock(MathTicketRepository.class);
        when(mathRepository.findTopByOrderByMathTicketIdDesc()).thenReturn(new MathTicket(1, 2, 3, 4, 5, 6));

        LottoService lottoService = new LottoServiceImpl(statementRepository, randomRepository, staticRepository, mathRepository, null);
        String correctDate = "2024-01-02";
        int[] correctLottoNumbers = {1, 2, 3, 4, 5, 6};
        int[] correctPlusNumbers = {1, 2, 3, 4, 5, 6};

        // too few numbers
        int[] lowerThanSixNumbers = {};
        assertThat(lottoService.saveStatement(lowerThanSixNumbers, correctPlusNumbers, correctDate)).isFalse();
        assertThat(lottoService.saveStatement(correctLottoNumbers, lowerThanSixNumbers, correctDate)).isFalse();

        // too many numbers
        int[] moreThanSixNumbers = {1, 2, 3, 4, 5, 6, 7, 8};
        assertThat(lottoService.saveStatement(moreThanSixNumbers, correctPlusNumbers, correctDate)).isFalse();
        assertThat(lottoService.saveStatement(correctLottoNumbers, moreThanSixNumbers, correctDate)).isFalse();

        // non-unique numbers
        int[] nonUniqueNumbers = {1, 1, 2, 3, 4, 5};
        assertThat(lottoService.saveStatement(nonUniqueNumbers, correctPlusNumbers, correctDate)).isFalse();
        assertThat(lottoService.saveStatement(correctLottoNumbers, nonUniqueNumbers, correctDate)).isFalse();

        // numbers outside the range <1,49>
        int[] outsideRangeNumbers = {1, 2, 3, 4, 5, 50};
        assertThat(lottoService.saveStatement(outsideRangeNumbers, correctPlusNumbers, correctDate)).isFalse();
        assertThat(lottoService.saveStatement(correctLottoNumbers, outsideRangeNumbers, correctDate)).isFalse();
    }

    @Test
    void saveStatement_newTicketsDoNotExist_returnsFalse() {
        var statementRepository = mock(StatementRepository.class);
        when(statementRepository.findTopByOrderByDateDesc()).thenReturn(null);
        when(statementRepository.save(any())).thenAnswer(in -> in.getArgument(0));

        var randomRepository = mock(RandomTicketRepository.class);
        var staticRepository = mock(StaticTicketRepository.class);
        var mathRepository = mock(MathTicketRepository.class);

        LottoService lottoService = new LottoServiceImpl(statementRepository, randomRepository, staticRepository, mathRepository, null);

        when(randomRepository.findTopByOrderByRandomTicketIdDesc()).thenReturn(null);
        when(staticRepository.findTopByOrderByStaticTicketIdDesc()).thenReturn(null);
        when(mathRepository.findTopByOrderByMathTicketIdDesc()).thenReturn(null);
        assertThat(lottoService.saveStatement(new int[]{1, 2, 3, 4, 5, 6}, new int[]{1, 2, 3, 4, 5, 6}, "2024-01-01")).isFalse();

        LottoPlusStatement lastDrawStatement = new LottoPlusStatement(
                Date.valueOf("2024-01-01"),
                new int[]{1, 2, 3, 4, 5, 6},
                new int[]{11, 12, 13, 14, 15, 16},
                new RandomTicket(11, 21, 31, 41, 45, 46),
                new StaticTicket(12, 22, 32, 42, 45, 46),
                new MathTicket(13, 23, 33, 43, 45, 46)
        );

        var lastDrawRandomTicket = new RandomTicket(1, 2, 3, 4, 5, 6);
        lastDrawRandomTicket.setLottoPlusStatement(lastDrawStatement);

        var lastDrawStaticTicket = new StaticTicket(1, 2, 3, 4, 5, 6);
        lastDrawStaticTicket.setLottoPlusStatement(lastDrawStatement);

        var lastDrawMathTicket = new MathTicket(1, 2, 3, 4, 5, 6);
        lastDrawMathTicket.setLottoPlusStatement(lastDrawStatement);

        when(randomRepository.findTopByOrderByRandomTicketIdDesc()).thenReturn(lastDrawRandomTicket);
        when(staticRepository.findTopByOrderByStaticTicketIdDesc()).thenReturn(lastDrawStaticTicket);
        when(mathRepository.findTopByOrderByMathTicketIdDesc()).thenReturn(lastDrawMathTicket);
        assertThat(lottoService.saveStatement(new int[]{1, 2, 3, 4, 5, 6}, new int[]{1, 2, 3, 4, 5, 6}, "2024-01-01")).isFalse();
    }

    @Test
    void saveStatement_lastStatementDoesNotExist_returnsTrue() {
        var statementRepository = mock(StatementRepository.class);
        when(statementRepository.findTopByOrderByDateDesc()).thenReturn(null);
        when(statementRepository.save(any())).thenAnswer(in -> in.getArgument(0));
        var randomRepository = mock(RandomTicketRepository.class);
        when(randomRepository.findTopByOrderByRandomTicketIdDesc()).thenReturn(new RandomTicket(1, 2, 3, 4, 5, 6));
        var staticRepository = mock(StaticTicketRepository.class);
        when(staticRepository.findTopByOrderByStaticTicketIdDesc()).thenReturn(new StaticTicket(1, 2, 3, 4, 5, 6));
        var mathRepository = mock(MathTicketRepository.class);
        when(mathRepository.findTopByOrderByMathTicketIdDesc()).thenReturn(new MathTicket(1, 2, 3, 4, 5, 6));

        LottoService lottoService = new LottoServiceImpl(statementRepository, randomRepository, staticRepository, mathRepository, null);
        assertThat(lottoService.saveStatement(new int[]{1, 2, 3, 4, 5, 6}, new int[]{1, 2, 3, 4, 5, 6}, "2024-01-01")).isTrue();
    }

}