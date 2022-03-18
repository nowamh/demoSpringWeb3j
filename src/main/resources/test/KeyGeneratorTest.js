// SPDX-License-Identifier: MIT
const KeyGenerator = artifacts.require("KeyGenerator");

contract("KeyGenerator", accounts => {

    let keyGenerator;

    before(async () => {
        keyGenerator = await KeyGenerator.new();
    })
    context("deploy a new key generator ", () => {
        it("Successefully deploys a new key generator ", async () => {
            assert.isNotNull(keyGenerator)
        })
    })
    context("generate key and id ", () => {
        it("Successefully generates an id ", async () => {
            const id = await keyGenerator.generateId.call();
            assert.isNotNull(id)
        })
        it("Successefully generates a key ", async () => {
            const keyWord = "PP";
            const key = await keyGenerator.generateKey.call(keyWord);
            assert.isNotNull(key)
        })
    })
})