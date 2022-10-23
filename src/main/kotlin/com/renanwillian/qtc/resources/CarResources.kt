package com.renanwillian.qtc.resources

import com.renanwillian.qtc.dto.CarRequest
import com.renanwillian.qtc.services.CarService
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class CarResources {

    @Inject
    private lateinit var carService: CarService

    @GET
    fun list(): Response {
        return Response.ok(carService.list()).build()
    }

    @POST
    fun create(carRequest: CarRequest): Response {
        return Response.status(Status.CREATED.statusCode)
                       .entity(carService.create(carRequest))
                       .build()
    }

    @GET
    @Path("/{id}")
    fun find(@PathParam("id") id: Long): Response {
        return Response.ok(carService.find(id)).build()
    }

    @PUT
    @Path("/{id}")
    fun update(@PathParam("id") id: Long, carRequest: CarRequest): Response {
        return Response.ok(carService.update(id, carRequest)).build()
    }

    @DELETE
    @Path("/{id}")
    fun delete(@PathParam("id") id: Long): Response {
        carService.delete(id)
        return Response.noContent().build()
    }
}