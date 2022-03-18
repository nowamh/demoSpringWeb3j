// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;
import "../libraries/PowerPlantLib.sol";

/**
 * Power Plant creating params
 */
struct PowerPlantCreateDTO {
    string name; // (NOT NULL)
    string description;
    PowerPlantLib.PowerPlantType powerPlantType; // (NOT NULL)
    string powerPlantSubType;
    PowerPlantLib.PowerPlantStatus powerPlantStatus; // (NOT NULL)
    string mainProfilePicture; // (NOT NULL)
    PowerPlantLib.Location location;
}
