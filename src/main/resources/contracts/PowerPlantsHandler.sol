// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;

import "@openzeppelin/contracts/access/Ownable.sol";
import "./libraries/PowerPlantLib.sol";
import "./DTO/PowerPlantDTOs.sol";
import "./PowerPlant.sol";
import "./PowerPlantsHandlerStorage.sol";
import "./PowerPlantsHandlerLogic.sol";

contract PowerPlantsHandler is Ownable {

    PowerPlantsHandlerStorage handlerStorage;
    PowerPlantsHandlerLogic handlerLogic;

    /**
     * Emitted when a new PowerPlant created
     */
    event PowerPlantCreated(
        address indexed powerPlantAddress,
        address indexed ownerAddress,
        string name,
        string description,
        PowerPlantLib.PowerPlantType powerPlantType,
        string powerPlantSubType,
        PowerPlantLib.PowerPlantStatus powerPlantStatus,
        string mainProfilePicture,
        uint256 creationDateTime,
        uint256 modificationDateTime
    );

    constructor(address handlerStorageAddress, address handlerLogicAddress) {
        handlerStorage = PowerPlantsHandlerStorage(handlerStorageAddress);
        handlerLogic = PowerPlantsHandlerLogic(handlerLogicAddress);
    }

    function deployNewPowerPlant(
        PowerPlantCreateDTO memory dto, 
        address keyGeneratorAddress)
        external
        returns (address) {
        {
            require(
                bytes(dto.name).length > 0 &&
                    bytes(dto.name).length <= 50,
                "Name is invalid"
            );

            require(
                bytes(dto.mainProfilePicture).length > 0,
                "Picture is required"
            );
        }
        
        PowerPlant newPowerPlant = handlerLogic.createNewPowerPlant(dto, keyGeneratorAddress);
        
        if (address(newPowerPlant) == address(0)) {
            revert("Deployment failed");
        }

        {
            emit PowerPlantCreated(
                address(newPowerPlant),
                msg.sender,
                dto.name,
                dto.description,
                dto.powerPlantType,
                dto.powerPlantSubType,
                dto.powerPlantStatus,
                dto.mainProfilePicture,
                block.timestamp,
                block.timestamp
            );
        }
        {
            // save created power plant to the array with all versions of the power plant
            handlerStorage.storeDeployedPowerPlant(address(newPowerPlant));

            // generate a new key and save the power plant by key to the mapping
            handlerStorage.storePowerPlantByKey(newPowerPlant.getKey(), address(newPowerPlant));

            // generate a new id for the power plant and save to the mapping
            handlerStorage.storePowerPlantById(newPowerPlant.getId(), address(newPowerPlant));
        }

        return (address(newPowerPlant));
    }

    /* Get all versions of the power plant */
    function getDeployedPowerPlants() external view returns (address[] memory) {
        return handlerStorage.getDeployedPowerPlants();
    }

    /* Get a power plant by a key */
    function getPowerPlantByKey(string memory key)
        external
        view
        returns (address)
    {
        return handlerStorage.getPowerPlantByKey(key);
    }

    /* Get a power plant by an id */
    function getPowerPlantById(uint256 id) external view returns (address) {
        return handlerStorage.getPowerPlantById(id);
    }
}
