// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;
import "@openzeppelin/contracts/access/Ownable.sol";

/**
 * A smart contract for generating unique keys and id of a power plant.
 */
contract KeyGenerator is Ownable {
   
    uint currentId = 0;
    uint ppKeyIncrement = 0;

    function generateId() public returns (uint) {
        return ++currentId;
    }

    function generateKey(string memory keyWord) public returns (string memory) {

        return string(abi.encodePacked(keyWord, ++ppKeyIncrement));
    }

}
