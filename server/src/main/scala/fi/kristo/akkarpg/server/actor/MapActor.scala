package fi.kristo.akkarpg.server.actor

import akka.actor.{Actor, Props}
import fi.kristo.akkarpg.event.{MapVisualization, Location}
import fi.kristo.akkarpg.server.event.UpdateMap
import grizzled.slf4j.Logging

object MapActor {
    def props(grid: Array[Array[String]]): Props = Props(new MapActor(grid))

    def childName(location: Location): String = {
        childName(location.x, location.y)
    }

    private def childName(x: Int, y: Int): String = {
        s"x${x}y${y}"
    }
}

class MapActor(val initialGrid: Array[Array[String]]) extends Actor with Logging {
    createMapTileActors()

    override def receive: Receive = updateMap(initialGrid)

    private def updateMap(currentGrid: Array[Array[String]]): Receive = {
        case UpdateMap(fromLocation, toLocation, clientRef) =>
            val newMapGrid = buildNewMapGrid(currentGrid, fromLocation, toLocation)
            val newVisualMap = newMapGrid
                .map(row => row.mkString(" "))
                .mkString("\n")
            info(s"new map:\n${newVisualMap}")

            clientRef ! MapVisualization(newVisualMap)

            context become updateMap(newMapGrid)
    }

    def buildNewMapGrid(currentGrid: Array[Array[String]], fromLocation: Location, toLocation: Location) = {
        val newGrid = currentGrid.map(_.clone())
        newGrid(fromLocation.y)(fromLocation.x) = MapTileType.Empty
        newGrid(toLocation.y)(toLocation.x) = MapTileType.Player
        newGrid
    }

    private def createMapTileActors() {
        for (rowIndex <- initialGrid.indices) {
            for (horizontalTileIndex <- initialGrid(rowIndex).indices) {
                val mapTileContent = initialGrid(rowIndex)(horizontalTileIndex)
                val mapTileName = MapActor.childName(horizontalTileIndex, rowIndex)
                val location = new Location(horizontalTileIndex, rowIndex)

                context.actorOf(MapTileActor.props(context.self, location, mapTileContent), name = mapTileName)
            }
        }
    }
}
