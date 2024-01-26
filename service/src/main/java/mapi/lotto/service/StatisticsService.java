package mapi.lotto.service;

import mapi.lotto.model.Stat;

import java.util.List;

public interface StatisticsService {
    Stat getDynamicStatByTicketName(String name);

    List<Stat> getStaticStatsByTicketName(String name);
}
