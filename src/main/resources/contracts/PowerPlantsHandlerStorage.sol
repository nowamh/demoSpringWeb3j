// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;

contract PowerPlantsHandlerStorage {

    // list of power plants
    address[] private deployedPowerPlants;
    // power plants mepped to their keys
    mapping(string => address) public powerPlantsByKey;
    // power plants mapped to their ids
    mapping(uint => address) public powerPlantsById;

    function storeDeployedPowerPlant(address powerPlantAddress) public {
        deployedPowerPlants.push(powerPlantAddress);
    }
    function getDeployedPowerPlants() public view returns (address[] memory) {
        return deployedPowerPlants;
    }
    
    function storePowerPlantByKey(
        string memory key, 
        address powerPlantAddress) 
        public {
        powerPlantsByKey[key] = powerPlantAddress;
    }
    function getPowerPlantByKey(string memory key)
        public
        view
        returns (address)
    {
        return powerPlantsByKey[key];
    }

    function storePowerPlantById(uint id, address powerPlantAddress) public {
        powerPlantsById[id] = powerPlantAddress;
    }

    function getPowerPlantById(uint id) public view returns (address) {
        return powerPlantsById[id];
    }
}