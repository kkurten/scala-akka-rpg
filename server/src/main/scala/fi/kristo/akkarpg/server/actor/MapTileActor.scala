package fi.kristo.akkarpg.server.actor

import akka.actor.{ActorRef, Actor, Props}
import fi.kristo.akkarpg.event.{JoinAccepted, Location, MoveFailed}
import fi.kristo.akkarpg.server.event.{MoveToTile, UpdateMap}
import grizzled.slf4j.Logging

private object MapTileType {
    val Empty = "*"
    val Tree = "|"
    val Rock = "O"
    val Player = "X"
}

object MapTileActor {
    def props(mapActor: ActorRef, location: Location, content: String): Props = {
        Props(new MapTileActor(mapActor, location, content))
    }
}

class MapTileActor(val mapActorRef: ActorRef, val location: Location, val content: String) extends Actor with Logging {
    info(s"Map tile created: x=${location.x}:y=${location.y}, content=$content")

    override def receive: Receive = borderControl(Option.empty)

    private def borderControl(currentPlayerId: Option[String]): Receive = {
        case MoveToTile(newPlayerId, fromLocation, clientRef) =>
            currentPlayerId match {
                case Some(playerId) =>
                    alreadyHasPlayer(playerId, clientRef)
                case None =>
                    welcomeNewPlayer(newPlayerId, fromLocation, clientRef)
            }
    }

    def alreadyHasPlayer(playerId: String, clientRef: ActorRef) {
        val moveFailedReason = s"Map tile x=${location.x}:y=${location.y} is already occupied by player $playerId"
        info(moveFailedReason)
        clientRef ! MoveFailed(moveFailedReason)
    }

    def welcomeNewPlayer(newPlayerId: String, fromLocation: Location, clientRef: ActorRef) {
        info(s"Player $newPlayerId moved from x=${fromLocation.x}:y=${fromLocation.y} to x=${location.x}:y=${location.y}")
        clientRef ! JoinAccepted(newPlayerId)
        mapActorRef ! UpdateMap(fromLocation, location, clientRef)
        context become borderControl(Option(newPlayerId))
    }
}
