package com.renanwillian.qtc

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.PostgreSQLContainer

class PostgresTestLifecycleManager: QuarkusTestResourceLifecycleManager {

    companion object {
        val db: PostgreSQLContainer<Nothing> = PostgreSQLContainer("postgres:12.2")
    }

    override fun start(): MutableMap<String, String> {
        db.start()
        val properties = hashMapOf<String, String>()
        properties["quarkus.datasource.jdbc.url"] = db.jdbcUrl
        properties["quarkus.datasource.username"] = db.username
        properties["quarkus.datasource.password"] = db.password
        return properties
    }

    override fun stop() {
        if (db.isRunning) {
            db.stop()
        }
    }
}