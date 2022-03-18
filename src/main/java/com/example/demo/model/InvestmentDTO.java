package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class InvestmentDTO {

    private final String powerPlantAddress;
    private final BigInteger amount ;
    private final String investorAddress;

    @JsonCreator
    public InvestmentDTO(
            @JsonProperty(required = true, value = "powerPlantAddress")
                    String powerPlantAddress,
            @JsonProperty(required = true, value = "amount")
                    BigInteger amount,
            @JsonProperty(required = true, value = "investorAddress")
                    String investorAddress) {
        this.powerPlantAddress = powerPlantAddress;
        this.amount = amount;
        this.investorAddress = investorAddress;
    }

    public String getPowerPlantAddress() {
        return powerPlantAddress;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public String getInvestorAddress() {
        return investorAddress;
    }
}
