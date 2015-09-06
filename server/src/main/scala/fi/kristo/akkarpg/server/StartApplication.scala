package fi.kristo.akkarpg.server

import fi.kristo.akkarpg.server.config.ServerConfig
import org.springframework.boot.SpringApplication

object StartApplication extends App {
    SpringApplication.run(classOf[ServerConfig])
}
