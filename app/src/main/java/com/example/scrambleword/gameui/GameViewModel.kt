package com.example.scrambleword.gameui

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    private val _score= MutableLiveData(0)
    val score:LiveData<Int>
        get() = _score
    private val _currentWordCount=MutableLiveData(0)
    val currentWordCount:LiveData<Int>
        get() = _currentWordCount
    private val _currentScrambleWord= MutableLiveData<String>()
    val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambleWord) {
        if (it == null) {
            SpannableString("")
        } else {
            val scrambledWord = it.toString()
            val spannable: Spannable = SpannableString(scrambledWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(scrambledWord).build(),
                0,
                scrambledWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }
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
        while (String(tempWord).equals(currentWord,false)) {
            tempWord.shuffle()
        }
        if(wordList.contains(currentWord))
            getNextWord()
        else{
            _currentScrambleWord.value= String(tempWord)
            _currentWordCount.value=(_currentWordCount.value)?.inc()
            wordList.add(currentWord)
        }
    }
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordList.clear()
        getNextWord()
    }
    fun nextWord():Boolean{
        return if(currentWordCount.value!!< MAX_NO_OF_WORDS){
            getNextWord()
            true
        }
        else false
    }

    private fun incrementScore(){
        _score.value= (_score.value)?.plus(SCORE_INCREASE)
    }
    fun isUserCorrect(playerWord:String):Boolean{
        if(playerWord.equals(currentWord,true)) {
            incrementScore()
            return true
        }
        return false
    }

}