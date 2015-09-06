package fi.kristo.akkarpg.server.actor

import java.util.UUID

import akka.actor.{Actor, Props}
import fi.kristo.akkarpg.event.{JoinRequest, Location, Move}
import fi.kristo.akkarpg.server.event.MoveToTile
import grizzled.slf4j.Logging

object ServerActor {
    def props(mapGrid: Array[Array[String]]): Props = Props(new ServerActor(mapGrid))
}

class ServerActor(mapGrid: Array[Array[String]]) extends Actor with Logging {
    private val mapActorRef = context.actorOf(MapActor.props(mapGrid), "map")

    override def receive: Receive = {
        case JoinRequest(playerName) =>
            placePlayerToGame(playerName)
        case Move(playerId, fromLocation, toLocation) =>
            movePlayer(playerId, fromLocation, toLocation)
        case unknown =>
            warn(s"Received unknown message: $unknown")
    }

    private def placePlayerToGame(playerName: String) {
        info(s"Player $playerName joined the game")
        val playerId = UUID.randomUUID().toString

        placePlayerToEmptyMapTile(playerId)
    }

    def placePlayerToEmptyMapTile(playerId: String) {
        // TODO find empty map tile
        val location = Location(0, 0)
        val mapTileActorName = MapActor.childName(location)
        context.actorSelection(mapActorRef.path./(mapTileActorName)) ! MoveToTile(playerId, location, sender())
    }

    private def movePlayer(playerId: String, fromLocation: Location, toLocation: Location) {
        info(s"Player $playerId wants to move from x=${fromLocation.x}:y=${fromLocation.y} to x=${toLocation.x}:y=${toLocation.y}")
        val mapTileActorName = MapActor.childName(toLocation)
        context.actorSelection(mapActorRef.path./(mapTileActorName)) ! MoveToTile(playerId, toLocation, sender())
    }
}
