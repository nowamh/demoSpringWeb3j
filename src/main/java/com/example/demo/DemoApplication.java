package com.example.demo;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.lang.Nullable;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.filters.FilterException;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class DemoApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);


	final Web3j   web3j = Web3j.build(new HttpService("https://eth-dev.youki.network"));

	@Nullable
	private Disposable transactionsSubscription = null;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@PostConstruct
	public void listenTransactions() {
		this.subscribeOnTransactions(this.web3j);
	}

	private void subscribeOnTransactions(final Web3j web3j) {
		try {
			transactionsSubscription = web3j.transactionFlowable().subscribe(tx -> {
				LOGGER.info("New transaction: id={}, block={}, from={}, to={}",
						tx.getHash(),
						tx.getBlockHash(),
						tx.getFrom(),
						tx.getTo());

				EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(
						tx.getFrom(), DefaultBlockParameterName.LATEST).send();

				LOGGER.info("Transaction count: {}",
						transactionCount.getTransactionCount().intValue());

			});
		}
		catch  (FilterException e) {
			throw new BlockchainException("Error when obtaining client version", e);
		}
	}

}
