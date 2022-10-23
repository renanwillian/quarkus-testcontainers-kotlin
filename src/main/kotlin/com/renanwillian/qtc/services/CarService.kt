package com.renanwillian.qtc.services

import com.renanwillian.qtc.domain.Car
import com.renanwillian.qtc.dto.CarRequest
import com.renanwillian.qtc.dto.CarResponse
import com.renanwillian.qtc.repository.CarRepository
import com.renanwillian.qtc.util.toDomain
import com.renanwillian.qtc.util.toResponse
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotFoundException

@ApplicationScoped
@Transactional
class CarService {

    @Inject
    private lateinit var carRepository: CarRepository

    fun list(): List<CarResponse> {
        return carRepository.listAll().map { it.toResponse() }
    }

    fun create(carRequest: CarRequest): CarResponse {
        if (carRepository.countByMakeModelYear(carRequest.make, carRequest.model, carRequest.year) > 0) throw BadRequestException()
        val car = carRequest.toDomain()
        carRepository.persist(car)
        return car.toResponse()
    }

    fun find(id: Long): CarResponse {
        return findById(id).toResponse()
    }

    fun update(id: Long, carRequest: CarRequest): CarResponse {
        val car = findById(id)
        car.make = carRequest.make
        car.model = carRequest.model
        car.year = carRequest.year
        carRepository.persist(car)
        return car.toResponse()
    }

    fun delete(id: Long) {
        carRepository.delete(findById(id))
    }

    private fun findById(id: Long): Car {
        return carRepository.findById(id) ?: throw NotFoundException()
    }
}