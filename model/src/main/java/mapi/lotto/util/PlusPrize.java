package mapi.lotto.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PlusPrize {
    THREE(10),
    FOUR(100),
    FIVE(3500),
    SIX(1000000);

    @Getter
    private final int prize;
}
