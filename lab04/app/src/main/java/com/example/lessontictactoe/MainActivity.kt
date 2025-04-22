package com.example.lessontictactoe

import android.R
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

enum class CellState { EMPTY, CROSS, NOUGHT }
enum class GameState { IN_PROGRESS, CROSS_WIN, NOUGHT_WIN, DRAW }
enum class Player { CROSS, NOUGHT }

data class PlayerInfo(
    val name: String,
    val avatarResId: Int,
    var score: Int = 0
)

@Composable
fun TicTacToeApp() {
    var gameState by remember { mutableStateOf(GameState.IN_PROGRESS) }
    var boardSize by remember { mutableStateOf(3) }
    var isGameStarted by remember { mutableStateOf(false) }
    var player1 by remember { mutableStateOf(PlayerInfo("Player 1", R.drawable.ic_menu_myplaces)) }
    var player2 by remember { mutableStateOf(PlayerInfo("Player 2", R.drawable.ic_menu_my_calendar)) }
    var currentPlayer by remember { mutableStateOf(Player.CROSS) }
    var field by remember { mutableStateOf(List(boardSize * boardSize) { CellState.EMPTY }) }
    var timerSeconds by remember { mutableStateOf(10) }
    var showScoreDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Timer logic
    LaunchedEffect(currentPlayer, gameState, timerSeconds) {
        if (gameState == GameState.IN_PROGRESS && timerSeconds > 0) {
            delay(1000)
            timerSeconds -= 1
            if (timerSeconds == 0) {
                currentPlayer = if (currentPlayer == Player.CROSS) Player.NOUGHT else Player.CROSS
                timerSeconds = 10
            }
        }
    }

    if (!isGameStarted) {
        SetupScreen(
            onStartGame = { size, p1Name, p1Avatar, p2Name, p2Avatar ->
                boardSize = size
                player1 = PlayerInfo(p1Name, p1Avatar)
                player2 = PlayerInfo(p2Name, p2Avatar)
                field = List(size * size) { CellState.EMPTY }
                gameState = GameState.IN_PROGRESS
                currentPlayer = Player.CROSS
                timerSeconds = 10
                isGameStarted = true
            }
        )
    } else {
        MainScreen(
            boardSize = boardSize,
            field = field,
            currentPlayer = currentPlayer,
            gameState = gameState,
            player1 = player1,
            player2 = player2,
            timerSeconds = timerSeconds,
            onCellClick = { index ->
                if (field[index] == CellState.EMPTY && gameState == GameState.IN_PROGRESS) {
                    field = field.toMutableList().apply {
                        this[index] = if (currentPlayer == Player.CROSS) CellState.CROSS else CellState.NOUGHT
                    }
                    gameState = checkGameState(field, boardSize)
                    when (gameState) {
                        GameState.CROSS_WIN -> player1 = player1.copy(score = player1.score + 1)
                        GameState.NOUGHT_WIN -> player2 = player2.copy(score = player2.score + 1)
                        else -> {}
                    }
                    currentPlayer = if (currentPlayer == Player.CROSS) Player.NOUGHT else Player.CROSS
                    timerSeconds = 10
                }
            },
            onResetRound = {
                field = List(boardSize * boardSize) { CellState.EMPTY }
                gameState = GameState.IN_PROGRESS
                currentPlayer = Player.CROSS
                timerSeconds = 10
            },
            onNewGame = {
                isGameStarted = false
                player1 = player1.copy(score = 0)
                player2 = player2.copy(score = 0)
            },
            onShowScore = { showScoreDialog = true }
        )
    }

    if (showScoreDialog) {
        AlertDialog(
            onDismissRequest = { showScoreDialog = false },
            title = { Text("Scoreboard") },
            text = {
                Column {
                    Text("${player1.name}: ${player1.score}")
                    Text("${player2.name}: ${player2.score}")
                }
            },
            confirmButton = {
                Button(onClick = { showScoreDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun SetupScreen(
    onStartGame: (Int, String, Int, String, Int) -> Unit
) {
    var boardSize by remember { mutableStateOf(3) }
    var player1Name by remember { mutableStateOf("Player 1") }
    var player2Name by remember { mutableStateOf("Player 2") }
    var player1Avatar by remember { mutableStateOf(android.R.drawable.ic_menu_myplaces) }
    var player2Avatar by remember { mutableStateOf(android.R.drawable.ic_menu_my_calendar) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tic Tac Toe Setup",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Board size selection
        Text("Select Board Size", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(3, 4, 5).forEach { size ->
                Button(
                    onClick = { boardSize = size },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (boardSize == size) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("${size}x$size")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Player 1 setup
        OutlinedTextField(
            value = player1Name,
            onValueChange = { player1Name = it },
            label = { Text("Player 1 Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Text("Player 1 Avatar", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                android.R.drawable.ic_menu_myplaces,
                android.R.drawable.ic_menu_my_calendar,
                android.R.drawable.ic_menu_mapmode
            ).forEach { avatar ->
                Image(
                    painter = painterResource(avatar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { player1Avatar = avatar }
                        .border(
                            width = 2.dp,
                            color = if (player1Avatar == avatar) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Player 2 setup
        OutlinedTextField(
            value = player2Name,
            onValueChange = { player2Name = it },
            label = { Text("Player 2 Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Text("Player 2 Avatar", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                android.R.drawable.ic_menu_myplaces,
                android.R.drawable.ic_menu_my_calendar,
                android.R.drawable.ic_menu_mapmode
            ).forEach { avatar ->
                Image(
                    painter = painterResource(avatar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { player2Avatar = avatar }
                        .border(
                            width = 2.dp,
                            color = if (player2Avatar == avatar) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onStartGame(boardSize, player1Name, player1Avatar, player2Name, player2Avatar) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Game")
        }
    }
}

@Composable
fun MainScreen(
    boardSize: Int,
    field: List<CellState>,
    currentPlayer: Player,
    gameState: GameState,
    player1: PlayerInfo,
    player2: PlayerInfo,
    timerSeconds: Int,
    onCellClick: (Int) -> Unit,
    onResetRound: () -> Unit,
    onNewGame: () -> Unit,
    onShowScore: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tic Tac Toe",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Player info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PlayerCard(player1, currentPlayer == Player.CROSS)
            PlayerCard(player2, currentPlayer == Player.NOUGHT)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Timer
        LinearProgressIndicator(
            progress = timerSeconds / 10f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        Text(
            text = "Time left: $timerSeconds s",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Game board
        GameBoard(
            boardSize = boardSize,
            field = field,
            gameState = gameState,
            onCellClick = onCellClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Game status
        when (gameState) {
            GameState.CROSS_WIN -> Text("${player1.name} (X) Wins!", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
            GameState.NOUGHT_WIN -> Text("${player2.name} (O) Wins!", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
            GameState.DRAW -> Text("It's a Draw!", style = MaterialTheme.typography.headlineSmall)
            GameState.IN_PROGRESS -> Text(
                text = "${if (currentPlayer == Player.CROSS) player1.name else player2.name}'s Turn (${if (currentPlayer == Player.CROSS) "X" else "O"})",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Control buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onResetRound) {
                Text("Reset Round")
            }
            Button(onClick = onNewGame) {
                Text("New Game")
            }
            Button(onClick = onShowScore) {
                Text("Show Score")
            }
        }
    }
}

@Composable
fun PlayerCard(player: PlayerInfo, isActive: Boolean) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(300)
    )

    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(backgroundColor)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(player.avatarResId),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            Text(player.name, style = MaterialTheme.typography.bodyMedium)
            Text("Score: ${player.score}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun GameBoard(
    boardSize: Int,
    field: List<CellState>,
    gameState: GameState,
    onCellClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(boardSize),
        modifier = Modifier
            .size((boardSize * 80).dp)
            .border(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        items(boardSize * boardSize) { index ->
            val isWinningCell = gameState != GameState.IN_PROGRESS && isPartOfWinningLine(index, field, boardSize)
            val scale by animateFloatAsState(
                targetValue = if (isWinningCell) 1.2f else 1f,
                animationSpec = tween(300)
            )
            val alpha by animateFloatAsState(
                targetValue = if (isWinningCell) 0.7f else 1f,
                animationSpec = tween(300)
            )

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .padding(4.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primary)
                    .clickable { onCellClick(index) }
                    .scale(scale)
                    .alpha(alpha),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (field[index]) {
                        CellState.EMPTY -> ""
                        CellState.CROSS -> "X"
                        CellState.NOUGHT -> "O"
                    },
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 32.sp),
                    color = if (isWinningCell) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

fun checkGameState(field: List<CellState>, boardSize: Int): GameState {
    // Check rows
    for (row in 0 until boardSize) {
        val start = row * boardSize
        if (field[start] != CellState.EMPTY && (0 until boardSize).all { field[start + it] == field[start] }) {
            return when (field[start]) {
                CellState.CROSS -> GameState.CROSS_WIN
                CellState.NOUGHT -> GameState.NOUGHT_WIN
                else -> GameState.IN_PROGRESS
            }
        }
    }

    // Check columns
    for (col in 0 until boardSize) {
        if (field[col] != CellState.EMPTY && (0 until boardSize).all { field[it * boardSize + col] == field[col] }) {
            return when (field[col]) {
                CellState.CROSS -> GameState.CROSS_WIN
                CellState.NOUGHT -> GameState.NOUGHT_WIN
                else -> GameState.IN_PROGRESS
            }
        }
    }

    // Check main diagonal
    if (field[0] != CellState.EMPTY && (0 until boardSize).all { field[it * (boardSize + 1)] == field[0] }) {
        return when (field[0]) {
            CellState.CROSS -> GameState.CROSS_WIN
            CellState.NOUGHT -> GameState.NOUGHT_WIN
            else -> GameState.IN_PROGRESS
        }
    }

    // Check anti-diagonal
    if (field[boardSize - 1] != CellState.EMPTY && (0 until boardSize).all { field[it * (boardSize - 1) + (boardSize - 1)] == field[boardSize - 1] }) {
        return when (field[boardSize - 1]) {
            CellState.CROSS -> GameState.CROSS_WIN
            CellState.NOUGHT -> GameState.NOUGHT_WIN
            else -> GameState.IN_PROGRESS
        }
    }

    return if (field.any { it == CellState.EMPTY }) GameState.IN_PROGRESS else GameState.DRAW
}

fun isPartOfWinningLine(index: Int, field: List<CellState>, boardSize: Int): Boolean {
    val row = index / boardSize
    val col = index % boardSize

    // Check row
    val rowStart = row * boardSize
    if (field[rowStart] != CellState.EMPTY && (0 until boardSize).all { field[rowStart + it] == field[rowStart] }) {
        return index >= rowStart && index < rowStart + boardSize
    }

    // Check column
    if (field[col] != CellState.EMPTY && (0 until boardSize).all { field[it * boardSize + col] == field[col] }) {
        return index % boardSize == col
    }

    // Check main diagonal
    if (index % (boardSize + 1) == 0 && field[0] != CellState.EMPTY && (0 until boardSize).all { field[it * (boardSize + 1)] == field[0] }) {
        return true
    }

    // Check anti-diagonal
    if (index % (boardSize - 1) == 0 && index != 0 && index != (boardSize * boardSize - 1) && field[boardSize - 1] != CellState.EMPTY && (0 until boardSize).all { field[it * (boardSize - 1) + (boardSize - 1)] == field[boardSize - 1] }) {
        return true
    }

    return false
}