package com.olibersystem.bot.binance.botbinance.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SummaryBO {
    public static final int MAX_OPERATION = 5;

    private OperationBO ant;
    private int up;
    private int down;
    private double average;
    private SymbolStatusEnum status;
    private OperationBO opMax;
    private OperationBO opMin;

    public SummaryBO() {
        opMax = new OperationBO(null, -99999999.00);
        opMin = new OperationBO(null, 99999999.00);
    }

    public void update(OperationBO operationBO) {
        findMax(operationBO);
        findMin(operationBO);
        valueAnt(operationBO);
    }
    public void findMax(OperationBO operationBO){
        if ( operationBO.getPrice() > this.opMax.getPrice()) {
            this.opMax = operationBO;
        }
    }
    public void findMin(OperationBO operationBO){
        if ( operationBO.getPrice() < this.opMin.getPrice()) {
            this.opMin = operationBO;
        }
    }
    public void valueAnt (OperationBO operationBO) {
        if (ant != null ) {
            if (ant.getPrice() > operationBO.getPrice()) {
                up++;
                down = 0;
            }
            if (ant.getPrice() < operationBO.getPrice()) {
                up = 0;
                down++;
            }
            status = up > MAX_OPERATION ? SymbolStatusEnum.POSITIVE :
                    down > MAX_OPERATION ? SymbolStatusEnum.NEGATIVE : SymbolStatusEnum.NEUTRAL;
            average = ( ant.getPrice() - operationBO.getPrice() ) / 2;
        }
        this.ant = operationBO;
    }

    @Override
    public String toString() {
        return "SummaryBO{" +
                ", up=" + up +
                ", down=" + down +
                ", status=" + status +
                ", opMax=" + opMax.getPrice() +
                ", opMin=" + opMin.getPrice() +
                ", avg = " + average +
                '}';
    }
}
