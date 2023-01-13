package io.emeraldpay.dshackle.upstream.calls

import io.emeraldpay.grpc.Chain
import spock.lang.Specification

class DefaultEthereumMethodsSpec extends Specification {

    def "eth_chainId is available"() {
        setup:
        def methods = new DefaultEthereumMethods(Chain.ETHEREUM)
        when:
        def act = methods.isAvailable("eth_chainId")
        then:
        act
    }

    def "eth_chainId is hardcoded"() {
        setup:
        def methods = new DefaultEthereumMethods(Chain.ETHEREUM)
        when:
        def act = methods.isHardcoded("eth_chainId")
        then:
        act
    }

    def "eth_chainId is not callable"() {
        setup:
        def methods = new DefaultEthereumMethods(Chain.ETHEREUM)
        when:
        def act = methods.isCallable("eth_chainId")
        then:
        !act
    }

    def "Provides hardcoded correct chainId"() {
        expect:
        new String(new DefaultEthereumMethods(chain).executeHardcoded("eth_chainId")) == id
        where:
        chain                  | id
        Chain.ETHEREUM         | '"0x1"'
        Chain.ETHEREUM_CLASSIC | '"0x3d"'
        Chain.BSC              | '"0x38"'
        Chain.MATIC            | '"0x89"'
        Chain.AVAX             | '"0xa86a"'
        Chain.ARBITRUM         | '"0xa4b1"'
        Chain.NOVA             | '"0xa4ba"'
        Chain.MOONBEAM         | '"0x504"'
        Chain.CELO             | '"0xa4eC"'
        Chain.EVMOS            | '"0x2329"'
        Chain.CRONOS           | '"0x19"'
        Chain.NEON             | '"0xe9aC0d6"'
        Chain.OPTIMISM         | '"0xa"'
        Chain.EOS              | '"0x3b"'
        Chain.MILKOMEDA        | '"0x7d1"'
        Chain.TESTNET_KOVAN    | '"0x2a"'
        Chain.TESTNET_GOERLI   | '"0x5"'
        Chain.TESTNET_RINKEBY  | '"0x4"'
        Chain.TESTNET_ROPSTEN  | '"0x3"'
        Chain.TESTNET_SEPOLIA  | '"0xaa36a7"'
        Chain.TESTNET_BSC      | '"0x61"'
        Chain.TESTNET_MUMBAI   | '"0x13881"'
        Chain.TESTNET_FUJI     | '"0xa869"'
        Chain.TESTNET_OPTIMISM | '"0x12c"'
        Chain.TESTNET_ARBITRUM | '"0x66eed"'
        Chain.TESTNET_FANTOM   | '"0xfa2"'
        Chain.TESTNET_EVMOS    | '"0x2328"'
        Chain.TESTNET_CRONOS   | '"0x152"'
        Chain.TESTNET_NEON     | '"0xe9ac0dc"'
        Chain.TESTNET_MILKOMEDA| '"0x30da5"'
        Chain.TESTNET_RSK      | '"0x1f"'
    }
}
