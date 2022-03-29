package com.olibersystem.bot.binance.botbinance.converts;

public interface  IConvertion <E, D> {

    public D convertToDto(E entity);

    public E convertToEntity(D dto);
}
