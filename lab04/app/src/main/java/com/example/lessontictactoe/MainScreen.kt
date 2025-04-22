package com.example.lessontictactoe

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lessontictactoe.ui.theme.LessonTicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LessonTicTacToeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TicTacToeApp()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    LessonTicTacToeTheme {
        MainScreen(
            boardSize = 3,
            field = List(9) { CellState.EMPTY },
            currentPlayer = Player.CROSS,
            gameState = GameState.IN_PROGRESS,
            player1 = PlayerInfo("Player 1", R.drawable.ic_menu_myplaces, 2),
            player2 = PlayerInfo("Player 2", R.drawable.ic_menu_my_calendar, 1),
            timerSeconds = 10,
            onCellClick = {},
            onResetRound = {},
            onNewGame = {},
            onShowScore = {}
        )
    }
}