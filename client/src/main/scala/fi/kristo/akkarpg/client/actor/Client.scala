package fi.kristo.akkarpg.client.actor

import akka.actor.Actor
import fi.kristo.akkarpg.client.event.StartGame
import fi.kristo.akkarpg.event.{MoveFailed, JoinAccepted, JoinRequest, MapVisualization}
import grizzled.slf4j.Logging

class Client(serverUri: String, playerName: String) extends Actor with Logging {
    val server = context.actorSelection(serverUri)

    override def receive: Receive = startGame

    private def startGame: Receive = {
        case StartGame =>
            info(s"Sending join request to server as player: $playerName")
            server ! JoinRequest(playerName)
        case MoveFailed(reason) =>
            info(s"Failed to move to map tile: $reason")
        case JoinAccepted(playerId) =>
            info(s"Joined game, received playerId: $playerId")
            context become playGame(playerId)
    }

    private def playGame(playerId: String): Receive = {
        case MapVisualization(map) =>
            info(s"\n${map}")
        case _ => error("Received unknown event")
    }
}
