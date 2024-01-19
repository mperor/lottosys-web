package mapi.lotto.model;

import java.sql.Date;

import jakarta.persistence.*;
import mapi.lotto.util.Tickets;

@Entity
@Table(name = "lotto_plus_statement")
public class LottoPlusStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lps_id", nullable = false, unique = true)
    private long lpsId;

    @Column(nullable = false)
    private Date date;

    @Column
    private int l1;

    @Column
    private int l2;

    @Column
    private int l3;

    @Column
    private int l4;

    @Column
    private int l5;

    @Column
    private int l6;

    @Column
    private int p1;

    @Column
    private int p2;

    @Column
    private int p3;

    @Column
    private int p4;

    @Column
    private int p5;

    @Column
    private int p6;

    /*
    @OneToOne
    @JoinColumn(name = "lps_id", nullable = true)
    private RandomTicket randomTicket;
     */
    @OneToOne(mappedBy = "lottoPlusStatement")
    private RandomTicket randomTicket;

    @OneToOne(mappedBy = "lottoPlusStatement")
    private StaticTicket staticTicket;

    @OneToOne(mappedBy = "lottoPlusStatement")
    private MathTicket mathTicket;

    protected LottoPlusStatement() {
    }

    public LottoPlusStatement(Date date, int[] lottoArray, int[] plusArray, RandomTicket randomTicket, StaticTicket staticTicket, MathTicket mathTicket) {
        this.date = date;
        if (lottoArray.length == 6 && plusArray.length == 6) {
            this.l1 = lottoArray[0];
            this.l2 = lottoArray[1];
            this.l3 = lottoArray[2];
            this.l4 = lottoArray[3];
            this.l5 = lottoArray[4];
            this.l6 = lottoArray[5];
            this.p1 = plusArray[0];
            this.p2 = plusArray[1];
            this.p3 = plusArray[2];
            this.p4 = plusArray[3];
            this.p5 = plusArray[4];
            this.p6 = plusArray[5];
        } else {
            throw new IllegalStateException("Incorrect arrays plus and lotto !!!");
        }
        this.randomTicket = randomTicket;
        randomTicket.setLottoPlusStatement(this);
        this.staticTicket = staticTicket;
        staticTicket.setLottoPlusStatement(this);
        this.mathTicket = mathTicket;
        mathTicket.setLottoPlusStatement(this);
    }

    public long getLpsId() {
        return lpsId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getL1() {
        return l1;
    }

    public void setL1(int l1) {
        this.l1 = l1;
    }

    public int getL2() {
        return l2;
    }

    public void setL2(int l2) {
        this.l2 = l2;
    }

    public int getL3() {
        return l3;
    }

    public void setL3(int l3) {
        this.l3 = l3;
    }

    public int getL4() {
        return l4;
    }

    public void setL4(int l4) {
        this.l4 = l4;
    }

    public int getL5() {
        return l5;
    }

    public void setL5(int l5) {
        this.l5 = l5;
    }

    public int getL6() {
        return l6;
    }

    public void setL6(int l6) {
        this.l6 = l6;
    }

    public int getP1() {
        return p1;
    }

    public void setP1(int p1) {
        this.p1 = p1;
    }

    public int getP2() {
        return p2;
    }

    public void setP2(int p2) {
        this.p2 = p2;
    }

    public int getP3() {
        return p3;
    }

    public void setP3(int p3) {
        this.p3 = p3;
    }

    public int getP4() {
        return p4;
    }

    public void setP4(int p4) {
        this.p4 = p4;
    }

    public int getP5() {
        return p5;
    }

    public void setP5(int p5) {
        this.p5 = p5;
    }

    public int getP6() {
        return p6;
    }

    public void setP6(int p6) {
        this.p6 = p6;
    }

    public int[] getLottoLotteryArray() {
        return new int[]{l1, l2, l3, l4, l5, l6};
    }

    public int[] getPlusLotteryArray() {
        return new int[]{p1, p2, p3, p4, p5, p6};
    }

    public RandomTicket getRandomTicket() {
        return randomTicket;
    }

    public StaticTicket getStaticTicket() {
        return staticTicket;
    }

    public MathTicket getMathTicket() {
        return mathTicket;
    }

    public Ticket getTicket(String name) {
        Ticket ticket = null;
        switch (Tickets.fromString(name)) {
            case RANDOM:
                ticket = getRandomTicket();
                break;
            case STATIC:
                ticket = getStaticTicket();
                break;
            case MATH:
                ticket = getMathTicket();
                break;
            default:
                throw new IllegalArgumentException("Incorrect ticket name!");
        }
        return ticket;
    }
}
