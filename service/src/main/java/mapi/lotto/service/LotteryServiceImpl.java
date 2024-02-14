package mapi.lotto.service;

import lombok.RequiredArgsConstructor;
import mapi.lotto.model.result.LotteryResult;
import mapi.lotto.model.result.LottoNumbers;
import mapi.lotto.model.result.PlusNumbers;
import mapi.lotto.model.ticket.LotteryTicket;
import mapi.lotto.model.ticket.TicketNumbers;
import mapi.lotto.repository.LotteryResultRepository;
import mapi.lotto.repository.LotteryTicketRepository;
import mapi.lotto.util.TicketType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
@RequiredArgsConstructor
public class LotteryServiceImpl implements LotteryService {

    private final LotteryResultRepository resultRepository;
    private final LotteryTicketRepository ticketRepository;

    @Override
    public List<LotteryTicket> getTicketsOfYear(String name, Year year) {
        return ticketRepository.findAllByTicketTypeAndLotteryResult_LotteryDateBetween(
                name,
                year.atDay(1),
                year.atDay(year.length())
        );
    }

    @Override
    public List<LotteryTicket> getCurrentYearAndNewTickets(String name) {
        List<LotteryTicket> usedTickets = getTicketsOfYear(name, Year.now());
        List<LotteryTicket> newTickets = ticketRepository.findAllByTicketTypeAndLotteryResult(name, null);
        return Stream.of(usedTickets, newTickets)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<LotteryResult> getCurrentYearResults() {
        LocalDate now = LocalDate.now();
        return resultRepository.findByLotteryDateBetweenOrderByLotteryDate(
                now.with(firstDayOfYear()),
                now.with(lastDayOfYear())
        );
    }

    @Override
    public boolean saveTicketIfNewNotExist(String name, TicketNumbers ticketNumbers) {
        TicketType ticketType = TicketType.fromString(name);
        if (ticketRepository.existsByTicketTypeAndLotteryResultIsNull(ticketType.getName()))
            return false;

        return ticketRepository.save(ticketType.getCreator().apply(ticketNumbers)) != null;
    }

    @Override
    public boolean saveNewResultAndBindNewTickets(LottoNumbers lottoNumbers, PlusNumbers plusNumbers, LocalDate date) {
        if (resultRepository.existsByLotteryDateGreaterThanEqual(date))
            return false;

        LotteryResult result = resultRepository.save(new LotteryResult(date, lottoNumbers, plusNumbers));
        bindResultWithNewTickets(result);
        return true;
    }

    private void bindResultWithNewTickets(LotteryResult result) {
        List<LotteryTicket> tickets = ticketRepository.findAllByLotteryResult(null);
        tickets.forEach(lotteryTicket -> lotteryTicket.setLotteryResult(result));
        ticketRepository.saveAll(tickets);
    }

    @Override
    public Optional<LotteryTicket> findLatestTicketByName(String name) {
        return ticketRepository.findTop1ByTicketTypeOrderByLotteryResultLotteryDateAsc(name);
    }
}
