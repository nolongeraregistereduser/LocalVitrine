-- V1__BusinessProfile_Enum_Migration.sql
-- Migrate BusinessProfile to use enums and add new fields

-- Add new columns (nullable initially for existing records)
ALTER TABLE business_profiles
ADD COLUMN address VARCHAR(255) NULL,
ADD COLUMN website VARCHAR(255) NULL,
ADD COLUMN detailed_description TEXT NULL,
ADD COLUMN target_audience VARCHAR(500) NULL,
ADD COLUMN primary_cta VARCHAR(50) NULL,
ADD COLUMN facebook VARCHAR(255) NULL,
ADD COLUMN instagram VARCHAR(255) NULL,
ADD COLUMN whatsapp VARCHAR(255) NULL;

-- Convert goal column from VARCHAR to ENUM
-- First, create mapping: existing text values to valid Goal enum values
-- This uses CASE to map common variations to valid enum values, defaults to 'CALLS'
UPDATE business_profiles
SET goal = CASE 
    WHEN UPPER(goal) = 'CALLS' THEN 'CALLS'
    WHEN UPPER(goal) = 'BOOKINGS' THEN 'BOOKINGS'
    WHEN UPPER(goal) = 'MESSAGES' THEN 'MESSAGES'
    WHEN UPPER(goal) = 'LEADS' THEN 'LEADS'
    WHEN UPPER(goal) = 'SALES' THEN 'SALES'
    WHEN UPPER(goal) = 'PROMOTION' THEN 'PROMOTION'
    ELSE 'CALLS'  -- Default fallback
END;

-- Convert sector column from VARCHAR to ENUM
-- Map existing text values to valid Sector enum values, defaults to 'OTHER'
UPDATE business_profiles
SET sector = CASE
    WHEN UPPER(sector) = 'RESTAURANT' THEN 'RESTAURANT'
    WHEN UPPER(sector) = 'BEAUTY' THEN 'BEAUTY'
    WHEN UPPER(sector) = 'REAL_ESTATE' THEN 'REAL_ESTATE'
    WHEN UPPER(sector) = 'FITNESS' THEN 'FITNESS'
    WHEN UPPER(sector) = 'HEALTHCARE' THEN 'HEALTHCARE'
    WHEN UPPER(sector) = 'EDUCATION' THEN 'EDUCATION'
    WHEN UPPER(sector) = 'SERVICES' THEN 'SERVICES'
    WHEN UPPER(sector) = 'ECOMMERCE' THEN 'ECOMMERCE'
    WHEN UPPER(sector) = 'TOURISM' THEN 'TOURISM'
    WHEN UPPER(sector) = 'EVENTS' THEN 'EVENTS'
    WHEN UPPER(sector) = 'AUTOMOTIVE' THEN 'AUTOMOTIVE'
    WHEN UPPER(sector) = 'TECHNOLOGY' THEN 'TECHNOLOGY'
    ELSE 'OTHER'  -- Default fallback for unknown values
END;

-- Set default values for address and website for existing records
-- Using a placeholder that users must update
UPDATE business_profiles
SET 
    address = COALESCE(address, ''),
    website = COALESCE(website, '');

-- Note: Modify column types after data migration (if using pure SQL approach)
-- If using Hibernate with spring-boot, the entity mapping will handle type conversion on next deploy
-- For strict SQL migration, uncomment below (requires careful testing):
/*
ALTER TABLE business_profiles
MODIFY address VARCHAR(255) NOT NULL,
MODIFY website VARCHAR(255) NOT NULL;
*/
