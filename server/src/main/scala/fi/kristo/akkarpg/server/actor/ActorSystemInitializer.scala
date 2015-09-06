package fi.kristo.akkarpg.server.actor

import javax.annotation.PreDestroy

import akka.actor.ActorSystem
import fi.kristo.akkarpg.server.map.MapContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ActorSystemInitializer @Autowired() (val mapContainer: MapContainer) {
    val system = ActorSystem("akka-rpg-server")
    system.actorOf(ServerActor.props(mapContainer.grid), "server")

    @PreDestroy
    private def shutdown() = {
        system.shutdown()
    }
}
