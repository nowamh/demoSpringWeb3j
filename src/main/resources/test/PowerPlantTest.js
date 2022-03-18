// SPDX-License-Identifier: MIT
const PowerPlant = artifacts.require("PowerPlant");
const PowerPlantLib = artifacts.require("PowerPlantLib");
const KeyGenerator = artifacts.require("KeyGenerator");
const { getLog } = require("./utils/txHelpers");
// Assertions for emitted events
const { expectEvent } = require('@openzeppelin/test-helpers');

contract("PowerPlant", accounts => {
    let powerPlant;
    let startCampaign;
    let compaignDetails;
    let dto;
    before(async () => {
        dto = {
            name: 'new power plant',
            description: 'This is a power plant creation test',
            powerPlantType: PowerPlantLib.PowerPlantType.PHOTOVOLTAIC,
            powerPlantSubType: 'subtype',
            powerPlantStatus: PowerPlantLib.PowerPlantStatus.PLANNED,
            mainProfilePicture: 'pic',
            location: { latitude: 0, longitude: 0, humanReadableLocation: 'loc' }
        };
        let keyGenerator = await KeyGenerator.deployed();
        // instantiate PowerPlant contract  
        powerPlant = await PowerPlant.new(dto, keyGenerator.address);

        startCampaign = await powerPlant.startCampaign();
    })
    context("deploy a new power plant ", () => {
        it("Successefully deploys a new powerPlant ", async () => {
            assert.isNotNull(powerPlant)
        })
    })
    context("get power plant details  ", () => {
        it("gets power plant name ", async () => {
            const actualResult = await powerPlant.getPowerPlantName.call();
            const expectedResult = dto.name;
            expect(expectedResult).to.eql(actualResult)
        })
        it("gets power plant address ", async () => {
            const actualResult = await powerPlant.getPowerPlantAddress.call();
            const expectedResult = powerPlant.address;
            expect(expectedResult).to.eql(actualResult)
        })
        it("gets power plant token address ", async () => {
            const result = await powerPlant.getPowerPlantTokenAddress.call();
            assert.isNotNull(result)
        })
        it("gets power plant owner address ", async () => {
            const actualResult = await powerPlant.getPowerPlantOwnerAddress.call();
            const expectedResult = accounts[0];
            expect(expectedResult).to.eql(actualResult)
        })

        it("gets power plant profile ", async () => {
            const result = await powerPlant.getPowerPlantProfile.call();
            assert.isNotNull(result)
        })
    })
    context("starts a new investment campaign ", () => {
        it("gets investment campaign details ", async () => {
            compaignDetails = await powerPlant.getCampaignDetails.call();
            assert.isNotNull(compaignDetails)
        })
        it("Successefully started campaign ", async () => {
            const actualResult = compaignDetails.status;
            const expectedResult = PowerPlantLib.CampaignStatus.ACTIVE.toString();
            expect(expectedResult).to.eql(actualResult)
        })
        it('emits a CampaignStarted event on successful campaign start', async function () {
            const log = getLog(startCampaign, "CampaignStarted")
            let timeStampStart = log.args["signingStartTimeStamp"];
            let timeStampEnd = log.args["signingEndTimeStamp"];
            // Event assertions can verify that the arguments are the expected ones
            expectEvent(startCampaign, 'CampaignStarted', {
                loanType: PowerPlantLib.LoanType.SUBORDINATED_LOAN.toString(),
                expectedInterestRateInPercent: "0",
                signingStartTimeStamp: timeStampStart,
                signingEndTimeStamp: timeStampEnd, // a campaign can be active for 3 months
                maxInvestmentAmountPerInvester: "100000", // for mvp
                status: PowerPlantLib.CampaignStatus.ACTIVE.toString(),
                targetAmount: "589",
                reachedAmount: "0",
                reservedAmount: "0"
            });
        });
    })
})
