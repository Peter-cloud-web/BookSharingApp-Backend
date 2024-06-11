//package com.example.plugins
//
//import io.ktor.server.application.*
//import io.swagger.v3.oas.models.OpenAPI
//
//
//fun Application.module() {
//    install(OpenAPI) {
//        info {
//            version = "1.0.0"
//            title = "My API"
//            description = "This is my API description"
//        }
//        exposeSwaggerUi = true
//        disableGenerationLogging = true
//        disableModelGeneration = true
//    }
//}