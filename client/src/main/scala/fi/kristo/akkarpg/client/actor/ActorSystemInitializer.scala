package fi.kristo.akkarpg.client.actor

import javax.annotation.PreDestroy

import akka.actor.{ActorSystem, Props}
import fi.kristo.akkarpg.client.event.StartGame
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.PropertyResolver
import org.springframework.stereotype.Component

@Component
class ActorSystemInitializer @Autowired()(propertyResolver: PropertyResolver) {
    val playerName = propertyResolver.getRequiredProperty("game.player.name")
    val serverUri = propertyResolver.getRequiredProperty("server.uri")

    val system = ActorSystem("akka-rpg-client")
    val client = system.actorOf(Props(new Client(serverUri, playerName)), "client")

    client ! StartGame

    @PreDestroy
    private def shutdown() = {
        system.shutdown()
    }
}
