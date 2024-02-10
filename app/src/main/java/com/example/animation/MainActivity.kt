package com.example.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.example.animation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        initListeners()
    }

    private fun initListeners() {
        binding.rotateButton.setOnClickListener {
            rotater()
        }

        binding.translateButton.setOnClickListener {
            translater()
        }

        binding.scaleButton.setOnClickListener {
            scaler()
        }

        binding.fadeButton.setOnClickListener {
            fader()
        }

        binding.colorizeButton.setOnClickListener {
            colorizer()
        }

        binding.showerButton.setOnClickListener {
            shower()
        }
    }

    private fun ObjectAnimator.disableButtonDuringAnimation(view: View) {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.isEnabled = true
            }
        })
    }

    private fun rotater() {
        val animator = ObjectAnimator.ofFloat(binding.star, View.ROTATION, -360f, 0f)
        animator.duration = 1000
        animator.disableButtonDuringAnimation(binding.rotateButton)
        animator.start()
    }

    private fun translater() {
        val animator = ObjectAnimator.ofFloat(binding.star, View.TRANSLATION_X, 200f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableButtonDuringAnimation(binding.translateButton)
        animator.start()
    }

    private fun scaler() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)

        val animator = ObjectAnimator.ofPropertyValuesHolder(binding.star, scaleX, scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableButtonDuringAnimation(binding.scaleButton)
        animator.start()
    }

    private fun fader() {
        val animator = ObjectAnimator.ofFloat(binding.star, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableButtonDuringAnimation(binding.fadeButton)
        animator.start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun colorizer() {
        val animator =
            ObjectAnimator.ofArgb(binding.star.parent, "backgroundColor", Color.BLACK, Color.WHITE)
        animator.duration = 500
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableButtonDuringAnimation(binding.colorizeButton)
        animator.start()
    }

    private fun shower() {
        var starW = binding.star.width.toFloat()
        var starH = binding.star.height.toFloat()
        val containerW = binding.frameLayout.width.toFloat()
        val containerH = binding.frameLayout.height.toFloat()
        val newStar = AppCompatImageView(this)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        newStar.scaleX = Math.random().toFloat() * 1.5f + 0.2f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX
        starH *= newStar.scaleY
        newStar.translationX = Math.random().toFloat() * containerW - starW / 2
        val mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y, -starH, containerH + starH)
        mover.interpolator = AccelerateInterpolator(1f)
        val rotation =
            ObjectAnimator.ofFloat(newStar, View.ROTATION, Math.random().toFloat() * 1080)
        rotation.interpolator = LinearInterpolator()
        val animationSet = AnimatorSet()
        animationSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                binding.frameLayout.removeView(newStar)
            }
        })
        animationSet.duration = (Math.random() * 1500 + 500).toLong()
        animationSet.playTogether(rotation, mover)
        animationSet.start()
        binding.frameLayout.addView(newStar)
    }
}