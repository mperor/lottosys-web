package mapi.lotto.service;

import lombok.RequiredArgsConstructor;
import mapi.lotto.model.result.LotteryResult;
import mapi.lotto.model.statistic.LotteryStatistic;
import mapi.lotto.model.statistic.LottoHits;
import mapi.lotto.model.statistic.PlusHits;
import mapi.lotto.model.ticket.TicketNumbers;
import mapi.lotto.repository.LotteryStatisticRepository;
import mapi.lotto.util.TicketType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class StatisticServiceImpl implements StatisticService, ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(StatisticServiceImpl.class);

    private final LotteryService lotteryService;
    private final LotteryStatisticRepository statisticRepository;

    @Override
    public LotteryStatistic getCurrentYearDynamicStatsByTicketName(String name) {
        return getDynamicStatsByTicketNameAndYear(name, Year.now());
    }

    private LotteryStatistic getDynamicStatsByTicketNameAndYear(String name, Year year) {
        var tickets = lotteryService.getTicketsOfYear(name, year);

        if (tickets.isEmpty()) {
            return null;
        }

        var lottoHits = new LottoHits();
        var plusHits = new PlusHits();

        tickets.forEach(ticket -> {
            TicketNumbers ticketNumbers = ticket.getTicketNumbers();
            LotteryResult result = ticket.getLotteryResult();
            lottoHits.increment(result.getLottoNumbers().countCommonNumbers(ticketNumbers));
            plusHits.increment(result.getPlusNumbers().countCommonNumbers(ticketNumbers));
        });

        return new LotteryStatistic(year.getValue(), lottoHits.getLAll(), lottoHits, plusHits, name);
    }

    @Override
    public List<LotteryStatistic> getStaticStatsByTicketName(String name) {
        var lastYear = Year.now().minusYears(1);
        if (statisticRepository.findTopByLotteryYearAndTicketType(lastYear.getValue(), name) == null) {
            updateStatisticOfYear(name, lastYear);
        }
        return statisticRepository.findByTicketType(name);
    }

    private boolean updateStatisticOfYear(String name, Year year) {
        LotteryStatistic lotteryStatistic = getDynamicStatsByTicketNameAndYear(name, year);
        if (lotteryStatistic == null) {
            return false;
        }
        statisticRepository.save(lotteryStatistic);
        return true;
    }

    @Override
    public List<Integer> getActiveLotteryYears(String name) {
        var statisticYears = statisticRepository.findDistinctLotteryYearByTicketTypeOrderDesc(name);
        statisticYears.add(0, Year.now().getValue());
        return statisticYears;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("Import static statistics => loading ...");
        for (TicketType type : TicketType.values()) {
            var now = Year.now().getValue();
            Integer latestYear = lotteryService.findLatestTicketByName(type.getName())
                    .map(lotteryTicket -> lotteryTicket.getLotteryResult().getLotteryDate())
                    .map(LocalDate::getYear)
                    .orElse(now);


            if (latestYear < now) {
                IntStream.range(latestYear, now)
                        .peek(nextYear -> logger.info("Update {} from year {} !!!", type.getName(), nextYear))
                        .forEach(nextYear -> updateStatisticOfYear(type.getName(), Year.of(nextYear)));
            }
        }
        logger.info("Import static statistics => done!");
    }
}
