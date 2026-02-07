package com.ens.presentation;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/v1")
public class RestApplication extends Application {
    // JAX-RS will automatically discover all REST endpoints
}
