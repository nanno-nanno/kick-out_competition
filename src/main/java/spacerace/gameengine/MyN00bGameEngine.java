package spacerace.gameengine;

import java.io.IOException;
import java.util.List;

import spacerace.client.RemoteGame;
import spacerace.client.communication.LocalServerAdapter;
import spacerace.domain.Action;
import spacerace.domain.ShipState;
import spacerace.domain.Vector2D;

public class MyN00bGameEngine implements SpaceRaceGameEngine {

    @Override
    public Action getAction(final ShipState shipState, final List<Vector2D> otherShipPositions, final List<Vector2D> otherShipMissilePositions) {
        // Create a GUI interface using visual basic to track the killers IP address (youtube.com/watch?v=hkDD03yeLnU)
        return null;
    }

    public static void main(final String[] args) throws IOException, InterruptedException {
        final String              playerName   = "Robocop";
        final String              gameName     = "Battle of Trustly";
        final int                 levelNumber  = 2;
        final LocalServerAdapter  server       = new LocalServerAdapter(playerName, gameName, levelNumber);
        final RemoteGame          remoteGame   = new RemoteGame(server, playerName, gameName, levelNumber);
        final SpaceRaceGameEngine myGameEngine = new MyN00bGameEngine();
        remoteGame.runGame(myGameEngine);
    }
}
