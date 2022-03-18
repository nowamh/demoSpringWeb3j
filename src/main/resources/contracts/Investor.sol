// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;
import "@openzeppelin/contracts/access/Ownable.sol";
import "@youkinetwork/youki_identity_smart_contracts/contracts/YoukiIdentity.sol";
import "@youkinetwork/youki_identity_smart_contracts/contracts/YoukiIdentity.sol";
import "./libraries/InvestmentLib.sol";

/** Defines the role of investor */
contract Investor is Ownable {

    using InvestmentLib for InvestmentLib.Investor;
    using InvestmentLib for InvestmentLib.Investment;

    InvestmentLib.Investor investor;
    InvestmentLib.Investment[] private investments;

    function setCompany(string memory _companyName) public {
        investor.companyName = _companyName;
    }

    // add a new investment to the list of investments
    function addInvestment(InvestmentLib.Investment memory investment) public {
        investments.push(investment);
    }

    function saveRoleToIdentity() external pure {
        
    }   

}