// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;

import "./PowerPlant.sol";
import "./InvestmentEscrow.sol";
import "./libraries/InvestmentLib.sol";
import "./libraries/PowerPlantLib.sol";

contract InvestmentsHandlerLogic {

    function checkRequirementsToStartInvestment(PowerPlant powerPlant, PowerPlantLib.CampaignStatus status, uint amount) external view {
        // check if the campaign is active
        require (status == PowerPlantLib.CampaignStatus.ACTIVE , "Campaign not active");

        uint total = powerPlant.getReachedAmount() + amount + powerPlant.getReservedAmount();

        // check if the amount is available
        require(total <= powerPlant.getTargetAmount(), "Already fully funded");
    }
}