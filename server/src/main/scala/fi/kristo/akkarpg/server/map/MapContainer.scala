package fi.kristo.akkarpg.server.map

import org.springframework.stereotype.Component

import scala.io.Source

@Component
class MapContainer() {
    val grid = Source
        .fromInputStream(getClass.getResourceAsStream("/onlinerpg.map"))
        .getLines()
        .toArray
        .map(row => row.split(" "))

    def rowCount(): Range = {
        grid.indices
    }

    def horizontalTileCountOf(verticalTileIndex: Int): Range = {
        grid(verticalTileIndex).indices
    }

    def content(rowIndex: Int, horizontalTileIndex: Int): String = {
        grid(rowIndex)(horizontalTileIndex)
    }
}
