#!/bin/bash

solc InvestmentsHandler.sol --bin --abi --optimize --overwrite -o ./compiledFiles/ @openzeppelin=../node_modules/@openzeppelin

cd compiledFiles

mkdir java

web3j generate solidity -b InvestmentsHandler.bin -a InvestmentsHandler.abi -o ./java/ --package ai.youki.blockchainservice



