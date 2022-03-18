// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;

/**
 * Structures, enums and setters that can be reused for creating, updating and deploying a power plant.
 */
library PowerPlantLib {
    struct PowerPlantProfile {
        string name; // NOT NULL
        string description;
        PowerPlantType powerPlantType; // NOT NULL
        string powerPlantSubType;
        PowerPlantStatus powerPlantStatus; // NOT NULL
        string mainProfilePicture; // ipfs // NOT NULL
        uint256 creationDateTime;
        uint256 modificationDateTime;
        string[] otherPictures; // ipfs
        Location location;
    }
     
    struct Location {
        int256 latitude;
        int256 longitude;
        string humanReadableLocation;
    }

    function setLocaton(
        int256 _latitude,
        int256 _longitude,
        string memory _humanReadableLocation
    ) public pure returns (Location memory) {
        return
            Location({
                latitude: _latitude,
                longitude: _longitude,
                humanReadableLocation: _humanReadableLocation});
    }

    enum PowerPlantType {
        PHOTOVOLTAIC
    }

    enum PowerPlantStatus {
        PLANNED,
        UNDER_CONSTRUCTION,
        OPERATIONAL
    }

    enum CampaignStatus {
        PENDING,
        ACTIVE,
        BLOCKED,
        CLOSED
    }

    enum LoanType {
        SUBORDINATED_LOAN
    }

    enum DocumentCategory {
        LEGITIMATION,
        VERMITTLUNG_VERTRAG,
        FRAGEBOGEN_31WPHG,
        INFO_23AKWG,
        SECURITY_PROSPECTUS
    }

    // TODO add setter and getter
    struct PhotovoltaicPowerMetricDetails {
        uint256 powerGenerationCapacityInkW;
        uint256 expectedAnnualPowerGenerationInkWh;
        uint256 numberOfHousholdsPoweredPerYear;
        string cityEquivalentOfPower;
        uint256 savedCo2PerYearInTons;
    }
    function setPhotovoltaicPowerMetricDetails(
        uint256 _powerGenerationCapacityInk,
        uint256 _expectedAnnualPowerGenerationInkWh,
        uint256 _numberOfHousholdsPoweredPerYear,
        string memory _cityEquivalentOfPower,
        uint256 _savedCo2PerYearInTons) 
        external pure returns (PhotovoltaicPowerMetricDetails memory) 
    {
        return PhotovoltaicPowerMetricDetails({
            powerGenerationCapacityInkW: _powerGenerationCapacityInk,
            expectedAnnualPowerGenerationInkWh: _expectedAnnualPowerGenerationInkWh,
            numberOfHousholdsPoweredPerYear: _numberOfHousholdsPoweredPerYear,
            cityEquivalentOfPower: _cityEquivalentOfPower,
            savedCo2PerYearInTons: _savedCo2PerYearInTons
        });
    }

    struct CampaignDetails {
        LoanType loanType;
        uint256 expectedInterestRateInPercent;
        uint256 signingStartTimeStamp;
        uint256 signingEndTimeStamp;
        uint256 maxInvestmentAmountPerInvester;
        CampaignStatus status;
        uint256 targetAmount;
        uint256 reachedAmount;
        uint256 reservedAmount; 
        uint256 successMesurmentInPercent;
    }

    struct DocumentTemplate {
        uint id;
        DocumentCategory category;
        string fileIPFSHash;
        bool isPublic;
        string creationDateTime; // ISO 8601 format
        string modificationDateTime; // ISO 8601 format
    }
    function setDocumentTemplate( 
        uint _id,
        DocumentCategory _category,
        string memory _fileIPFSHash,
        bool _isPublic,
        string memory _creationDateTime,
        string memory _modificationDateTime) 
        external pure returns (DocumentTemplate memory) 
    {
        return DocumentTemplate({
            id: _id,
            category: _category,
            fileIPFSHash: _fileIPFSHash,
            isPublic: _isPublic,
            creationDateTime: _creationDateTime,
            modificationDateTime: _modificationDateTime
        });
    }

}
