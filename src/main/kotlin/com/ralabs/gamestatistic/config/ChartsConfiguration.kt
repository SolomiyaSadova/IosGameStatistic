package com.ralabs.gamestatistic.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
@ConfigurationProperties(prefix = "charts")
class ChartsConfiguration {
    private val logger: Logger = LoggerFactory.getLogger(ChartsConfiguration::class.java)
    // values comes from fetchUrls.yaml file
    var urls: List<String> = listOf()
    @PostConstruct
    fun init() {
        urls.forEach { logger.info("Load chart URL: $it") }
    }
}