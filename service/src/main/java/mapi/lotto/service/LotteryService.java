package mapi.lotto.service;

import mapi.lotto.model.result.LotteryResult;
import mapi.lotto.model.result.LottoNumbers;
import mapi.lotto.model.result.PlusNumbers;
import mapi.lotto.model.ticket.LotteryTicket;
import mapi.lotto.model.ticket.TicketNumbers;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface LotteryService {

    List<LotteryTicket> getTicketsOfYear(String name, Year year);

    List<LotteryTicket> getCurrentYearAndNewTickets(String name);

    List<LotteryResult> getCurrentYearResults();

    boolean saveTicketIfNewNotExist(String name, TicketNumbers ticketNumbers);

    boolean saveNewResultAndBindNewTickets(LottoNumbers lottoNumbers, PlusNumbers plusNumbers, LocalDate date);

    Optional<LotteryTicket> findLatestTicketByName(String name);
}
