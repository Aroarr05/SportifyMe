package com.aroa.sportifyme.seguridad.dto; // Cambié el paquete a uno más genérico

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;

    // Método estático para respuestas exitosas
    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(true, message, data);
    }

    // Método estático para respuestas de error
    public static ApiResponse error(String message) {
        return new ApiResponse(false, message, null);
    }
}