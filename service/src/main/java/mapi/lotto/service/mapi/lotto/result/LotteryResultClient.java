package mapi.lotto.service.mapi.lotto.result;

import mapi.lotto.model.result.LotteryResult;
import mapi.lotto.model.result.LottoNumbers;
import mapi.lotto.model.result.PlusNumbers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.regex.Pattern;

@Component
public class LotteryResultClient {

    @Value("${lottery.result.client.lotto.url}")
    private String lottoUrl;
    @Value("${lottery.result.client.plus.url}")
    private String plusUrl;

    private final RestClient restClient = RestClient.create();
    private final Pattern newLinePattern = Pattern.compile("\\R");

    public LotteryResult downloadLastLotteryResult() {
        String lottoBody = retrieveBody(lottoUrl);
        LocalDate lotteryDate = getLotteryDate(lottoBody);
        Iterator<Integer> lottoIterator = getNumbersAsIterator(lottoBody);

        String plusBody = retrieveBody(plusUrl);
        Iterator<Integer> plusIterator = getNumbersAsIterator(plusBody);

        LottoNumbers lottoNumbers = new LottoNumbers(lottoIterator.next(), lottoIterator.next(), lottoIterator.next(), lottoIterator.next(), lottoIterator.next(), lottoIterator.next());
        PlusNumbers plusNumbers = new PlusNumbers(plusIterator.next(), plusIterator.next(), plusIterator.next(), plusIterator.next(), plusIterator.next(), plusIterator.next());

        return new LotteryResult(lotteryDate, lottoNumbers, plusNumbers);
    }

    private String retrieveBody(String lottoUrl) {
        var lottoBody = restClient.get()
                .uri(lottoUrl)
                .retrieve()
                .body(String.class);
        return lottoBody;
    }

    private Iterator<Integer> getNumbersAsIterator(String body) {
        var lottoIterator = newLinePattern.splitAsStream(body)
                .skip(1)
                .limit(6)
                .map(Integer::parseInt)
                .sorted()
                .iterator();
        return lottoIterator;
    }

    private LocalDate getLotteryDate(String body) {
        var lotteryDate = newLinePattern.splitAsStream(body)
                .findFirst()
                .map(LocalDate::parse)
                .orElseThrow(IllegalStateException::new);
        return lotteryDate;
    }

}
