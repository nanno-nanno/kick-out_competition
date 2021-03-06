package spacerace.client;

import java.io.IOException;
import java.util.function.Supplier;

import spacerace.client.communication.ServerAdapter;
import spacerace.client.communication.SocketServerAdapter;
import spacerace.domain.GameState;
import spacerace.domain.GameStatus;
import spacerace.domain.Statistics;
import spacerace.graphics.GraphicsFactory;
import spacerace.graphics.ViewerGraphics;
import spacerace.server.communication.response.ServerResponse;

public class GameViewer {

    private static final String SERVER_IP = "127.0.0.1"; // If you run locally
    //    private static final String SERVER_IP = "10.46.1.42"; // Game server WIFI
    //    private static final String SERVER_IP = "10.46.1.111"; // Game server ETHERNET
    //    private static final String SERVER_IP = "192.168.1.174"; // Max power computer
    //    private static final String SERVER_IP = "10.46.1.19"; // Other

    private static final int GAME_CYCLE_MIN_TIME = 15;

    private final String         gameName;
    private final ServerAdapter  server;
    private       ViewerGraphics graphics;
    private final Statistics gameCycleStatistics    = new Statistics();
    private final Statistics responseTimeStatistics = new Statistics();

    public static void main(final String[] args) throws IOException, InterruptedException {
        final String gameName = "BattleOfTrustly";

        final GameViewer gameViewer = new GameViewer(gameName);
        gameViewer.start();
    }

    private GameViewer(final String gameName) {
        this.gameName = gameName;
        this.server = new SocketServerAdapter(SERVER_IP, null, gameName);
        //        this.server = new RestServerAdapter(SERVER_IP, null, gameName);
    }

    private void start() throws IOException, InterruptedException {
        runViewerLoop();
    }

    private void runViewerLoop() throws InterruptedException, IOException {
        boolean stop = false;
        while (!stop) {
            final long timeBeforeCycle = System.currentTimeMillis();

            final ServerResponse response  = invokeServerCall(server::getGameStateForViewing, "Exception when getting game state for viewing for game: " + gameName);
            final GameState      gameState = response.getGameState();
            final ViewerGraphics graphics  = getGraphics(gameState, gameName);

            if (gameState != null) {
                graphics.setLevelNumber(response.getLevelNumber());
                if (GameStatus.valueOf(gameState.getGameStatus()) == GameStatus.FINISHED) {
                    final ServerResponse resultListResponse = invokeServerCall(server::getGameResult, "Exception when getting result for game: " + gameName);
                    graphics.setPlayerResults(resultListResponse.getPlayerResults());
                    stop = true;
                }
                graphics.setState(gameState);
            }

            sleepIfGameCycleTooFast(timeBeforeCycle);
        }
    }

    private ViewerGraphics getGraphics(final GameState gameState, final String gameName) throws IOException {
        if (graphics == null) {
            graphics = GraphicsFactory.createViewerGraphics(gameState, gameCycleStatistics, responseTimeStatistics, gameName);
        }
        return graphics;
    }

    private void sleepIfGameCycleTooFast(final long timeBeforeCycle) throws InterruptedException {
        final long executionTime = System.currentTimeMillis() - timeBeforeCycle;
        if (executionTime < GAME_CYCLE_MIN_TIME) {
            Thread.sleep(GAME_CYCLE_MIN_TIME - executionTime);
        }
        final Long cycleTime = System.currentTimeMillis() - timeBeforeCycle;
        gameCycleStatistics.add(cycleTime.intValue());
    }

    private ServerResponse invokeServerCall(final Supplier<ServerResponse> supplier, final String failMessage) {
        final long           beforeTime = System.currentTimeMillis();
        final ServerResponse response   = supplier.get();
        if (response.getErrorMessage() != null) {
            throw new IllegalStateException(failMessage + ". Error message: " + response.getErrorMessage());
        }
        final Long responseTime = System.currentTimeMillis() - beforeTime;
        responseTimeStatistics.add(responseTime.intValue());
        return response;
    }
}
