package com.olibersystem.bot.binance.botbinance.model.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Edge {
    private Node origin;
    private Node destination;
    private double price;
}
