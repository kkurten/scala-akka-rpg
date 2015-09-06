package fi.kristo.akkarpg.server.event

import akka.actor.ActorRef
import fi.kristo.akkarpg.event.Location

case class UpdateMap(fromLocation: Location, toLocation: Location, clientRef: ActorRef)
