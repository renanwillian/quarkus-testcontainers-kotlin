package com.renanwillian.qtc.repository

import com.renanwillian.qtc.domain.Car
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CarRepository: PanacheRepository<Car> {

    fun countByMakeModelYear(make: String, model: String, year: Int): Long {
        return find("lower(make) = lower(?1) AND lower(model) = lower(?2) AND year = ?3", make, model, year).count()
    }
}