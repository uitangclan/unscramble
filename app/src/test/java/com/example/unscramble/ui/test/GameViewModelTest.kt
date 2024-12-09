package com.example.unscramble.ui.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest {
    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {
        var gameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(gameUiState.currentScrambledWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        gameUiState = viewModel.uiState.value
        val lastWordCount = gameUiState.currentWordCount
        viewModel.skipWord()
        gameUiState = viewModel.uiState.value
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, gameUiState.score)
        assertEquals(lastWordCount + 1, gameUiState.currentWordCount)
    }


    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value

        assertFalse(currentGameUiState.isGuessedWordWrong)

        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        val incorrectPlayerWord = "and"

        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()

        val currentGameUiState = viewModel.uiState.value
        assertEquals(0, currentGameUiState.score)
        assertTrue(currentGameUiState.isGuessedWordWrong)
    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val gameUiState = viewModel.uiState.value
        val unScrambledWord = getUnscrambledWord(gameUiState.currentScrambledWord)

        assertNotEquals(unScrambledWord, gameUiState.currentScrambledWord)

        assertTrue(gameUiState.currentWordCount == 1)

        assertTrue(gameUiState.score == 0)

        assertFalse(gameUiState.isGameOver)
    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var expectedScore = 0
        var gameUiState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(gameUiState.currentScrambledWord)

        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            gameUiState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(gameUiState.currentScrambledWord)
            assertEquals(expectedScore, gameUiState.score)
        }

        assertEquals(MAX_NO_OF_WORDS, gameUiState.currentWordCount)

        assertTrue(gameUiState.isGameOver)
    }

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }
}