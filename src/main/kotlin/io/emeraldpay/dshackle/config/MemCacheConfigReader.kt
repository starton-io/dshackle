package io.emeraldpay.dshackle.config

import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.nodes.MappingNode
import java.io.InputStream

class MemCacheConfigReader : YamlConfigReader(), ConfigReader<MemCacheConfig> {

    companion object {
        private val log = LoggerFactory.getLogger(MemCacheConfigReader::class.java)
    }

    fun read(input: InputStream): MemCacheConfig? {
        val configNode = readNode(input)
        return read(configNode)
    }

    override fun read(input: MappingNode?): MemCacheConfig? {
        return getMapping(input, "mem-cache")?.let { node ->
            val config = MemCacheConfig()
            config.globalEnabled = getValueAsBool(node, "global-enabled") ?: false
            config.height = getValueAsBool(node, "height") ?: false
            config.block = getValueAsBool(node, "block") ?: false
            config.tx = getValueAsBool(node, "tx") ?: false
            config.receipt = getValueAsBool(node, "receipt") ?: false
            log.info("Memory cache configuration...")
            log.info("Globaly enabled: ${config.globalEnabled}")
            log.info("Block: ${config.block}")
            log.info("Height ${config.height}")
            log.info("Tx: ${config.tx}")
            log.info("Receipt: ${config.receipt}")
            config
        }
    }
}