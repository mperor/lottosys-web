package mapi.lotto.service;

import mapi.lotto.model.statistic.LotteryStatistic;

import java.util.List;

public interface StatisticService {
    LotteryStatistic getCurrentYearDynamicStatsByTicketName(String name);

    List<LotteryStatistic> getStaticStatsByTicketName(String name);

    List<Integer> getActiveLotteryYears(String name);
}
