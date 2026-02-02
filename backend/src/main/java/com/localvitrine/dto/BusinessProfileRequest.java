package com.localvitrine.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BusinessProfileRequest(
        @NotBlank @Size(max = 200) String businessName,
        @NotBlank @Size(max = 120) String city,
        @NotBlank @Size(max = 10000) String description,
        @NotBlank @Size(max = 40) String phone,
        @NotBlank @Email @Size(max = 190) String email,
        @NotBlank @Size(max = 200) String goal,
        @NotBlank @Size(max = 120) String sector
) {
}
