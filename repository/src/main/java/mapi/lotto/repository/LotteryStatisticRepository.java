package mapi.lotto.repository;

import mapi.lotto.model.statistic.LotteryStatistic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotteryStatisticRepository extends CrudRepository<LotteryStatistic, Long> {

    LotteryStatistic findTopByLotteryYearAndTicketType(int year, String ticketType);

    List<LotteryStatistic> findByTicketType(String ticketType);

    @Query("SELECT DISTINCT s.lotteryYear FROM LotteryStatistic s " +
            "WHERE s.ticketType = :ticketType " +
            "ORDER BY s.lotteryYear DESC")
    List<Integer> findDistinctLotteryYearByTicketTypeOrderDesc(String ticketType);
}
