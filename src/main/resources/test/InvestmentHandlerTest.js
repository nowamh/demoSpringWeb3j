// SPDX-License-Identifier: MIT
const InvestmentsHandler = artifacts.require("InvestmentsHandler");
const InvestmentsHandlerStorage = artifacts.require("InvestmentsHandlerStorage");
const InvestmentsHandlerLogic = artifacts.require("InvestmentsHandlerLogic");
const InvestmentEscrow = artifacts.require("InvestmentEscrow");
const KeyGenerator = artifacts.require("KeyGenerator");
const PowerPlantsHandler = artifacts.require("PowerPlantsHandler");
// Assertions for emitted events
const { expectEvent } = require('@openzeppelin/test-helpers');

contract("InvestmentsHandler", accounts => {
    const contractCreator = accounts[0];

    let investmentHandler;
    let powerPlantHandler;
    let powerPlants;
    let invest;
    let _id;
    const investor = "0x38d7705FD6cF99cefD20BC9E1E4Ae899944e6375";
    before(async () => {
        // instantiate InvestmentEscrow contract
        const handlerStorage = await InvestmentsHandlerStorage.deployed();
        const handlerLogic = await InvestmentsHandlerLogic.deployed();
        const escrow = await InvestmentEscrow.deployed();
        powerPlantHandler = await PowerPlantsHandler.deployed();
        let keyGenerator = await KeyGenerator.deployed();
        await powerPlantHandler.deployNewPowerPlant({ 
            name: "pow pow", 
            description: "this is a new test description ", 
            powerPlantType: 0, 
            powerPlantSubType: "type", 
            powerPlantStatus: 0, 
            mainProfilePicture: "picture1", 
            location: { latitude: 0, longitude: 0, humanReadableLocation: "locationTest" } }, 
            keyGenerator.address,
            { from: accounts[0] });
        powerPlants = await powerPlantHandler.getDeployedPowerPlants();
        // instantiate InvestmentsHandler handler contract
        investmentHandler = await InvestmentsHandler.new(
            handlerStorage.address,
            handlerLogic.address,
            escrow.address, 
            { from: contractCreator }
        )
    })
    context("Deploy a new investmentHandler ", () => {
        it("deploys successfully", async () => {
            assert.isNotNull(investmentHandler)
        })
    })
    context("Starts investing ", () => {
        it("starts investment successfully", async () => {
            invest = await investmentHandler.startInvestment(
                investor, 
                2,
                powerPlants[0]
            );
            assert.isNotNull(invest)
        })
        it('emits a NewInvestmentAttempt event on successful investment start', async function () {
            _id = Number(invest.logs[0].args[0]).toString()
            expectEvent(invest, 'NewInvestmentAttempt', {
                id: _id, 
                investor: investor, 
                project: powerPlants[0], 
                amount: "2"
            });
        });
    })
    context("Concludes investment ", () => {
        it("Shouldn't conclude if requirements not met", async () => {
            try {
                await investmentHandler.concludeInvestment(_id, false);
                expect.fail();
            } catch (err) {
                expect(err.message).to.include("Can't be concluded");
            }
        })
        it("Shouldn't conclude because of the signing end timestamp", async () => {
            try {
                await investmentHandler.concludeInvestment(_id, true);
                expect.fail();
            } catch (err) {
                expect(err.message).to.include("Can't execute now");
            }
        })
    })
})
