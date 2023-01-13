/**
 * Copyright (c) 2020 EmeraldPay, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.emeraldpay.dshackle

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.emeraldpay.dshackle.monitoring.MonitoringContext
import io.emeraldpay.dshackle.upstream.bitcoin.data.EsploraUnspent
import io.emeraldpay.dshackle.upstream.bitcoin.data.EsploraUnspentDeserializer
import io.emeraldpay.dshackle.upstream.bitcoin.data.RpcUnspent
import io.emeraldpay.dshackle.upstream.bitcoin.data.RpcUnspentDeserializer
import io.emeraldpay.dshackle.upstream.ethereum.subscribe.json.TransactionIdSerializer
import io.emeraldpay.dshackle.upstream.rpcclient.JsonRpcRequest
import io.emeraldpay.dshackle.upstream.rpcclient.JsonRpcResponse
import io.emeraldpay.etherjar.domain.TransactionId
import io.emeraldpay.grpc.Chain
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class Global {

    companion object {

        var metricsExtended = false

        val chainNames = mapOf(
            "ethereum" to Chain.ETHEREUM,
            "ethereum-classic" to Chain.ETHEREUM_CLASSIC,
            "eth" to Chain.ETHEREUM,
            "polygon" to Chain.MATIC,
            "polygon-mainnet" to Chain.MATIC,
            "matic" to Chain.MATIC,
            "polygon-mumbai" to Chain.TESTNET_MUMBAI,
            "polygon-testnet" to Chain.TESTNET_MUMBAI,
            "mumbai" to Chain.TESTNET_MUMBAI,
            "bsc" to Chain.BSC,
            "bsc-mainnet" to Chain.BSC,
            "binance-smart-chain" to Chain.BSC,
            "bnb" to Chain.BSC,
            "bnb-mainnet" to Chain.BSC,
            "bnb-chain" to Chain.BSC,
            "bsc-testnet" to Chain.TESTNET_BSC,
            "bnb-testnet" to Chain.TESTNET_BSC,
            "bnb-chain-testnet" to Chain.TESTNET_BSC,
            "avax" to Chain.AVAX,
            "avax-mainnet" to Chain.AVAX,
            "avalanche" to Chain.AVAX,
            "avalanche-c-chain" to Chain.AVAX,
            "avalanche-mainnet" to Chain.AVAX,
            "avax-testnet" to Chain.TESTNET_FUJI,
            "fuji" to Chain.TESTNET_FUJI,
            "avalanche-fuji" to Chain.TESTNET_FUJI,
            "avalanche-testnet" to Chain.TESTNET_FUJI,
            "etc" to Chain.ETHEREUM_CLASSIC,
            "ethereum-classic" to Chain.ETHEREUM_CLASSIC,
            "morden" to Chain.TESTNET_MORDEN,
            "kovan" to Chain.TESTNET_KOVAN,
            "kovan-testnet" to Chain.TESTNET_KOVAN,
            "goerli" to Chain.TESTNET_GOERLI,
            "goerli-testnet" to Chain.TESTNET_GOERLI,
            "rinkeby" to Chain.TESTNET_RINKEBY,
            "rinkeby-testnet" to Chain.TESTNET_RINKEBY,
            "ropsten" to Chain.TESTNET_ROPSTEN,
            "ropsten-testnet" to Chain.TESTNET_ROPSTEN,
            "sepolia" to Chain.TESTNET_SEPOLIA,
            "sepolia-testnet" to Chain.TESTNET_SEPOLIA,
            "bitcoin" to Chain.BITCOIN,
            "bitcoin-testnet" to Chain.TESTNET_BITCOIN,
            "arbitrum" to Chain.ARBITRUM,
            "arbitrum-mainnet" to Chain.ARBITRUM,
            "arbitrum-testnet" to Chain.TESTNET_ARBITRUM
            "moonbeam" to Chain.MOONBEAM,
            "moonbeam-mainnet" to Chain.MOONBEAM,
            "celo" to Chain.CELO,
            "celo-mainnet" to Chain.CELO,
            "evmos" to Chain.EVMOS,
            "evmos-mainnet" to Chain.EVMOS,
            "evmos-testnet" to Chain.TESTNET_EVMOS,
            "cronos" to Chain.CRONOS,
            "cronos-mainnet" to Chain.CRONOS,
            "cronos-testnet" to Chain.TESTNET_CRONOS,
            "neon" to Chain.NEON,
            "neon-mainnet" to Chain.NEON,
            "neon-testnet" to Chain.TESTNET_NEON,
            "optimism" to Chain.OPTIMISM,
            "optimism-mainnet" to Chain.OPTIMISM,
            "optimism-testnet" to Chain.TESTNET_OPTIMISM,
            "eos" to Chain.EOS,
            "eos-mainnet" to Chain.EOS,
            "milkomeda" to Chain.MILKOMEDA,
            "milkomeda-mainnet" to Chain.MILKOMEDA,
            "milkomeda-testnet" to Chain.TESTNET_MILKOMEDA,
            "fantom" to Chain.FANTOM,
            "fantom-mainnet" to Chain.FANTOM,
            "fantom-testnet" to Chain.TESTNET_FANTOM,
            "rsk" to Chain.RSK,
            "rsk-mainnet" to Chain.RSK,
            "rsk-testnet" to Chain.TESTNET_RSK,
        )

        fun chainById(id: String?): Chain {
            if (id == null) {
                return Chain.UNSPECIFIED
            }
            return chainNames[
                id.lowercase(Locale.getDefault()).replace("_", "-").trim()
            ] ?: Chain.UNSPECIFIED
        }

        @JvmStatic
        val objectMapper: ObjectMapper = createObjectMapper()

        var version: String = "DEV"

        val control: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

        val monitoring: MonitoringContext = MonitoringContext()

        private fun createObjectMapper(): ObjectMapper {
            val module = SimpleModule("EmeraldDshackle", Version(1, 0, 0, null, null, null))
            module.addSerializer(JsonRpcResponse::class.java, JsonRpcResponse.ResponseJsonSerializer())
            module.addSerializer(TransactionId::class.java, TransactionIdSerializer())

            module.addDeserializer(EsploraUnspent::class.java, EsploraUnspentDeserializer())
            module.addDeserializer(RpcUnspent::class.java, RpcUnspentDeserializer())
            module.addDeserializer(JsonRpcRequest::class.java, JsonRpcRequest.Deserializer())

            val objectMapper = ObjectMapper()
            objectMapper.registerModule(module)
            objectMapper.registerModule(Jdk8Module())
            objectMapper.registerModule(JavaTimeModule())
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            objectMapper
                .setDateFormat(SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSS"))
                .setTimeZone(TimeZone.getTimeZone("UTC"))

            return objectMapper
        }
    }
}
