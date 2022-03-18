// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;
import "./PowerPlant.sol";
import "./DTO/PowerPlantDTOs.sol";
import "./PowerPlantsHandlerStorage.sol";

contract PowerPlantsHandlerLogic {

    function createNewPowerPlant(
        PowerPlantCreateDTO memory dto,
        address keyGenerator
    ) external returns (PowerPlant) {
        return new PowerPlant(dto, keyGenerator);
    }

}