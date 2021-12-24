package ch.epfl.cs107.play.game.icwars;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icwars.actor.units.Soldier;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.Level0;
import ch.epfl.cs107.play.game.icwars.area.Level1;
import ch.epfl.cs107.play.game.icwars.actor.units.Tank;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.RealPlayer;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;
import ch.epfl.cs107.play.io.FileSystem;

import java.util.ArrayList;

public class ICWars extends AreaGame {
    private RealPlayer player;
    private final ArrayList<ICWarsPlayer> waitingToPlayCurrentRound = new ArrayList<>();
    private final ArrayList<ICWarsPlayer> waitingToPlayNextRound = new ArrayList<>();
    private ICWarsPlayer currentPlayer;
    private int areaIndex;
    private GameStates gameStates;
    public final static float CAMERA_SCALE_FACTOR = 20.f; //tuto2 was 13.f
    private final ArrayList<ICWarsPlayer> players = new ArrayList<>();
    private final String[] area = {"icwars/Level0", "icwars/Level1"};

    /**
     * Add all the areas
     */
    private void createAreas() {
        addArea(new Level0());
        addArea(new Level1());
    }

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            initArea(area[areaIndex]);
            return true;
        }
        return false;
    }


    private void initArea(String areaKey) {
        gameStates = GameStates.INIT;

        ICWarsArea area = (ICWarsArea) setCurrentArea(areaKey, true);

        DiscreteCoordinates spawnPositionAlly = area.getPlayerSpawnPosition();

        players.add(new RealPlayer(area, spawnPositionAlly, ICWarsActor.Faction.ALLY, new Tank(area, new DiscreteCoordinates(3, 6),
                ICWarsActor.Faction.ALLY), new Soldier(area, new DiscreteCoordinates(3, 5), ICWarsActor.Faction.ALLY)));

        players.get(0).enterArea(area, spawnPositionAlly);

        DiscreteCoordinates spawnPositionEnemy = area.getSpawnPositionEnemy();

        players.add(new RealPlayer(area, spawnPositionEnemy, ICWarsActor.Faction.ENEMY, new Tank(area, new DiscreteCoordinates(8, 5),
                ICWarsActor.Faction.ENEMY), new Soldier(area, new DiscreteCoordinates(9, 5), ICWarsActor.Faction.ENEMY)));
        players.get(1).enterArea(area, spawnPositionEnemy);

    }

    public void restart() {
        System.out.println("------ Restarted Game ------");
        begin(getWindow(), getFileSystem());
    }


    public void nextLevel() {
        if (areaIndex == 1) end();
        if (areaIndex == 0) {
            players.get(0).leaveArea();
            areaIndex = 1;
            ICWarsArea currentArea = (ICWarsArea) setCurrentArea(area[areaIndex], false);
            players.get(0).enterArea(currentArea, currentArea.getPlayerSpawnPosition());
        }
    }

    @Override
    public String getTitle() {
        return "ICWars";
    }


    @Override
    public void end() {
        System.out.println("------- Game Over -------");
    }

    private enum GameStates {
        INIT,
        CHOOSE_PLAYER,
        START_PLAYER_TURN,
        PLAYER_TURN,
        END_PLAYER_TURN,
        END_TURN,
        END
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getWindow().getKeyboard();
        if (keyboard.get(Keyboard.R).isReleased()) {
            restart();
        }
        if (keyboard.get(Keyboard.N).isReleased()) {
            nextLevel();
        }

        switch (gameStates) {
            case INIT:
                waitingToPlayNextRound.addAll(players);
                gameStates = GameStates.CHOOSE_PLAYER;
                break;
            case CHOOSE_PLAYER:
                if (!waitingToPlayNextRound.isEmpty()) {
                    currentPlayer = waitingToPlayNextRound.get(0);
                    gameStates = GameStates.START_PLAYER_TURN;
                    waitingToPlayNextRound.remove(0);
                } else {
                    gameStates = GameStates.END_TURN;
                }
                break;

            case START_PLAYER_TURN:
                gameStates = GameStates.PLAYER_TURN;
                currentPlayer.startTurn();
                break;

            case PLAYER_TURN:
                if (ICWarsPlayer.States.IDLE == currentPlayer.getCurrentState()) {
                    gameStates = GameStates.END_PLAYER_TURN;
                }
                break;

            case END_PLAYER_TURN:
                if (!currentPlayer.isDefeated()) {
                    waitingToPlayCurrentRound.add(currentPlayer);
                    gameStates = GameStates.CHOOSE_PLAYER;
                    currentPlayer.resetUnits();
                    currentPlayer = null;
                } else {
                    currentPlayer.leaveArea();
                }
                break;

            case END_TURN:
                waitingToPlayCurrentRound.removeIf(ICWarsPlayer::isDefeated);
                /*
                The loop can be replaced with 'Collection.removeIf'
                for (ICWarsPlayer player : nextRoundWaitingPlayers) {
                    if (player.isDefeated()) {
                        nextRoundWaitingPlayers.remove(player);
                    }
                }
                 */

                players.removeIf(ICWarsPlayer::isDefeated);
                /*
                The loop can be replaced with 'Collection.removeIf'
                for (ICWarsPlayer player : players) {
                    if (player.isDefeated()) {
                        players.remove(player);
                    }
                }
                 */

                if (waitingToPlayCurrentRound.size() != 1) {
                    waitingToPlayNextRound.addAll(waitingToPlayCurrentRound);
                    waitingToPlayCurrentRound.clear();
                    gameStates = GameStates.CHOOSE_PLAYER;
                } else {
                    gameStates = GameStates.END;
                }
                break;

            case END:
                //
                break;
        }
        super.update(deltaTime);
    }
}