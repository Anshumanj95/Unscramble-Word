package com.example.scrambleword.gameui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.scrambleword.R
import com.example.scrambleword.databinding.FragmentGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameFragment : Fragment() {
    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentGameBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submit.setOnClickListener { onSubmit() }
        binding.skip.setOnClickListener { onSkip() }
        updateWordOnScreen()

    }
    private fun onSubmit() {
        val playerWord = binding.textInputEditText.text.toString()

        if (viewModel.isUserCorrect(playerWord)) {
            setErrorTextField(false)
            if (viewModel.nextWord()) {
                updateWordOnScreen()
            } else {
                showFinalDialogScore()
            }
        }
        else
            setErrorTextField(true)
    }
    private fun onSkip(){
        if (viewModel.nextWord()) {
            setErrorTextField(false)
            updateWordOnScreen()
        } else {
            showFinalDialogScore()
        }
    }
    private fun updateWordOnScreen(){
        binding.textViewUnscrambledWord.text=viewModel.currentScrambleWord
        binding.score.text=getString(R.string.score,viewModel.score)
        binding.wordCount.text=getString(R.string.word_count,viewModel.currentWordCount, MAX_NO_OF_WORDS)
    }
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }
    override fun onDetach() {
        super.onDetach()
    }
    private fun showFinalDialogScore(){
       MaterialAlertDialogBuilder(requireContext())
           .setTitle(getString(R.string.congratulations))
           .setMessage(getString(R.string.you_scored,viewModel.score))
           .setCancelable(false)
           .setNegativeButton(getString(R.string.exit)) { _, _ ->
               exitGame()
           }
           .setPositiveButton(getString(R.string.play_again)) { _, _ ->
               restartGame()
           }
           .show()
    }
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
        updateWordOnScreen()
    }
    private fun exitGame() {
        activity?.finish()
    }


}