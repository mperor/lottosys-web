package mapi.lotto.service.mapi.lotto.result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapi.lotto.service.LotteryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LotteryResultUpdater {

    private final LotteryService lotteryService;
    private final LotteryResultClient lotteryResultClient;

    @Scheduled(cron = "${lottery.result.updater.cron}")
    public void update() {
        log.info("Update lottery results => starting ...");
        try {
            var result = lotteryResultClient.downloadLastLotteryResult();
            boolean saved = lotteryService.saveNewResultAndBindNewTickets(result.getLottoNumbers(), result.getPlusNumbers(), result.getLotteryDate());
            if (saved)
                lotteryService.generateNewTickets();
            log.info("Update lottery results => saved={}!", saved);
        } catch (Exception exception) {
            log.error("Lottery results update failed!", exception);
        }
    }

}
