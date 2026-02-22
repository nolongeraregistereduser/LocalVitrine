package com.localvitrine.dto;

import com.localvitrine.enums.Goal;
import com.localvitrine.enums.PrimaryCTA;
import com.localvitrine.enums.Sector;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BusinessProfileRequest(
        @NotBlank @Size(max = 200) String businessName,
        @NotBlank @Size(max = 120) String city,
        @NotBlank @Size(max = 255) String address,
        @NotBlank @Size(max = 10000) String description,
        @Size(max = 2000) String detailedDescription,
        @Size(max = 500) String targetAudience,
        @NotBlank @Size(max = 40) String phone,
        @NotBlank @Email @Size(max = 190) String email,
        @Size(max = 255) @Pattern(regexp = "^(https?://)?([\\w.-]+\\.)+[\\w-]{2,}(/.*)?$|^$", message = "Format URL invalide") String website,
        @NotNull Goal goal,
        @NotNull Sector sector,
        PrimaryCTA primaryCTA,
        @Size(max = 255) @Pattern(regexp = "^(https?://)?([\\w.-]+\\.)+[\\w-]{2,}(/.*)?$|^$", message = "Format URL invalide") String facebook,
        @Size(max = 255) @Pattern(regexp = "^(https?://)?([\\w.-]+\\.)+[\\w-]{2,}(/.*)?$|^$", message = "Format URL invalide") String instagram,
        @Size(max = 255) @Pattern(regexp = "^(https?://)?([\\w.-]+\\.)+[\\w-]{2,}(/.*)?$|^$", message = "Format URL invalide") String whatsapp
) {
}
