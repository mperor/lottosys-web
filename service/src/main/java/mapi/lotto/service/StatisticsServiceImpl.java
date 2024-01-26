package mapi.lotto.service;

import mapi.lotto.model.LottoPlusStatement;
import mapi.lotto.model.Stat;
import mapi.lotto.repository.StatRepository;
import mapi.lotto.util.LottoPrizes;
import mapi.lotto.util.LottoUtil;
import mapi.lotto.util.PlusPrizes;
import mapi.lotto.util.Tickets;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final LottoService lottoService;
    private final StatRepository statRepository;

    public StatisticsServiceImpl(LottoService lottoService, StatRepository statRepository) {
        this.lottoService = lottoService;
        this.statRepository = statRepository;
    }

    protected Stat getDynamicStatByTicketNameAndYear(String name, int year) {
        List<LottoPlusStatement> statements = lottoService.getLottoPlusStatements(Date.valueOf(year + "-01-01"), Date.valueOf(year + "-12-31"));
        if (statements.isEmpty()) {
            return null;
        }

        int[] lottoStatsArray = new int[7];
        int[] plusStatsArray = new int[7];

        for (LottoPlusStatement statement : statements) {
            Set<Integer> numberSet = statement.getTicket(name).getTicketSet();
            int[] lotto = statement.getLottoLotteryArray();
            int[] plus = statement.getPlusLotteryArray();
            int lottoHitCounter = 0, plusHitCounter = 0;

            for (int i = 0; i < lotto.length; i++) {
                if (numberSet.contains(lotto[i])) {
                    lottoHitCounter++;
                }
                if (numberSet.contains(plus[i])) {
                    plusHitCounter++;
                }
            }

            lottoStatsArray[lottoHitCounter]++;
            plusStatsArray[plusHitCounter]++;

        }

        int lottoWonCounter = 0, plusWonCounter = 0;
        int lottoBank = 0, plusBank = 0;

        for (int i = 3; i <= 6; i++) {
            lottoWonCounter += lottoStatsArray[i];
            lottoBank += lottoStatsArray[i] * LottoPrizes.values()[i - 3].getPrize();
            plusWonCounter += plusStatsArray[i];
            plusBank += plusStatsArray[i] * PlusPrizes.values()[i - 3].getPrize();
        }
        int all = statements.size();

        double lottoAcc = (double) lottoWonCounter / all;
        double plusAcc = (double) plusWonCounter / all;

        plusBank += (-1) * all;
        lottoBank += (-3) * all;

        Stat stat = new Stat((short) year, plusAcc, plusBank, all, lottoAcc, lottoBank, lottoStatsArray, plusStatsArray, Tickets.fromString(name).ordinal());
        return stat;
    }

    public Stat getDynamicStatByTicketName(String name) {
        int year = LottoUtil.getCurrentYear();
        return getDynamicStatByTicketNameAndYear(name, year);
    }

    private boolean updateStat(String name, int year) {
        Stat stat = getDynamicStatByTicketNameAndYear(name, year);
        if (stat == null) {
            return false;
        }
        statRepository.save(stat);
        return true;
    }

    @Override
    public List<Stat> getStaticStatsByTicketName(String name) {
        final int lastYear = LottoUtil.getCurrentYear() - 1;
        Tickets t = Tickets.fromString(name);
        int ticketOrdinal = t.ordinal();
        if (statRepository.findTopByStatYearAndTicketOrdinal((short) lastYear, ticketOrdinal) == null) {
            updateStat(name, lastYear);
        }
        return statRepository.findByTicketOrdinal(ticketOrdinal);
    }
}
