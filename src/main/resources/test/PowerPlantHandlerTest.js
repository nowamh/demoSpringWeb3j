// SPDX-License-Identifier: MIT
const PowerPlantsHandler = artifacts.require("PowerPlantsHandler");
const PowerPlant = artifacts.require("PowerPlant");
const PowerPlantLib = artifacts.require("PowerPlantLib");
const PowerPlantsHandlerStorage = artifacts.require("PowerPlantsHandlerStorage");
const PowerPlantsHandlerLogic =artifacts.require("PowerPlantsHandlerLogic");
const KeyGenerator = artifacts.require("KeyGenerator");
const { getLog } = require("./utils/txHelpers");
// Assertions for emitted events
const { expectEvent } = require('@openzeppelin/test-helpers');

contract("PowerPlantsHandler", accounts => {
    const contractCreator = accounts[0];
    let powerPlantHandler;
    let powerPlant;
    let powerPlantAddress;
    let dto;
    let eventTx;
    let log;
    let timestamp;

    before(async () => {
        let ppHandlerStorage = await PowerPlantsHandlerStorage.deployed();
        let ppHandlerLogic = await PowerPlantsHandlerLogic.deployed();
        let keyGenerator = await KeyGenerator.deployed();

        dto = {
            name: 'new power plant',
            description: 'This is a power plant creation test',
            powerPlantType: PowerPlantLib.PowerPlantType.PHOTOVOLTAIC.toString(),
            powerPlantSubType: 'subtype',
            powerPlantStatus: PowerPlantLib.PowerPlantStatus.PLANNED.toString(),
            mainProfilePicture: 'mainPic',
            location: { latitude: 0, longitude: 0, humanReadableLocation: 'loc' }
        };
        // instantiate PowerPlantHandler contract
        powerPlantHandler = await PowerPlantsHandler.new(
            ppHandlerStorage.address, 
            ppHandlerLogic.address);
        
        // deploy a new power plant
        eventTx = await powerPlantHandler.deployNewPowerPlant(dto, keyGenerator.address);
        log = getLog(eventTx, "PowerPlantCreated")
       
        powerPlantAddress = log.args["powerPlantAddress"]
        powerPlant = await PowerPlant.at(powerPlantAddress)
        timestamp = log.args["creationDateTime"]

    })
    context("deploy a power plant handler ", () => {
        it("Successefully deploys a new powerPlantHandler ", async () => {
            assert.isNotNull(powerPlantHandler)
        })
    })
    context("Deploy a new power Plant ", () => {
        it("deploys successfully", async () => {
            assert.isNotNull(powerPlantAddress)
            assert.isNotNull(powerPlant)
        })
        it('emits a PowerPlantDetails event on successful deployment', async function () {
            // Event assertions can verify that the arguments are the expected ones
            expectEvent(eventTx, 'PowerPlantCreated', {
                powerPlantAddress: powerPlantAddress,
                ownerAddress: contractCreator,
                name: 'new power plant',
                description: 'This is a power plant creation test',
                powerPlantType: PowerPlantLib.PowerPlantType.PHOTOVOLTAIC.toString(),
                powerPlantSubType: 'subtype',
                powerPlantStatus: PowerPlantLib.PowerPlantStatus.PLANNED.toString(),
                mainProfilePicture: 'mainPic',
                creationDateTime: timestamp,
                modificationDateTime: timestamp
            });
        });

        it("gets deployed power plants ", async () => {
            const deployedPowerPlants = await powerPlantHandler.getDeployedPowerPlants.call();
            assert.isNotNull(deployedPowerPlants)
        });
        it("gets power plant by key ", async () => {
            const key = await powerPlant.getKey.call();
            const expectedResult = await powerPlantHandler.getPowerPlantByKey.call(key);
            expect(expectedResult).to.eql(powerPlantAddress)
        });
        it("gets power plant by id ", async () => {
            const id = await powerPlant.getId.call();
            const expectedResult = await powerPlantHandler.getPowerPlantById.call(id);
            expect(expectedResult).to.eql(powerPlantAddress)
        });
    })

})
