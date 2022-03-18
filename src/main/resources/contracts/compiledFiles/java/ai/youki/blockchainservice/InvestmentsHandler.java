package ai.youki.blockchainservice;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class InvestmentsHandler extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50604051610e6c380380610e6c83398101604081905261002f9161008d565b600080546001600160a01b039485166001600160a01b0319918216179091556001805493851693821693909317909255600280549190931691161790556100d0565b80516001600160a01b038116811461008857600080fd5b919050565b6000806000606084860312156100a257600080fd5b6100ab84610071565b92506100b960208501610071565b91506100c760408501610071565b90509250925092565b610d8d806100df6000396000f3fe60806040526004361061004a5760003560e01c80635d7bef291461004f57806362197dcc146100855780636986f3b9146100a5578063eb9159d9146100c6578063f405e00e146100e6575b600080fd5b34801561005b57600080fd5b5061006f61006a3660046109c2565b6100fb565b60405161007c9190610a45565b60405180910390f35b34801561009157600080fd5b5061006f6100a0366004610a68565b61019d565b6100b86100b3366004610a94565b610249565b60405190815260200161007c565b3480156100d257600080fd5b5061006f6100e1366004610a68565b610625565b6100f96100f4366004610ad6565b61068d565b005b61012a6040805160a0810182526000808252602082018190529181018290526060810182905290608082015290565b600054604051635f6203ed60e01b8152600481018490526001600160a01b0390911690635f6203ed9060240160a060405180830381865afa158015610173573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906101979190610b60565b92915050565b6101cc6040805160a0810182526000808252602082018190529181018290526060810182905290608082015290565b60005460405163e2e0adbd60e01b81526001600160a01b038581166004830152602482018590529091169063e2e0adbd906044015b60a060405180830381865afa15801561021e573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906102429190610b60565b9392505050565b600080829050600160009054906101000a90046001600160a01b03166001600160a01b0316637b42f93b82836001600160a01b0316630c611f246040518163ffffffff1660e01b815260040161014060405180830381865afa1580156102b3573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906102d79190610c03565b60a00151876040518463ffffffff1660e01b81526004016102fa93929190610c95565b60006040518083038186803b15801561031257600080fd5b505afa158015610326573d6000803e3d6000fd5b50505050600073__$8a9f721c459d3364ee2667f17fe4921129$__63ca5d47b560008054906101000a90046001600160a01b03166001600160a01b03166339928ad76040518163ffffffff1660e01b8152600401602060405180830381865afa158015610397573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906103bb9190610cbf565b86898960006040518663ffffffff1660e01b81526004016103e0959493929190610cd8565b60a060405180830381865af41580156103fd573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906104219190610b60565b600254604051633c4044c360e11b81529192506001600160a01b031690637880898690610452908490600401610a45565b600060405180830381600087803b15801561046c57600080fd5b505af1158015610480573d6000803e3d6000fd5b505060005460405163cc3ebb3360e01b81526001600160a01b03909116925063cc3ebb3391506104b69087908590600401610d16565b600060405180830381600087803b1580156104d057600080fd5b505af11580156104e4573d6000803e3d6000fd5b505060005460405163752cbea760e01b81526001600160a01b03909116925063752cbea79150610518908490600401610a45565b600060405180830381600087803b15801561053257600080fd5b505af1158015610546573d6000803e3d6000fd5b5050600054604051635d6fbde960e01b81526001600160a01b039091169250635d6fbde9915061057c9089908590600401610d16565b600060405180830381600087803b15801561059657600080fd5b505af11580156105aa573d6000803e3d6000fd5b505050507fccd5e5e94445a0348f23c72137a8dec896aa8ac39ae438f43fb91a07f28e6c87816000015182604001518360200151846060015160405161061394939291909384526001600160a01b03928316602085015291166040830152606082015260800190565b60405180910390a15195945050505050565b6106546040805160a0810182526000808252602082018190529181018290526060810182905290608082015290565b60005460405163dc9dedbb60e01b81526001600160a01b038581166004830152602482018590529091169063dc9dedbb90604401610201565b600080546040516303c0477560e31b8152600481018590526001600160a01b0390911690631e023ba89060240160a060405180830381865afa1580156106d7573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906106fb9190610b60565b90506001821515146107485760405162461bcd60e51b815260206004820152601260248201527110d85b89dd0818994818dbdb98db1d59195960721b604482015260640160405180910390fd5b600254602082015160408084015160608501519151632cafe1e560e21b81526001600160a01b0393841660048201529083166024820152604481019190915291169063b2bf879490606401600060405180830381600087803b1580156107ad57600080fd5b505af11580156107c1573d6000803e3d6000fd5b5050604051631ddd13cf60e11b815273__$8a9f721c459d3364ee2667f17fe4921129$__9250633bba279e91506107ff908490600390600401610d33565b60006040518083038186803b15801561081757600080fd5b505af415801561082b573d6000803e3d6000fd5b5050505080602001516001600160a01b031663125eb9ed6040518163ffffffff1660e01b8152600401600060405180830381600087803b15801561086e57600080fd5b505af1158015610882573d6000803e3d6000fd5b5050600054602084015160405163cc3ebb3360e01b81526001600160a01b03909216935063cc3ebb3392506108bb918590600401610d16565b600060405180830381600087803b1580156108d557600080fd5b505af11580156108e9573d6000803e3d6000fd5b50506000546040808501519051635d6fbde960e01b81526001600160a01b039092169350635d6fbde99250610922918590600401610d16565b600060405180830381600087803b15801561093c57600080fd5b505af1158015610950573d6000803e3d6000fd5b505050507f1f47419f68642e39973f55ac83dd71aee36e1742812715c8d2b660d2bb61f94e838260400151836020015184606001516040516109b594939291909384526001600160a01b03928316602085015291166040830152606082015260800190565b60405180910390a1505050565b6000602082840312156109d457600080fd5b5035919050565b600481106109f957634e487b7160e01b600052602160045260246000fd5b50565b80518252602081015160018060a01b0380821660208501528060408401511660408501525050606081015160608301526080810151610a3a816109db565b806080840152505050565b60a0810161019782846109fc565b6001600160a01b03811681146109f957600080fd5b60008060408385031215610a7b57600080fd5b8235610a8681610a53565b946020939093013593505050565b600080600060608486031215610aa957600080fd5b8335610ab481610a53565b9250602084013591506040840135610acb81610a53565b809150509250925092565b60008060408385031215610ae957600080fd5b8235915060208301358015158114610b0057600080fd5b809150509250929050565b604051610140810167ffffffffffffffff81118282101715610b3d57634e487b7160e01b600052604160045260246000fd5b60405290565b600481106109f957600080fd5b8051610b5b81610b43565b919050565b600060a08284031215610b7257600080fd5b60405160a0810181811067ffffffffffffffff82111715610ba357634e487b7160e01b600052604160045260246000fd5b604052825181526020830151610bb881610a53565b60208201526040830151610bcb81610a53565b6040820152606083810151908201526080830151610be881610b43565b60808201529392505050565b805160018110610b5b57600080fd5b60006101408284031215610c1657600080fd5b610c1e610b0b565b610c2783610bf4565b815260208301516020820152604083015160408201526060830151606082015260808301516080820152610c5d60a08401610b50565b60a082015260c0838101519082015260e080840151908201526101008084015190820152610120928301519281019290925250919050565b6001600160a01b038416815260608101610cae846109db565b602082019390935260400152919050565b600060208284031215610cd157600080fd5b5051919050565b8581526001600160a01b038581166020830152841660408201526060810183905260a08101610d06836109db565b8260808301529695505050505050565b6001600160a01b038316815260c0810161024260208301846109fc565b60c08101610d4182856109fc565b610d4a836109db565b8260a0830152939250505056fea26469706673582212204f49074b9a01b12706748809dcb500d9c80eff4d9f5b753a025f4145b8e2f1d564736f6c634300080c0033\n"
            + "\n"
            + "// $8a9f721c459d3364ee2667f17fe4921129$ -> libraries/InvestmentLib.sol:InvestmentLib\n"
            + "// $8a9f721c459d3364ee2667f17fe4921129$ -> libraries/InvestmentLib.sol:InvestmentLib";

    public static final String FUNC_CONCLUDEINVESTMENT = "concludeInvestment";

    public static final String FUNC_GETINVESTMENTBYINDEX = "getInvestmentByIndex";

    public static final String FUNC_GETSPECIFICINVESTMENTOFINVESTOR = "getSpecificInvestmentOfInvestor";

    public static final String FUNC_GETSPECIFICINVESTMENTPOFPROJECT = "getSpecificInvestmentPOfProject";

    public static final String FUNC_STARTINVESTMENT = "startInvestment";

    public static final Event NEWINVESTMENTATTEMPT_EVENT = new Event("NewInvestmentAttempt", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event SUCCESSFULINVESTMENT_EVENT = new Event("SuccessfulInvestment", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected InvestmentsHandler(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected InvestmentsHandler(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected InvestmentsHandler(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected InvestmentsHandler(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<NewInvestmentAttemptEventResponse> getNewInvestmentAttemptEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NEWINVESTMENTATTEMPT_EVENT, transactionReceipt);
        ArrayList<NewInvestmentAttemptEventResponse> responses = new ArrayList<NewInvestmentAttemptEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewInvestmentAttemptEventResponse typedResponse = new NewInvestmentAttemptEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.investor = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.project = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewInvestmentAttemptEventResponse> newInvestmentAttemptEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewInvestmentAttemptEventResponse>() {
            @Override
            public NewInvestmentAttemptEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NEWINVESTMENTATTEMPT_EVENT, log);
                NewInvestmentAttemptEventResponse typedResponse = new NewInvestmentAttemptEventResponse();
                typedResponse.log = log;
                typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.investor = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.project = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewInvestmentAttemptEventResponse> newInvestmentAttemptEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWINVESTMENTATTEMPT_EVENT));
        return newInvestmentAttemptEventFlowable(filter);
    }

    public List<SuccessfulInvestmentEventResponse> getSuccessfulInvestmentEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SUCCESSFULINVESTMENT_EVENT, transactionReceipt);
        ArrayList<SuccessfulInvestmentEventResponse> responses = new ArrayList<SuccessfulInvestmentEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SuccessfulInvestmentEventResponse typedResponse = new SuccessfulInvestmentEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.investor = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.project = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SuccessfulInvestmentEventResponse> successfulInvestmentEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SuccessfulInvestmentEventResponse>() {
            @Override
            public SuccessfulInvestmentEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SUCCESSFULINVESTMENT_EVENT, log);
                SuccessfulInvestmentEventResponse typedResponse = new SuccessfulInvestmentEventResponse();
                typedResponse.log = log;
                typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.investor = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.project = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SuccessfulInvestmentEventResponse> successfulInvestmentEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SUCCESSFULINVESTMENT_EVENT));
        return successfulInvestmentEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> concludeInvestment(BigInteger id, Boolean _readyToConclude) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CONCLUDEINVESTMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(id), 
                new org.web3j.abi.datatypes.Bool(_readyToConclude)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Investment> getInvestmentByIndex(BigInteger index) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETINVESTMENTBYINDEX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Investment>() {}));
        return executeRemoteCallSingleValueReturn(function, Investment.class);
    }

    public RemoteFunctionCall<Investment> getSpecificInvestmentOfInvestor(String investor, BigInteger investment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSPECIFICINVESTMENTOFINVESTOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, investor), 
                new org.web3j.abi.datatypes.generated.Uint256(investment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Investment>() {}));
        return executeRemoteCallSingleValueReturn(function, Investment.class);
    }

    public RemoteFunctionCall<Investment> getSpecificInvestmentPOfProject(String project, BigInteger investment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSPECIFICINVESTMENTPOFPROJECT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, project), 
                new org.web3j.abi.datatypes.generated.Uint256(investment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Investment>() {}));
        return executeRemoteCallSingleValueReturn(function, Investment.class);
    }

    public RemoteFunctionCall<TransactionReceipt> startInvestment(String _investor, BigInteger _amount, String _project) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_STARTINVESTMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _investor), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount), 
                new org.web3j.abi.datatypes.Address(160, _project)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static InvestmentsHandler load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new InvestmentsHandler(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static InvestmentsHandler load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new InvestmentsHandler(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static InvestmentsHandler load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new InvestmentsHandler(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static InvestmentsHandler load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new InvestmentsHandler(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<InvestmentsHandler> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String handlerStorageAddress, String handlerLogicAddress, String _escrow) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, handlerStorageAddress), 
                new org.web3j.abi.datatypes.Address(160, handlerLogicAddress), 
                new org.web3j.abi.datatypes.Address(160, _escrow)));
        return deployRemoteCall(InvestmentsHandler.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<InvestmentsHandler> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String handlerStorageAddress, String handlerLogicAddress, String _escrow) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, handlerStorageAddress), 
                new org.web3j.abi.datatypes.Address(160, handlerLogicAddress), 
                new org.web3j.abi.datatypes.Address(160, _escrow)));
        return deployRemoteCall(InvestmentsHandler.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<InvestmentsHandler> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String handlerStorageAddress, String handlerLogicAddress, String _escrow) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, handlerStorageAddress), 
                new org.web3j.abi.datatypes.Address(160, handlerLogicAddress), 
                new org.web3j.abi.datatypes.Address(160, _escrow)));
        return deployRemoteCall(InvestmentsHandler.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<InvestmentsHandler> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String handlerStorageAddress, String handlerLogicAddress, String _escrow) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, handlerStorageAddress), 
                new org.web3j.abi.datatypes.Address(160, handlerLogicAddress), 
                new org.web3j.abi.datatypes.Address(160, _escrow)));
        return deployRemoteCall(InvestmentsHandler.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class Investment extends StaticStruct {
        public BigInteger id;

        public String project;

        public String investor;

        public BigInteger amount;

        public BigInteger state;

        public Investment(BigInteger id, String project, String investor, BigInteger amount, BigInteger state) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id),new org.web3j.abi.datatypes.Address(project),new org.web3j.abi.datatypes.Address(investor),new org.web3j.abi.datatypes.generated.Uint256(amount),new org.web3j.abi.datatypes.generated.Uint8(state));
            this.id = id;
            this.project = project;
            this.investor = investor;
            this.amount = amount;
            this.state = state;
        }

        public Investment(Uint256 id, Address project, Address investor, Uint256 amount, Uint8 state) {
            super(id,project,investor,amount,state);
            this.id = id.getValue();
            this.project = project.getValue();
            this.investor = investor.getValue();
            this.amount = amount.getValue();
            this.state = state.getValue();
        }
    }

    public static class NewInvestmentAttemptEventResponse extends BaseEventResponse {
        public BigInteger id;

        public String investor;

        public String project;

        public BigInteger amount;
    }

    public static class SuccessfulInvestmentEventResponse extends BaseEventResponse {
        public BigInteger id;

        public String investor;

        public String project;

        public BigInteger amount;
    }
}
