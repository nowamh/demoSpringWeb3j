// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;

import "./PowerPlant.sol";
import "./InvestmentEscrow.sol";
import "./libraries/InvestmentLib.sol";
import "./InvestmentsHandlerStorage.sol";
import "./InvestmentsHandlerLogic.sol";
/**
 *  Handles the investment process. 
 */
contract InvestmentsHandler{

    using InvestmentLib for *;

    InvestmentsHandlerStorage private handlerStorage;
    InvestmentsHandlerLogic private handlerLogic;
    InvestmentEscrow private investmentEscrow;

    event SuccessfulInvestment(uint id, address investor, address project, uint amount);
    event NewInvestmentAttempt(uint id, address investor, address project, uint amount);

    constructor(address handlerStorageAddress, address handlerLogicAddress, address _escrow) {
        handlerStorage = InvestmentsHandlerStorage(handlerStorageAddress);
        handlerLogic = InvestmentsHandlerLogic(handlerLogicAddress);
        investmentEscrow =  InvestmentEscrow(_escrow);
    }
    
    function startInvestment(address payable _investor, uint256 _amount, address payable _project)
        public 
        payable
        returns (uint)
    {
        PowerPlant powerPlant = PowerPlant(_project);
        handlerLogic.checkRequirementsToStartInvestment(powerPlant, powerPlant.getCampaignDetails().status, _amount);

        // initiate investment 
        InvestmentLib.Investment memory investment = InvestmentLib.setInvestment(
            handlerStorage.getNumberOfInvestmentAttempts(), 
            _project, 
            _investor, 
            _amount, 
            InvestmentLib.InvestmentState.PENDING);

        // reserve tokens accordingly to the investment amout 
        investmentEscrow.blockTokensForInvestment(investment);

        // save investment to all power plant's investments 
        handlerStorage.storeInvestmentByPowerPlant(_project, investment);

        // save investment to the list of investments 
        handlerStorage.storeInvestmentAttempt(investment);

        // save investment to the investments of investors
        handlerStorage.storeInvestmentByInvestor(_investor, investment);

        emit NewInvestmentAttempt(
            investment.id, 
            investment.investor, 
            investment.project, 
            investment.amount
        );
        return (investment.id);
    }

    /** finish the investment */
    function concludeInvestment(uint id, bool _readyToConclude) public payable {
        InvestmentLib.Investment memory investment = handlerStorage.getInvestmentAttemptById(id);

        require( _readyToConclude == true, "Can't be concluded");

        investmentEscrow.withdrawToInvestor(investment.project, investment.investor, investment.amount);
        InvestmentLib.setInvestmentState(investment, InvestmentLib.InvestmentState.FINISHED);

        PowerPlant(investment.project).updateCompaignAfterInvestment();
        handlerStorage.storeInvestmentByPowerPlant(investment.project, investment);

        handlerStorage.storeInvestmentByInvestor(investment.investor, investment);

        emit SuccessfulInvestment(
            id, 
            investment.investor, 
            investment.project, 
            investment.amount);
    }

    function getInvestmentByIndex(uint index) external view returns (InvestmentLib.Investment memory) {
        return (handlerStorage.getInvestmentAttemptByIndex(index));
    }

    function getSpecificInvestmentPOfProject(address project, uint investment) 
        external 
        view 
        returns (InvestmentLib.Investment memory) 
    {
        return (handlerStorage.getInvestmentByPowerPlant(project, investment));
    }

    function getSpecificInvestmentOfInvestor(address investor, uint investment)
        external 
        view 
        returns (InvestmentLib.Investment memory) 
    {
        return (handlerStorage.getInvestmentByInvestor(investor, investment));
    }

}
