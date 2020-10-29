package com.kuznetsova.game


class KalmanFilter(
    private val f: Double,
    private val q: Double,
    private val h: Double,
    private val r: Double
) {

    private var x0: Double = 0.0
    private var p0: Double = 0.0
    var state: Double = 0.0
    private var covariance: Double = 0.0


    fun setState(state: Double, covariance: Double) {
        this.state = state
        this.covariance = covariance
    }

    fun correct(data: Double) {
        //time update - prediction
        x0 = f * state
        p0 = f * covariance * f + q

        //measurement update - correction
        val k = h * p0 / (h * p0 * h + r)
        state = x0 + k * (data - h * x0)
        covariance = (1 - k * h) * p0
    }

}