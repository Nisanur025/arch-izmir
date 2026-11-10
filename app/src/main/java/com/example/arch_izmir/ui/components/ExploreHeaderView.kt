
package com.example.arch_izmir.ui.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.PI

import kotlin.math.sin

class ExploreHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val navy = Color.rgb(13, 22, 43)
    private val gold = Color.rgb(217, 170, 67)
    private val cream = Color.rgb(245, 233, 208)
    private val landmark = Color.rgb(52, 68, 103)

    private var animationProgress = 0f
    private var animator: ValueAnimator? = null

    var onProfileClick: (() -> Unit)? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val stars = listOf(
        0.08f to 0.18f,
        0.25f to 0.32f,
        0.43f to 0.13f,
        0.61f to 0.19f,
        0.82f to 0.12f,
        0.94f to 0.30f,
        0.15f to 0.78f,
        0.52f to 0.82f,
        0.72f to 0.70f,
        0.95f to 0.68f
    )

    private val profileRect = RectF()
    private var profilePressed = false

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = cream
        textSize = 25f
        typeface = Typeface.create(
            "serif",
            Typeface.BOLD
        )
    }

    private val subtitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = cream
        alpha = 220
        textSize = 11f
        typeface = Typeface.create(
            "serif",
            Typeface.NORMAL
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()

        // Density dönüşümü
        val density = resources.displayMetrics.density

        canvas.save()

        // Alt köşeleri yuvarlat
        val clipPath = Path().apply {
            addRoundRect(
                0f, 0f, w, h,
                20f * density,
                20f * density,
                Path.Direction.CW
            )
        }

        canvas.clipPath(clipPath)

        drawBackground(canvas, w, h)
        drawStars(canvas, w, h, density)
        drawHorizon(canvas, w, h)
        drawClockTower(canvas, w, h, density)
        drawAncientColumns(canvas, w, h, density)
        drawFlightPathAndPlane(
            canvas,
            w,
            h,
            density,
            animationProgress
        )
        drawTexts(canvas, w, h, density)
        drawProfile(canvas, w, h, density)

        canvas.restore()
    }

    private fun drawBackground(
        canvas: Canvas,
        w: Float,
        h: Float
    ) {
        paint.shader = LinearGradient(
            0f, 0f, 0f, h,
            intArrayOf(
                Color.rgb(16, 27, 51),
                navy,
                Color.rgb(17, 29, 54)
            ),
            null,
            Shader.TileMode.CLAMP
        )

        canvas.drawRect(0f, 0f, w, h, paint)
        paint.shader = null

        // Arka planda atmosfer
        paint.color = Color.argb(30, 65, 90, 135)

        canvas.drawCircle(
            w * 0.98f,
            h * 1.12f,
            w * 0.40f,
            paint
        )
    }

    private fun drawStars(
        canvas: Canvas,
        w: Float,
        h: Float,
        density: Float
    ) {
        stars.forEachIndexed { index, star ->

            val alpha = (
                    120 + 100 * sin(
                        animationProgress * 2 * PI + index
                    )
                    ).toInt().coerceIn(20, 255)

            paint.color = Color.argb(
                alpha,
                248,
                230,
                189
            )

            val radius = if (index % 3 == 0) {
                2.2f
            } else {
                1.3f
            }

            canvas.drawCircle(
                w * star.first,
                h * star.second,
                radius * density,
                paint
            )
        }
    }

    private fun drawHorizon(
        canvas: Canvas,
        w: Float,
        h: Float
    ) {
        val path = Path().apply {
            moveTo(w * 0.40f, h * 1.05f)

            quadTo(
                w * 0.72f,
                h * 0.48f,
                w * 1.10f,
                h * 0.95f
            )
        }

        paint.color = landmark
        paint.alpha = 150
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f

        canvas.drawPath(path, paint)

        paint.style = Paint.Style.FILL
        paint.alpha = 255
    }


    private fun drawClockTower(
        canvas: Canvas,
        w: Float,
        h: Float,
        d: Float
    ) {
        val x = w * 0.76f
        val base = h * 0.91f
        val s = h / 180f

        val stone = Color.rgb(75, 91, 124)
        val lightStone = Color.rgb(112, 126, 157)
        val darkStone = Color.rgb(39, 52, 79)

        fun rect(
            left: Float, top: Float,
            right: Float, bottom: Float,
            color: Int
        ) {
            paint.color = color
            paint.style = Paint.Style.FILL
            canvas.drawRect(left, top, right, bottom, paint)
        }

        fun line(
            x1: Float, y1: Float,
            x2: Float, y2: Float,
            color: Int,
            width: Float = 1f
        ) {
            paint.color = color
            paint.strokeWidth = width * d
            paint.style = Paint.Style.STROKE
            canvas.drawLine(x1, y1, x2, y2, paint)
            paint.style = Paint.Style.FILL
        }

        // Zemindeki gölge
        paint.color = Color.argb(35, 0, 0, 0)
        canvas.drawOval(
            x - 20*s, base - 3*s,
            x + 20*s, base + 3*s,
            paint
        )

        // Alt kaide
        rect(
            x - 17*s, base - 9*s,
            x + 17*s, base,
            darkStone
        )

        rect(
            x - 15*s, base - 13*s,
            x + 15*s, base - 9*s,
            stone
        )

        rect(
            x - 13*s, base - 15*s,
            x + 13*s, base - 13*s,
            lightStone
        )

        // Ana kule gövdesi
        rect(
            x - 7*s, base - 67*s,
            x + 7*s, base - 15*s,
            stone
        )

        // Gövdenin kenar süslemeleri
        line(
            x - 5*s, base - 64*s,
            x - 5*s, base - 17*s,
            lightStone, 1f
        )

        line(
            x + 5*s, base - 64*s,
            x + 5*s, base - 17*s,
            darkStone, 1f
        )

        // Gövde üzerindeki dekoratif katlar
        for (i in 0..3) {
            val y = base - (25 + i * 10) * s

            line(
                x - 7*s, y,
                x + 7*s, y,
                lightStone, 1f
            )
        }

        // Küçük kemerli pencereler
        for (i in -1..1) {
            val windowX = x + i * 4*s

            paint.color = darkStone
            canvas.drawRoundRect(
                windowX - 1.3f*s,
                base - 46*s,
                windowX + 1.3f*s,
                base - 37*s,
                1.5f*s,
                1.5f*s,
                paint
            )

            line(
                windowX - 1.3f*s,
                base - 46*s,
                windowX + 1.3f*s,
                base - 46*s,
                lightStone, 1f
            )
        }

        // Üst balkon kaidesi
        rect(
            x - 12*s, base - 70*s,
            x + 12*s, base - 66*s,
            darkStone
        )

        rect(
            x - 11*s, base - 72*s,
            x + 11*s, base - 70*s,
            lightStone
        )

        // Balkon korkulukları
        for (i in -4..4) {
            val bx = x + i * 2.2f*s

            line(
                bx, base - 78*s,
                bx, base - 72*s,
                lightStone, 1f
            )
        }

        line(
            x - 11*s, base - 78*s,
            x + 11*s, base - 78*s,
            lightStone, 1.5f
        )

        // Saat odası
        rect(
            x - 10*s, base - 93*s,
            x + 10*s, base - 79*s,
            stone
        )

        // Saat odasının üst ve alt kornişleri
        rect(
            x - 12*s, base - 94*s,
            x + 12*s, base - 91*s,
            lightStone
        )

        rect(
            x - 11*s, base - 80*s,
            x + 11*s, base - 78*s,
            lightStone
        )

        // Saat kadranı
        paint.color = darkStone
        canvas.drawCircle(
            x, base - 86*s,
            4.5f*s,
            paint
        )

        paint.color = gold
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 0.9f*d

        canvas.drawCircle(
            x, base - 86*s,
            3.7f*s,
            paint
        )

        // Saat akrebi ve yelkovanı
        line(
            x, base - 86*s,
            x, base - 89*s,
            cream, 0.8f
        )

        line(
            x, base - 86*s,
            x + 2.2f*s, base - 85*s,
            cream, 0.8f
        )

        paint.style = Paint.Style.FILL

        // Üst kubbe
        val dome = Path().apply {
            moveTo(x - 9*s, base - 94*s)
            cubicTo(
                x - 9*s, base - 101*s,
                x - 4*s, base - 105*s,
                x, base - 108*s
            )
            cubicTo(
                x + 4*s, base - 105*s,
                x + 9*s, base - 101*s,
                x + 9*s, base - 94*s
            )
            close()
        }

        paint.color = stone
        canvas.drawPath(dome, paint)

        // Kubbe ışık çizgisi
        line(
            x - 5*s, base - 101*s,
            x, base - 106*s,
            lightStone, 1f
        )

        // Kubbe üstü alem
        line(
            x, base - 108*s,
            x, base - 115*s,
            gold, 1f
        )

        paint.color = gold
        canvas.drawCircle(
            x, base - 115*s,
            1.2f*s,
            paint
        )
    }



    private fun drawAncientColumns(
        canvas: Canvas,
        w: Float,
        h: Float,
        d: Float
    ) {
        val x = w * 0.85f
        val base = h * 0.94f
        val s = h / 180f

        val stone = Color.rgb(67, 82, 114)
        val lightStone = Color.rgb(105, 119, 150)
        val darkStone = Color.rgb(35, 48, 75)

        // Antik yapının zemin kaidesi
        paint.color = darkStone

        canvas.drawRect(
            x - 5*s, base - 5*s,
            x + 43*s, base,
            paint
        )

        paint.color = stone

        canvas.drawRect(
            x - 3*s, base - 8*s,
            x + 41*s, base - 5*s,
            paint
        )

        // Sütunlar
        for (i in 0..4) {
            val cx = x + i * 9*s

            // Sütun gövdesi
            paint.color = stone

            canvas.drawRect(
                cx - 2*s,
                base - 36*s,
                cx + 2*s,
                base - 9*s,
                paint
            )

            // Sütun olukları
            for (j in -1..1) {
                paint.color = if (j == 0) {
                    lightStone
                } else {
                    darkStone
                }

                paint.strokeWidth = 0.8f*d

                canvas.drawLine(
                    cx + j * 1.1f*s,
                    base - 35*s,
                    cx + j * 1.1f*s,
                    base - 10*s,
                    paint
                )
            }

            // Alt kaide
            paint.color = lightStone

            canvas.drawRect(
                cx - 3.5f*s,
                base - 10*s,
                cx + 3.5f*s,
                base - 8*s,
                paint
            )

            // Sütun başlığı
            paint.color = stone

            canvas.drawRect(
                cx - 3*s,
                base - 39*s,
                cx + 3*s,
                base - 36*s,
                paint
            )

            // Başlık üstündeki dekoratif çıkıntılar
            paint.color = lightStone

            canvas.drawRect(
                cx - 4*s,
                base - 41*s,
                cx + 4*s,
                base - 39*s,
                paint
            )

            // Küçük başlık süsleri
            for (j in -1..1) {
                paint.color = lightStone

                canvas.drawCircle(
                    cx + j * 2*s,
                    base - 42*s,
                    1.1f*s,
                    paint
                )
            }
        }

        // Üst mimari kiriş
        paint.color = darkStone

        canvas.drawRect(
            x - 6*s,
            base - 47*s,
            x + 43*s,
            base - 43*s,
            paint
        )

        // Kirişin ışıklı kenarı
        paint.color = lightStone

        canvas.drawRect(
            x - 5*s,
            base - 48*s,
            x + 42*s,
            base - 46*s,
            paint
        )

        // Kırık üçgen alınlık
        val pediment = Path().apply {
            moveTo(x - 5*s, base - 48*s)
            lineTo(x + 9*s, base - 58*s)
            lineTo(x + 23*s, base - 51*s)
            lineTo(x + 34*s, base - 55*s)
            lineTo(x + 42*s, base - 48*s)
            close()
        }

        paint.color = stone
        canvas.drawPath(pediment, paint)

        // Alınlık altın ışık çizgisi
        paint.color = gold
        paint.alpha = 75
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 0.8f*d

        canvas.drawPath(pediment, paint)

        paint.style = Paint.Style.FILL
        paint.alpha = 255
    }




    private fun drawFlightPathAndPlane(
        canvas: Canvas,
        w: Float,
        h: Float,
        d: Float,
        progress: Float
    ) {
        // Uçak rotası
        val route = Path().apply {
            moveTo(w * 0.43f, h * 0.30f)

            cubicTo(
                w * 0.50f, h * 0.47f,
                w * 0.57f, h * 0.48f,
                w * 0.57f, h * 0.30f
            )

            cubicTo(
                w * 0.57f, h * 0.08f,
                w * 0.49f, h * 0.09f,
                w * 0.49f, h * 0.25f
            )

            cubicTo(
                w * 0.49f, h * 0.44f,
                w * 0.60f, h * 0.53f,
                w * 0.69f, h * 0.32f
            )

            cubicTo(
                w * 0.74f, h * 0.23f,
                w * 0.79f, h * 0.15f,
                w * 0.84f, h * 0.14f
            )
        }

        // Rota çizgisi
        paint.color = cream
        paint.alpha = 210
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.5f * d
        paint.strokeCap = Paint.Cap.ROUND
        paint.pathEffect = DashPathEffect(
            floatArrayOf(5f * d, 5f * d),
            0f
        )

        canvas.drawPath(route, paint)

        paint.pathEffect = null
        paint.style = Paint.Style.FILL
        paint.alpha = 255

        // Rotanın gerçek konumunu ve yönünü bul
        val pathMeasure = PathMeasure(route, false)

        val position = FloatArray(2)
        val tangent = FloatArray(2)

        pathMeasure.getPosTan(
            pathMeasure.length * progress.coerceIn(0f, 1f),
            position,
            tangent
        )

        val angle = Math.toDegrees(
            kotlin.math.atan2(
                tangent[1].toDouble(),
                tangent[0].toDouble()
            )
        ).toFloat()

        canvas.save()

        // Uçağın merkezini tam olarak rotanın üzerine taşı
        canvas.translate(position[0], position[1])

        // Uçağı rotanın yönüne çevir
        canvas.rotate(angle)

        paint.color = cream
        paint.style = Paint.Style.FILL

        // Uçak gövdesi: merkez çizgisi y = 0
        val body = Path().apply {
            moveTo(13f * d, 0f)
            lineTo(-9f * d, -2f * d)
            lineTo(-5f * d, 0f)
            lineTo(-9f * d, 2f * d)
            close()
        }

        canvas.drawPath(body, paint)

        // Kanatlar
        val wings = Path().apply {
            moveTo(2f * d, 0f)
            lineTo(-3f * d, -9f * d)
            lineTo(-6f * d, -9f * d)
            lineTo(-4f * d, 0f)
            lineTo(-6f * d, 9f * d)
            lineTo(-3f * d, 9f * d)
            close()
        }

        canvas.drawPath(wings, paint)

        // Kuyruk kanatları
        val tail = Path().apply {
            moveTo(-6f * d, 0f)
            lineTo(-9f * d, -4f * d)
            lineTo(-11f * d, -4f * d)
            lineTo(-9f * d, 0f)
            lineTo(-11f * d, 4f * d)
            lineTo(-9f * d, 4f * d)
            close()
        }

        canvas.drawPath(tail, paint)

        canvas.restore()
    }



    private fun drawTexts(
        canvas: Canvas,
        w: Float,
        h: Float,
        d: Float
    ) {
        textPaint.textSize = 25f * d
        subtitlePaint.textSize = 11f * d

        canvas.drawText(
            "İzmir'i Keşfet",
            20f * d,
            h * 0.42f,
            textPaint
        )

        canvas.drawText(
            "Geçmişin İzinde, Kendi Hikâyeni Yaz.",
            20f * d,
            h * 0.58f,
            subtitlePaint
        )
    }

    private fun drawProfile(
        canvas: Canvas,
        w: Float,
        h: Float,
        d: Float
    ) {
        val x = w - 35f * d
        val y = 35f * d

        profileRect.set(
            x - 21f * d,
            y - 21f * d,
            x + 21f * d,
            y + 21f * d
        )

        // Dış altın çember
        paint.color = gold
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.3f * d

        canvas.drawCircle(x, y, 20f * d, paint)

        paint.style = Paint.Style.FILL

        // Kullanıcı simgesi
        paint.color = Color.WHITE

        canvas.drawCircle(
            x,
            y - 5f * d,
            5f * d,
            paint
        )

        canvas.drawRoundRect(
            x - 9f * d,
            y + 2f * d,
            x + 9f * d,
            y + 12f * d,
            5f * d,
            5f * d,
            paint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                profilePressed = profileRect.contains(
                    event.x,
                    event.y
                )
                return profilePressed
            }

            MotionEvent.ACTION_UP -> {
                if (profilePressed && profileRect.contains(
                        event.x,
                        event.y
                    )
                ) {
                    performClick()
                }

                profilePressed = false
                return true
            }

            MotionEvent.ACTION_CANCEL -> {
                profilePressed = false
                return true
            }
        }

        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        onProfileClick?.invoke()
        return true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 6500
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()

            addUpdateListener {
                animationProgress = it.animatedValue as Float
                invalidate()
            }

            start()
        }
    }

    override fun onDetachedFromWindow() {
        animator?.cancel()
        animator = null
        super.onDetachedFromWindow()
    }
}
