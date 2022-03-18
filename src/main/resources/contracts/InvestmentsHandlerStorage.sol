// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;
import "./InvestmentEscrow.sol";
import "./libraries/InvestmentLib.sol";

contract InvestmentsHandlerStorage {

    using InvestmentLib for InvestmentLib.Investment;

    InvestmentLib.Investment[] private investmentsAttempts;

    // investments mapped to their power plants
    mapping(address => InvestmentLib.Investment[]) private investmentsByProjects;

    // investments mapped to their investors
    mapping(address => InvestmentLib.Investment[]) private investmentsByInvestors;

    function storeInvestmentAttempt(InvestmentLib.Investment memory investment) external {
        investmentsAttempts.push(investment);
    }
    function getInvestmentAttemptByIndex(uint index) 
        external 
        view 
        returns (InvestmentLib.Investment memory investment) 
    {
        return investmentsAttempts[index];
    }
    function getInvestmentAttemptById(uint id) 
        external 
        view 
        returns (InvestmentLib.Investment memory investment) 
    {
        return investmentsAttempts[id];
    }

    function storeInvestmentByPowerPlant(address powerPlant, InvestmentLib.Investment memory investment) 
        external 
    {
        investmentsByProjects[powerPlant].push(investment);
    }
    function getNumberOfInvestmentAttempts() external view returns (uint) {
        return investmentsAttempts.length;
    }

    function getInvestmentByPowerPlant(address powerPlant, uint investment)
        external 
        view 
        returns (InvestmentLib.Investment memory) 
    {
        return investmentsByProjects[powerPlant][investment];
    }
    function storeInvestmentByInvestor(address investor, InvestmentLib.Investment memory investment) external 
    {
        investmentsByInvestors[investor].push(investment);
    }
    function getInvestmentByInvestor(address investor, uint investment) 
        external 
        view
        returns (InvestmentLib.Investment memory)  
    {
        return investmentsByInvestors[investor][investment];
    }
}