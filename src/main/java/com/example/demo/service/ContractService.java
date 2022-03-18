package com.example.demo.service;
import com.example.demo.InvestmentsHandler;
import com.example.demo.model.InvestmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@Service
public class ContractService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractService.class);
    private  InvestmentsHandler investmentHandler;
    final Web3j   web3j = Web3j.build(new HttpService("https://eth-dev.youki.network"));

    private  ContractGasProvider contractGasProvider;

    Credentials credentials;
    private List<String> contracts = new ArrayList<>();

/*    public ContractService(  Web3j web3j, ContractGasProvider contractGasProvider) {

    }*/
    @PostConstruct
    public void init() throws IOException, CipherException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {

        this.contractGasProvider=contractGasProvider;
        credentials = Credentials.create("0xe20C8dC707B046C36545Be123eBe0AdC83E91C28");

        this.investmentHandler = InvestmentsHandler.load(
                "0x4e0D7170C4f357A3667c521C44D039fB9789a57b",
                this.web3j,
                new RawTransactionManager(this.web3j, credentials),
                this.contractGasProvider
        );
        LOGGER.info("Credentials created: address={}", credentials.getAddress());
        EthCoinbase coinbase = web3j.ethCoinbase().send();
        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(coinbase.getAddress(), DefaultBlockParameterName.LATEST).send();
        Transaction transaction = Transaction.createEtherTransaction(coinbase.getAddress(), transactionCount.getTransactionCount(), BigInteger.valueOf(20_000_000_000L), BigInteger.valueOf(21_000), credentials.getAddress(),BigInteger.valueOf(25_000_000_000_000_000L));
        web3j.ethSendTransaction(transaction).send();
        EthGetBalance balance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
        LOGGER.info("Balance: {}", balance.getBalance().longValue());
    }


    public void invest(final InvestmentDTO investmentDTO) throws Exception {
        LOGGER.info("Investment handler: invest");

        this.investmentHandler.startInvestment(
               investmentDTO.getInvestorAddress(),
               investmentDTO.getAmount(),
                investmentDTO.getPowerPlantAddress()
       ).send();

        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, this.investmentHandler.getContractAddress());
        web3j.ethLogFlowable(filter).subscribe(log -> {
            LOGGER.info("Log: {}", log.getData());
        });

    };




}
