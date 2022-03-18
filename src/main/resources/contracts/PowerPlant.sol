// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;
import "@openzeppelin/contracts/access/Ownable.sol";
import "./libraries/PowerPlantLib.sol";
import "./DTO/PowerPlantDTOs.sol";
import "./PowerPlantToken.sol";
import "./KeyGenerator.sol";

contract PowerPlant is Ownable {
    using PowerPlantLib for PowerPlantLib.PowerPlantProfile;
    using PowerPlantLib for PowerPlantLib.CampaignDetails;
    using PowerPlantLib for PowerPlantLib.DocumentTemplate;

    address private powerPlantOwner;
    address private tokenAddress;

    uint256 private id;
    string private key;

    PowerPlantLib.PowerPlantProfile private powerPlantProfile;
    PowerPlantLib.CampaignDetails private campaignDetails;

    PowerPlantLib.PhotovoltaicPowerMetricDetails[] private powerMetrics;
    PowerPlantLib.DocumentTemplate[] private documentEvents;

    event DocumentTemplateAdded(
        uint id,
        PowerPlantLib.DocumentCategory category,
        string fileIPFSHash,
        bool isPublic,
        string creationDateTime,
        string modificationDateTime
    );
    
    KeyGenerator keyGenerator;

    modifier OnlyOnDate(uint256 _date) {
        require(block.timestamp >= _date);
        _;
    }

    event CampaignStarted(
        PowerPlantLib.LoanType loanType,
        uint256 expectedInterestRateInPercent,
        uint256 signingStartTimeStamp,
        uint256 signingEndTimeStamp,
        uint256 maxInvestmentAmountPerInvester,
        PowerPlantLib.CampaignStatus status,
        uint256 targetAmount,
        uint256 reachedAmount,
        uint256 reservedAmount,
        uint256 successMesurmentInPercent
    );

    event CampaignUpdatedAfterInvestment(
        PowerPlantLib.CampaignStatus status,
        uint256 targetAmount,
        uint256 reachedAmount,
        uint256 reservedAmount
    );

    constructor(PowerPlantCreateDTO memory params, address keyGeneratorAddress)
    {
        keyGenerator = KeyGenerator(keyGeneratorAddress);

        powerPlantProfile = PowerPlantLib.PowerPlantProfile({
            name: params.name,
            description: params.description,
            powerPlantType: params.powerPlantType,
            powerPlantSubType: params.powerPlantSubType,
            powerPlantStatus: params.powerPlantStatus,
            mainProfilePicture: params.mainProfilePicture,
            creationDateTime: block.timestamp,
            modificationDateTime: block.timestamp,
            otherPictures: new string[](0),
            location: params.location
        });
        id = keyGenerator.generateId();
        key = keyGenerator.generateKey("PP");
        // define power plant token
        PowerPlantToken token = new PowerPlantToken(
            powerPlantProfile.name,
            powerPlantProfile.name
        );
        tokenAddress = address(token);
        setCampaignStatus(PowerPlantLib.CampaignStatus.PENDING);
    }

    function getId() public view returns (uint) {
        return id;
    }

    function getKey() public view returns (string memory) {
        return key;
    }

    /**
     * @dev start a campaign this function should be called when the
     *   OnlyOnDate(campaignDetails.signingStartTimeStamp)
     */
    //TODO make compaign details configurable
    function startCampaign() public payable {
        campaignDetails = PowerPlantLib.CampaignDetails({
            loanType: PowerPlantLib.LoanType.SUBORDINATED_LOAN,
            expectedInterestRateInPercent: 0,
            signingStartTimeStamp: powerPlantProfile.creationDateTime,
            signingEndTimeStamp: powerPlantProfile.creationDateTime + (90 days), // a campaign can be active for 3 months
            maxInvestmentAmountPerInvester: 100000, // for mvp
            status: PowerPlantLib.CampaignStatus.PENDING,
            targetAmount: 589,
            reachedAmount: 0,
            reservedAmount: 0,
            successMesurmentInPercent: 60
        });
        PowerPlantToken(tokenAddress).mint(
            address(this),
            campaignDetails.targetAmount
        );
        setCampaignStatus(PowerPlantLib.CampaignStatus.ACTIVE);

        emit CampaignStarted(
            campaignDetails.loanType,
            campaignDetails.expectedInterestRateInPercent,
            campaignDetails.signingStartTimeStamp,
            campaignDetails.signingEndTimeStamp,
            campaignDetails.maxInvestmentAmountPerInvester,
            campaignDetails.status,
            campaignDetails.targetAmount,
            campaignDetails.reachedAmount,
            campaignDetails.reservedAmount,
            campaignDetails.successMesurmentInPercent
        );
    }

    /**
     * @dev updates the compaign details after each investment
     */
    function updateCompaignAfterInvestment() public payable {

        // check the status of the compaign
        if (
            campaignDetails.reachedAmount + campaignDetails.reservedAmount >=
            campaignDetails.targetAmount
        ) {
            setCampaignStatus(PowerPlantLib.CampaignStatus.BLOCKED);
        } else if (
            campaignDetails.reachedAmount == campaignDetails.targetAmount
        ) {
            setCampaignStatus(PowerPlantLib.CampaignStatus.CLOSED);
        }

        emit CampaignUpdatedAfterInvestment(
            campaignDetails.status,
            campaignDetails.targetAmount,
            campaignDetails.reachedAmount,
            campaignDetails.reservedAmount
        );
    }

    /**
     * @dev set campaign status
     */
    function setCampaignStatus(PowerPlantLib.CampaignStatus status) internal {
        campaignDetails.status = status;
    }

    /**
     * @dev get powerPlant address
     * @return
     */
    function getPowerPlantAddress() public view returns (address) {
        return address(this);
    }

    /**
     * @dev get powerPlant address
     * @return
     */
    function getPowerPlantTokenAddress() public payable returns (address) {
        return tokenAddress;
    }

    /**
     * @dev get powerPlant name
     * @return
     */
    function getPowerPlantName() public view returns (string memory) {
        return powerPlantProfile.name;
    }

    /**
     * @dev get powerPlant owner address
     * @return
     */
    function getPowerPlantOwnerAddress() public view returns (address) {
        return msg.sender;
    }

    /**
     * @dev get powerPlant campaign details
     * @return
     */
    function getCampaignDetails()
        public
        view
        returns (PowerPlantLib.CampaignDetails memory)
    {
        return campaignDetails;
    }

    /**
     * @dev get powerPlant profile details
     * @return
     */
    function getPowerPlantProfile()
        public
        view
        returns (PowerPlantLib.PowerPlantProfile memory)
    {
        return powerPlantProfile;
    }

    function getReachedAmount() public view returns (uint256) {
        return campaignDetails.reachedAmount;
    }

    function getTargetAmount() public view returns (uint256) {
        return campaignDetails.targetAmount;
    }

    function getReservedAmount() public view returns (uint256) {
        return campaignDetails.reservedAmount;
    }

    function addFunds(uint256 _amount) public {
        campaignDetails.reachedAmount += _amount;
    }

    function freezeAmount(uint256 _amount) public {
        campaignDetails.reservedAmount += _amount;
    }

    function unfreezeAmount(uint256 _amount) public {
        campaignDetails.reservedAmount -= _amount;
    }

    // TODO needs to be executed by the token owner
    function approveTokenAccess(address to, uint256 amount) public {
        PowerPlantToken(tokenAddress).approve(to, amount);
    }

    function getAllow(address to) public view returns (uint256) {
        return PowerPlantToken(tokenAddress).allowance(address(this), to);
    }

    function getBalance(address to) public view returns (uint256) {
        return PowerPlantToken(tokenAddress).balanceOf(to);
    }

    function checkSuccessMesurmentInPercentAchieved()
        external 
        view
        returns (bool) 
    {
        uint achievedPersentage = campaignDetails.targetAmount / 100 * campaignDetails.successMesurmentInPercent;
        if (campaignDetails.reachedAmount >= achievedPersentage) {
            return true;
        } else {
            return false;
        }
    }
}
