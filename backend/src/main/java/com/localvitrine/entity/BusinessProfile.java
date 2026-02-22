package com.localvitrine.entity;

import com.localvitrine.enums.Goal;
import com.localvitrine.enums.PrimaryCTA;
import com.localvitrine.enums.Sector;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "business_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false, unique = true)
    private Project project;

    @Column(nullable = false, length = 200)
    private String businessName;

    @Column(nullable = false, length = 120)
    private String city;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 40)
    private String phone;

    @Column(nullable = false, length = 190)
    private String email;

    @Column(length = 255)
    private String website;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String detailedDescription;

    @Column(length = 500)
    private String targetAudience;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Goal goal;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sector sector;

    @Column
    @Enumerated(EnumType.STRING)
    private PrimaryCTA primaryCTA;

    @Column(length = 255)
    private String facebook;

    @Column(length = 255)
    private String instagram;

    @Column(length = 255)
    private String whatsapp;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
