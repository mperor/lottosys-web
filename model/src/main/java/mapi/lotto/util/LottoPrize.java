package mapi.lotto.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LottoPrize {
    THREE(24),
    FOUR(170),
    FIVE(5300),
    SIX(3000000);

    @Getter
    private final int prize;
}
