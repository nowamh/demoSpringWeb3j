package com.example.demo.controller;

import com.example.demo.model.InvestmentDTO;
import com.example.demo.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.web3j.tx.Contract;

@RestController
@RequestMapping("/contract")
public class ContractController {


    private static final Logger LOGGER = LoggerFactory.getLogger(ContractController.class);

    @Autowired
    ContractService service;


    @PostMapping("/invest")
    public ResponseEntity<Void> invest(@RequestBody InvestmentDTO investmentDTO){
        try {
            service.invest(investmentDTO);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
//    @PostMapping
//    public Contract createContract(@RequestBody Contract newContract) throws Exception {
//        return service.createContract(newContract);
//    }

}
