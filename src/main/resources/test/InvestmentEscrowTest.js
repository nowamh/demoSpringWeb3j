// SPDX-License-Identifier: MIT
const InvestmentEscrow = artifacts.require("InvestmentEscrow");
const PowerPlantsHandler = artifacts.require("PowerPlantsHandler");
const InvestmentLib = artifacts.require("InvestmentLib");
const PowerPlant = artifacts.require("PowerPlant");
const KeyGenerator = artifacts.require("KeyGenerator");

contract("InvestmentEscrow", accounts => {
    const contractCreator = accounts[0];

    let escrow;
    let powerPlantHandler;
    let powerPlants;
    let powerPlant;
    let investment;
    const investor = "0x38d7705FD6cF99cefD20BC9E1E4Ae899944e6375";

    before(async () => {
        let keyGenerator = await KeyGenerator.deployed();

        const dto = { 
            name: "pp", 
            description: "this is a new test description ", 
            powerPlantType: 0, 
            powerPlantSubType: "type", 
            powerPlantStatus: 0, 
            mainProfilePicture: "picture1", 
            location: {latitude: 0, longitude: 0, humanReadableLocation: "locationTest"} 
        };

        powerPlantHandler = await PowerPlantsHandler.deployed();
        await powerPlantHandler.deployNewPowerPlant(
            dto, 
            keyGenerator.address,
            { from: accounts[0] });
        powerPlants = await powerPlantHandler.getDeployedPowerPlants();
        powerPlant = await PowerPlant.at(powerPlants[0])
        // instantiate InvestmentEscrow contract
        escrow = await InvestmentEscrow.new({ from: contractCreator })
        investment = {
            id: "0", 
            project: powerPlants[0], 
            investor: investor, 
            amount: "2", 
            state: InvestmentLib.InvestmentState.PENDING.toString()
        };
    })
    context("Deploy a new InvestmentEscrow ", () => {
        it("deploys successfully", async () => {
            assert.isNotNull(escrow)
        })
    })
    context("Blocks token after investment initiated ", () => {
        it("Blocks successfully", async () => {
            let blockTokens = await escrow.blockTokensForInvestment(investment);
            assert.isNotNull(blockTokens)
        })
    })
    context("Unblock tokens after payment done ", () => {
        it("Shouldn't unblock if payment isn't done", async () => {
            try {
                await escrow.unblockTokensForInvestment(investment, false);
                expect.fail();
            } catch (err) {
                expect(err.message).to.include("Unsuccessful");
            }
        })
        it("Should unblock after payment done", async () => {
            const currReserverAmount = await powerPlant.getReservedAmount.call();
            await escrow.unblockTokensForInvestment(investment, true);
            const updatedReserverAmount = await powerPlant.getReservedAmount.call();
            assert.notEqual(currReserverAmount, updatedReserverAmount, "Reserved amount should be different")
        })
    })
    context("Withdrow tokens ", () => {
        it("Shouldn't withdrow ", async () => {
            try {
                await escrow.withdrawToInvestor(
                    powerPlants[0], 
                    investor, 
                    "2");
                expect.fail();
            } catch (err) {
                expect(err.message).to.include("Can't execute now");
            }
        })
    })

})
