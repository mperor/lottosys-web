package mapi.lotto.service;

import mapi.lotto.model.*;
import mapi.lotto.repository.*;
import mapi.lotto.util.LottoUtil;
import mapi.lotto.util.Tickets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class LottoServiceImpl implements LottoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LottoServiceImpl.class);

	private final StatementRepository statementRepository;
	private final RandomTicketRepository randomTicketRepository;
	private final StaticTicketRepository staticTicketRepository;
	private final MathTicketRepository mathTicketRepository;

	public LottoServiceImpl(StatementRepository statementRepository, RandomTicketRepository randomTicketRepository, StaticTicketRepository staticTicketRepository, MathTicketRepository mathTicketRepository) {
		this.statementRepository = statementRepository;
		this.randomTicketRepository = randomTicketRepository;
		this.staticTicketRepository = staticTicketRepository;
		this.mathTicketRepository = mathTicketRepository;
	}

	@Override
    public Iterable<LottoPlusStatement> getStatements() {
	return statementRepository.findAll();
    }

	public Iterable<StaticTicket> getStaticTickets() {
		return staticTicketRepository.findAll();
	}

	@Override
    public List<Ticket> getThisYearTickets(String name) {
	List<LottoPlusStatement> statements = getThisYearStatements();
	List<Ticket> tickets = new LinkedList<>();
	Ticket freeTicket = null;

	switch (Tickets.fromString(name)) {
	    case RANDOM:
		for (LottoPlusStatement lps : statements) {
		    tickets.add(lps.getRandomTicket());
		}
		freeTicket = getFreeRandomTicket();
		if (freeTicket != null) {
		    tickets.add(freeTicket);
		}
		break;
	    case STATIC:
		for (LottoPlusStatement lps : statements) {
		    tickets.add(lps.getStaticTicket());
		}
		freeTicket = getFreeStaticTicket();
		if (freeTicket != null) {
		    tickets.add(freeTicket);
		}
		break;
	    case MATH:
		for (LottoPlusStatement lps : statements) {
		    tickets.add(lps.getMathTicket());
		}
		freeTicket = getFreeMathTicket();
		if (freeTicket != null) {
		    tickets.add(freeTicket);
		}
		break;
	}

	return tickets;
    }

    public LottoPlusStatement getLastLottoPlusStatementOrderByDate() {
	return statementRepository.findTopByOrderByDateDesc();
    }

    @Override
    public List<LottoPlusStatement> getThisYearStatements() {
	int year = LottoUtil.getCurrentYear();
	return getLottoPlusStatements(Date.valueOf(year + "-01-01"), Date.valueOf(year + "-12-31"));
    }

    @Override
    public Iterable<RandomTicket> getRandomTickets() {
	return randomTicketRepository.findAll();
    }

	@Override
    public Iterable<MathTicket> getMathTickets() {
	return mathTicketRepository.findAll();
    }

    @Override
    public RandomTicket getLastRandomTicket() {
	return randomTicketRepository.findTopByOrderByRandomTicketIdDesc();
    }

    @Override
    public MathTicket getLastMathTicket() {
	return mathTicketRepository.findTopByOrderByMathTicketIdDesc();
    }

    @Override
    public StaticTicket getLastStaticTicket() {
	return staticTicketRepository.findTopByOrderByStaticTicketIdDesc();
    }

    @Override
    public RandomTicket getFreeRandomTicket() {
	return randomTicketRepository.findTopByLottoPlusStatement(null);
    }

    @Override
    public MathTicket getFreeMathTicket() {
	return mathTicketRepository.findTopByLottoPlusStatement(null);
    }

    @Override
    public StaticTicket getFreeStaticTicket() {
	return staticTicketRepository.findTopByLottoPlusStatement(null);
    }

    @Override
    public List<LottoPlusStatement> getLottoPlusStatements(Date from, Date to) {
	return statementRepository.findByDateBetweenOrderByDate(from, to);
    }

    @Override
    public Iterable<? extends Ticket> getTickets(String name) {
	Iterable<? extends Ticket> tickets = null;
	switch (Tickets.fromString(name)) {
	    case RANDOM:
		tickets = getRandomTickets();
		break;
	    case STATIC:
		tickets = getStaticTickets();
		break;
	    case MATH:
		tickets = getMathTickets();
		break;
	}
	return tickets;
    }

    @Override
    public boolean saveTicket(String name, int[] array) {
	if (array.length != 6 || !LottoUtil.isUnique(array) || !LottoUtil.containDrawNumbers(array)) {
	    return false;
	}
	Arrays.sort(array);
	Ticket ticket = null;

	switch (Tickets.fromString(name)) {
	    case RANDOM:
		ticket = getFreeRandomTicket();
		if (ticket == null) {
		    randomTicketRepository.save(new RandomTicket(array));
		}
		break;
	    case STATIC:
		ticket = getFreeStaticTicket();
		if (ticket == null) {
		    staticTicketRepository.save(new StaticTicket(array));
		}
		break;
	    case MATH:
		ticket = getFreeMathTicket();
		if (ticket == null) {
		    mathTicketRepository.save(new MathTicket(array));
		}
		break;
	}
	return ticket == null;
    }

    @Override
    public boolean saveStatement(int lotto[], int plus[], String date) {
	LottoPlusStatement lastStatement = getLastLottoPlusStatementOrderByDate();
	Date lastDate = Date.valueOf(LocalDate.of(1957, Month.JANUARY, 27));
	if (lastStatement != null) {
	    lastDate = lastStatement.getDate();
	}

	if (date == null || !LottoUtil.isDateFormatCorrect(date) || Date.valueOf(date).before(lastDate) || Date.valueOf(date).equals(lastDate)) {
	    return false;
	}

	RandomTicket randomTicket = getLastRandomTicket();
	MathTicket mathTicket = getLastMathTicket();
	StaticTicket staticTicket = getLastStaticTicket();

	if (randomTicket == null || mathTicket == null || staticTicket == null
		|| randomTicket.getLottoPlusStatement() != null
		|| mathTicket.getLottoPlusStatement() != null
		|| staticTicket.getLottoPlusStatement() != null) {
	    return false;
	}

	if (lotto.length != 6 || !LottoUtil.isUnique(lotto) || !LottoUtil.containDrawNumbers(lotto)
		|| plus.length != 6 || !LottoUtil.isUnique(plus) || !LottoUtil.containDrawNumbers(plus)) {
	    return false;
	}

	Arrays.sort(lotto);
	Arrays.sort(plus);

	statementRepository.save(new LottoPlusStatement(Date.valueOf(date), lotto, plus, randomTicket, staticTicket, mathTicket));
	return true;
    }

}
