package mapi.lotto.model;

import java.util.Set;

public interface Ticket {

    LottoPlusStatement getLottoPlusStatement();

    int getN1();

    int getN2();

    int getN3();

    int getN4();

    int getN5();

    int getN6();

    Set<Integer> getTicketSet();

    void setLottoPlusStatement(LottoPlusStatement lottoPlusStatement);

    void setN1(int n1);

    void setN2(int n2);

    void setN3(int n3);

    void setN4(int n4);

    void setN5(int n5);

    void setN6(int n6);

}
