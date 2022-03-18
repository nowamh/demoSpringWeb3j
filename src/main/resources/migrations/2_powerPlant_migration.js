const PowerPlantsHandler = artifacts.require("PowerPlantsHandler");
const InvestmentsHandler = artifacts.require("InvestmentsHandler");
const InvestmentEscrow = artifacts.require("InvestmentEscrow");
const InvestmentLib = artifacts.require("InvestmentLib");
const PowerPlant = artifacts.require("PowerPlant");
const KeyGenerator = artifacts.require("KeyGenerator");
const PowerPlantLib = artifacts.require("PowerPlantLib");
const PowerPlantsHandlerStorage = artifacts.require("PowerPlantsHandlerStorage");
const PowerPlantsHandlerLogic =artifacts.require("PowerPlantsHandlerLogic");
const InvestmentsHandlerStorage = artifacts.require("InvestmentsHandlerStorage");
const InvestmentsHandlerLogic =artifacts.require("InvestmentsHandlerLogic");
const web3 = require("web3");

module.exports = async function(deployer, network, accounts) {
  let id;

  async function Unlock(web3, address, password) {
    try {
      await web3.eth.personal.unlockAccount(address, password, 30000);
    } catch (e) {
      return false;
    }
    return true;
  }


  await deployer.deploy(InvestmentEscrow).then(async () => {
    await deployer.deploy(KeyGenerator);
    let keyGeneratorAddress = (await KeyGenerator.deployed()).address;
    await deployer.deploy(PowerPlantLib);

    deployer.link(PowerPlantLib, PowerPlantsHandler);

    await deployer.deploy(PowerPlantsHandlerStorage);
    let ppHandlerStorageAddress = (await PowerPlantsHandlerStorage.deployed()).address;

    await deployer.deploy(PowerPlantsHandlerLogic);
    let ppHandlerLogicAddress = (await PowerPlantsHandlerLogic.deployed()).address;

    await deployer.deploy(
      PowerPlantsHandler, 
      ppHandlerStorageAddress,
      ppHandlerLogicAddress);
    let powerPlantHandler = await PowerPlantsHandler.deployed();

    let escrowAddress = await InvestmentEscrow.deployed();
    await deployer.deploy(InvestmentLib);
    await deployer.link(InvestmentLib, InvestmentsHandler)

    await deployer.deploy(InvestmentsHandlerStorage);
    let investmentsHandlerStorage = (await InvestmentsHandlerStorage.deployed()).address;

    await deployer.deploy(InvestmentsHandlerLogic);
    let investmentsHandlerLogic = (await InvestmentsHandlerLogic.deployed()).address;

    let investmentHandler = await deployer.deploy(
      InvestmentsHandler, 
      investmentsHandlerStorage,
      investmentsHandlerLogic,
      escrowAddress.address);

    await Unlock(web3, accounts[0], "youki").then(async () => {

      let pp = await powerPlantHandler.deployNewPowerPlant({ 
        name: "pp", 
        description: "description", 
        powerPlantType: 0, 
        powerPlantSubType: "type", 
        powerPlantStatus: 0, 
        mainProfilePicture: "pic1", 
        location: { latitude: 0, longitude: 0, humanReadableLocation: "locationTest" } }, 
        keyGeneratorAddress,
        { from: accounts[0] });
      let get = await powerPlantHandler.getDeployedPowerPlants();
      //instanciate deployed power plant 
      let powerPlant = await PowerPlant.at(get[0]);
      // start compaign 
      await powerPlant.startCampaign().then(async () => {

        let i = await investmentHandler.startInvestment("0x38d7705fd6cf99cefd20bc9e1e4ae899944e6375", 2, get[0]);
        a1 = await powerPlant.getAllow(escrowAddress.address)
        a3 = await powerPlant.getAllow(powerPlant.address)
        id = Number(i.logs[0].args[0])

      }).then(async () => {

      /*await investmentHandler.concludeInvestment(id, true).then(async () => {

        a1 = await powerPlant.getAllow(escrowAddress.address)
        a2 = await powerPlant.getAllow("0x38d7705fd6cf99cefd20bc9e1e4ae899944e6375")
        a3 = await powerPlant.getAllow(powerPlant.address)

        // console.log(Number(a1), Number(a2), Number(a3))

        balance1 = await powerPlant.getBalance(escrowAddress.address)
        balance2 = await powerPlant.getBalance("0x38d7705fd6cf99cefd20bc9e1e4ae899944e6375")
        balance3 = await powerPlant.getBalance(powerPlant.address)

        // console.log(Number(balance1), Number(balance2), Number(balance3))

      });*/
      })
    })
  })

}