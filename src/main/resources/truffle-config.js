/* eslint-disable no-undef */
require("dotenv").config();
const Web3 = require("web3");

/* 
Local environement is for local development we can switch between rinkeby testnet, local dev network or ganach 
to do so , first we need to set the environment variabl as bellow : 
  * to enable testnet we need:  IS_LOCAL = true  , IS_TESTNET = true  and IS_DEVPLAYGROUND = false
  * to enable devplayground we need:  IS_LOCAL = true  , IS_TESTNET = false  and IS_DEVPLAYGROUND = true 

than we need to run the appropriate migration script , they are on script on the package.json. 


On deployement to dev using github pipeline , the environment variable should be set as following 
  IS_LOCAL = false 
  IS_TESTNET = false 
  IS_DEVPLAYGROUND = false
  
*/
const isLocal = process.env["IS_LOCAL"];
const isTestnet = process.env["IS_TESTNET"];
const isDevPlayground = process.env["IS_DEVPLAYGROUND"];

let url;
let port;
let host;
let networkId;
let rinkeby;
let youkiAccount;
let youkiAccountPassword;

async function Unlock(web3, address, password) {
  try {
    await web3.eth.personal.unlockAccount(address, password, 30000);
  } catch (e) {
    return false;
  }
  return true;
}

if (isLocal == true) {
  if (isTestnet == true) {
    const HDWalletProvider = require("@truffle/hdwallet-provider");
    var infuraProjectId = process.env["INFURA_POJECT_ID"];
    var mnemonic = process.env["MNEMONIC_RINKEBY"];

    if (infuraProjectId === undefined || infuraProjectId === "") {
      throw new Error(
        'truffle-config.js needs the environment variable "INFURA_PROJECT_ID"'
      );
    } else if (mnemonic === undefined) {
      throw new Error(
        'truffle-config.js needs the environment variable "MNEMONIC"'
      );
    } else if (mnemonic.split(" ").length != 12) {
      throw new Error(
        'The environment variable "MNEMONIC" must be 12 words (space delineated)'
      );
    }

    rinkeby = {
      networkCheckTimeout: 10000,
      provider: () =>
        new HDWalletProvider(
          mnemonic,
          `https://rinkeby.infura.io/v3/${infuraProjectId}`
        ),
      network_id: 4, // Rinkeby's id
      gas: 6721975,
      confirmations: 3, // # of confs to wait between deployments. (default: 0)
      timeoutBlocks: 2000, // # of blocks before a deployment times out  (minimum/default: 50)
      skipDryRun: true, // Skip dry run before migrations? (default: false for public nets )
    };
  }
  if (isDevPlayground == true) {
    host = process.env["HOST_PLAYGROUND"];
    port = process.env["PORT_PLAYGROUND"];
    networkId = process.env["NETWORK_ID_PLAYGROUND"];
    url = "http://" + host + ":" + port;
    youkiAccount = process.env["YOUKI_ACCOUNT_PLAYGROUND"];
    youkiAccountPassword = process.env["YOUKI_ACCOUNT_PASSWORD_PLAYGROUND"];
  }
} else {
  host = process.env["HOST"];
  port = process.env["PORT"];
  networkId = process.env["NETWORK_ID"];
  url = host + ":" + port;
  youkiAccount = process.env["YOUKI_ACCOUNT"];
  youkiAccountPassword = process.env["YOUKI_ACCOUNT_PASSWORD"];
}

//unlock account for private chain
if (youkiAccountPassword && url) {
  const web3 = new Web3(url);
  console.log(">> Unlocking account ");

  Unlock(web3, youkiAccount, youkiAccountPassword);
}

module.exports = {
  compilers: {
    solc: {
      version: "0.8.0",
    },
  },
  networks: {
    dev: {
      provider: () => new Web3.providers.HttpProvider(url),
      network_id: networkId,
      from: youkiAccount,
    },
    ganache: {
      host: "127.0.0.1", // Localhost (default: none)
      port: 8545, // Standard Ethereum port (default: none)
      network_id: "*", // Any network (default: none)
    },
    rinkeby,
  },
  plugins: ["truffle-contract-size"]
};
