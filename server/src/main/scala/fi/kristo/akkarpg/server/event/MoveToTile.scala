package fi.kristo.akkarpg.server.event

import akka.actor.ActorRef
import fi.kristo.akkarpg.event.Location

case class MoveToTile(playerId: String, location: Location, clientRef: ActorRef)