package com.ralabs.gamestatistic.listener.domain

import javax.validation.constraints.Min

data class GameChartRequest(
        @Min(0)
        val limit: Long
)