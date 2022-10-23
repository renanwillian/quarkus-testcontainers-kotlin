package com.renanwillian.qtc.util

import com.renanwillian.qtc.domain.Car
import com.renanwillian.qtc.dto.CarRequest
import com.renanwillian.qtc.dto.CarResponse


fun CarRequest.toDomain(): Car {
    return Car(
        id = null,
        make = make,
        model = model,
        year = year
    )
}

fun Car.toResponse(): CarResponse {
    return CarResponse(
        id = id!!,
        make = make,
        model = model,
        year = year,
    )
}