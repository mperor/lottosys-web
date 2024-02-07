package mapi.lotto.util;

public enum LottoPrize {
    THREE(24),
    FOUR(170),
    FIVE(5300),
    SIX(3000000);

    private int prize;

    private LottoPrize(int prize) {
        this.prize = prize;
    }

    public int getPrize() {
        return prize;
    }

}
