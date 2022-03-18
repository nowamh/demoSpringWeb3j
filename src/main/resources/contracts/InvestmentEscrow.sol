// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;

import "./PowerPlant.sol";
import "./libraries/InvestmentLib.sol";

/**
 * Handles freezing and transfering tokens to an investor. 
 */
contract InvestmentEscrow {

    using InvestmentLib for *;

    // Expected and current states shoud ve equal 
    modifier InvestmentInState(
        InvestmentLib.InvestmentState current_state,
        InvestmentLib.InvestmentState expected_state
    ) {
        require(current_state == expected_state);
        _;
    }

    // Should be the power plant
    modifier onlyPowerPlant(InvestmentLib.Investment memory investment) {
        require(msg.sender == investment.project);
        _;
    }

    // Should be the investor
    modifier onlyInvestor(InvestmentLib.Investment memory investment) {
        require(msg.sender == investment.investor);
        _;
    }

    /** Freeze tokens */
    function blockTokensForInvestment(InvestmentLib.Investment memory investment) public payable {
        PowerPlant powerPlant = PowerPlant(investment.project);
        // allow escrow to deal with token
        powerPlant.approveTokenAccess(address(this), investment.amount);
        // freeze tokens
        powerPlant.freezeAmount(investment.amount);
    }

    function unblockTokensForInvestment(InvestmentLib.Investment memory investment, bool successfulPayment) public payable {
        require(successfulPayment == true, "Unsuccessful");
        PowerPlant powerPlant = PowerPlant(investment.project);
        powerPlant.unfreezeAmount(investment.amount);
        powerPlant.addFunds(investment.amount);
    }

    /** Transfers token to investor */
    function withdrawToInvestor(
        address payable _powerPlant,
        address payable _investor,
        uint256 _amount
    ) public payable {        
        PowerPlant powerPlant = PowerPlant(_powerPlant);
        require(block.timestamp >= powerPlant.getCampaignDetails().signingEndTimeStamp, "Can't execute now");
        require(powerPlant.checkSuccessMesurmentInPercentAchieved() == true, "Target amount isn't achieved");
        PowerPlantToken token = PowerPlantToken(powerPlant.getPowerPlantTokenAddress());
        require(token.allowance(_powerPlant, address(this)) >= _amount , "Invalid amount");
        // transfer token to investor 
        token.transferFrom(_powerPlant, _investor, _amount);   
    }
}
