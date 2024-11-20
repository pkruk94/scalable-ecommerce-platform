package com.pkruk.ecommerce.advice;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {
}
