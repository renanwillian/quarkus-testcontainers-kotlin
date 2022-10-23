package com.renanwillian.qtc.services

import com.renanwillian.qtc.PostgresTestLifecycleManager
import com.renanwillian.qtc.dto.CarRequest
import com.renanwillian.qtc.repository.CarRepository
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotFoundException


@QuarkusTest
@QuarkusTestResource(PostgresTestLifecycleManager::class)
class CarServiceIT {

    @Inject
    lateinit var flyway: Flyway

    @Inject
    lateinit var carService: CarService

    @Inject
    lateinit var carRepository: CarRepository

    @BeforeEach
    fun setUp() {
        flyway.clean()
        flyway.migrate()
    }

    @Test
    fun `when list method is called all cars are returned`() {
        val list = carService.list()

        assertEquals(1, list.size)
        assertEquals(1, list[0].id)
        assertEquals("Mercedes-Benz", list[0].make)
        assertEquals("500 SL", list[0].model)
        assertEquals(1992, list[0].year)
    }

    @Test
    fun `when create method is called with valid data a carResponse is returned`() {
        val carRequest = CarRequest(make = "BMW", model = "1 Series", year = 2013)
        val carResponse = carService.create(carRequest)

        assertEquals(2, carResponse.id)
        assertEquals("BMW", carResponse.make)
        assertEquals("1 Series", carResponse.model)
        assertEquals(2013, carResponse.year)
    }

    @Test
    fun `when create method is called with duplicated data, throws BadRequestException`() {
        val carRequest = CarRequest(make = "Mercedes-Benz", model = "500 SL", year = 1992)
        assertThrowsExactly(BadRequestException::class.java) {
            carService.create(carRequest)
        }
    }

    @Test
    fun `when update method is called with valid data a carResponse is returned`() {
        val carRequest = CarRequest(make = "BMW", model = "1 Series", year = 2013)
        val carResponse = carService.update(1, carRequest)

        assertEquals(1, carResponse.id)
        assertEquals("BMW", carResponse.make)
        assertEquals("1 Series", carResponse.model)
        assertEquals(2013, carResponse.year)
    }

    @Test
    fun `when find method is called with valid id a carResponse is returned`() {
        val carResponse = carService.find(1)

        assertEquals(1, carResponse.id)
        assertEquals("Mercedes-Benz", carResponse.make)
        assertEquals("500 SL", carResponse.model)
        assertEquals(1992, carResponse.year)
    }

    @Test
    fun `when find method is called with invalid id, throws NotFoundException`() {
        assertThrowsExactly(NotFoundException::class.java) {
            carService.find(500)
        }
    }

    @Test
    fun `when delete method is called with valid id a car is deleted`() {
        carService.delete(1)
        assertNull(carRepository.findById(1))
    }

    @Test
    fun `when delete method is called with invalid id, throws NotFoundException`() {
        assertThrowsExactly(NotFoundException::class.java) {
            carService.delete(500)
        }
    }
}