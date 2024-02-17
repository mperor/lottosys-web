package mapi.lotto.service.mapi.lotto.result;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapi.lotto.service.LotteryService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(value = "lottery.result.updater.enabled", havingValue = "true")
@Service
@RequiredArgsConstructor
@Slf4j
public class LotteryResultUpdater {

    private final LotteryService lotteryService;
    private final LotteryResultClient lotteryResultClient;

    @PostConstruct
    private void enabled() {
        log.info("LotteryResultUpdater is enabled!");
    }

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
