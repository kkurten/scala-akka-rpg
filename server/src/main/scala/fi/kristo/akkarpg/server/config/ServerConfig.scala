package fi.kristo.akkarpg.server.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.{ComponentScan, Configuration}

@Configuration
@EnableAutoConfiguration
@ComponentScan(value = Array("fi.kristo.akkarpg.server"))
class ServerConfig