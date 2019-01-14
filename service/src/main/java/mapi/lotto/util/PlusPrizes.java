package mapi.lotto.util;

public enum PlusPrizes {
    THREE(10),
    FOUR(100),
    FIVE(3500),
    SIX(1000000);

    private int prize;

    private PlusPrizes(int prize) {
        this.prize = prize;
    }

    public int getPrize() {
        return prize;
    }
}
