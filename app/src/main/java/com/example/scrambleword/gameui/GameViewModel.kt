package com.example.scrambleword.gameui

import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    private var _score=0
    val score:Int
        get() = _score
    private var _currentWordCount=0
    val currentWordCount:Int
        get() = _currentWordCount
    private lateinit var _currentScrambleWord:String
    val currentScrambleWord:String
        get() = _currentScrambleWord
    private var wordList:MutableList<String> = mutableListOf()
    private lateinit var currentWord:String
    init {
        getNextWord()
    }
    override fun onCleared() {
        super.onCleared()
    }
    private fun getNextWord(){
        currentWord= listOfWords.random()
        val tempWord=currentWord.toCharArray()
        tempWord.shuffle()
        while (tempWord.toString().equals(currentWord,false)) {
            tempWord.shuffle()
        }
        if(wordList.contains(currentWord))
            getNextWord()
        else{
            _currentScrambleWord= String(tempWord)
            ++_currentWordCount
            wordList.add(currentWord)
        }
    }
    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
        wordList.clear()
        getNextWord()
    }
    fun nextWord():Boolean{
        return if(currentWordCount< MAX_NO_OF_WORDS){
            getNextWord()
            true
        }
        else false
    }

    private fun incrementScore(){
        _score+= SCORE_INCREASE
    }
    fun isUserCorrect(playerWord:String):Boolean{
        if(playerWord.equals(currentWord,false)) {
            incrementScore()
            return true
        }
        return false
    }

}