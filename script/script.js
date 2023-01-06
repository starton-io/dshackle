const axios = require('axios')

async function callEthereumBlockchain() {
    console.log('\n\n\n----- ETHEREUM -----')
    const res = await axios.post(
        'http://localhost:8545/eth',
        {
            jsonrpc: "2.0",
            method: "eth_getBalance",
            id: 1,
            params: [
                "0x690b2bdf41f33f9f251ae0459e5898b856ed96be",
                "latest"
            ]
        }
    )

    console.log(res.data)
}

async function callBscBlockchain() {
    console.log('\n\n\n----- BSC -----')
    const res = await axios.post(
        'http://localhost:8545/bsc',
        {
            jsonrpc: "2.0",
            method: "eth_getBalance",
            id: 1,
            params: [
                "0x690b2bdf41f33f9f251ae0459e5898b856ed96be",
                "latest"
            ]
        }
    )

    console.log(res.data)
}

async function start() {
    await callEthereumBlockchain()
    await callBscBlockchain()
}

start()
.then(() => { console.log('----- END -----') })
.catch((e) => { console.log('----- ERROR -----\n', e, '----- ERROR -----') })