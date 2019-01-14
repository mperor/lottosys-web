package mapi.lotto.service;

import java.sql.Date;
import java.util.List;
import mapi.lotto.model.LottoPlusStatement;
import mapi.lotto.model.MathTicket;
import mapi.lotto.model.RandomTicket;
import mapi.lotto.model.Stat;
import mapi.lotto.model.StaticTicket;
import mapi.lotto.model.Ticket;

public interface LottoService {

    Stat getDynamicStatByTicketName(String name);

    MathTicket getFreeMathTicket();

    RandomTicket getFreeRandomTicket();

    StaticTicket getFreeStaticTicket();

    MathTicket getLastMathTicket();

    RandomTicket getLastRandomTicket();

    StaticTicket getLastStaticTicket();

    List<LottoPlusStatement> getLottoPlusStatements(Date from, Date to);

    Iterable<MathTicket> getMathTickets();

    Iterable<RandomTicket> getRandomTickets();

    Iterable<LottoPlusStatement> getStatements();

    List<Stat> getStaticStatsByTicketName(String name);

    Iterable<StaticTicket> getStaticTickets();

    List<LottoPlusStatement> getThisYearStatements();

    List<Ticket> getThisYearTickets(String name);

    Iterable<? extends Ticket> getTickets(String name);

    boolean saveStatement(int[] lotto, int[] plus, String date);

    boolean saveTicket(String name, int[] array);

}
