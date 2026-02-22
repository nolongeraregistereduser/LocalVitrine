-- Portable migration for MySQL profile

ALTER TABLE business_profiles ADD COLUMN address VARCHAR(255) NULL;
ALTER TABLE business_profiles ADD COLUMN website VARCHAR(255) NULL;
ALTER TABLE business_profiles ADD COLUMN detailed_description TEXT NULL;
ALTER TABLE business_profiles ADD COLUMN target_audience VARCHAR(500) NULL;
ALTER TABLE business_profiles ADD COLUMN primary_cta VARCHAR(50) NULL;
ALTER TABLE business_profiles ADD COLUMN facebook VARCHAR(255) NULL;
ALTER TABLE business_profiles ADD COLUMN instagram VARCHAR(255) NULL;
ALTER TABLE business_profiles ADD COLUMN whatsapp VARCHAR(255) NULL;

UPDATE business_profiles
SET goal = CASE
    WHEN UPPER(goal) = 'CALLS' THEN 'CALLS'
    WHEN UPPER(goal) = 'BOOKINGS' THEN 'BOOKINGS'
    WHEN UPPER(goal) = 'MESSAGES' THEN 'MESSAGES'
    WHEN UPPER(goal) = 'LEADS' THEN 'LEADS'
    WHEN UPPER(goal) = 'SALES' THEN 'SALES'
    WHEN UPPER(goal) = 'PROMOTION' THEN 'PROMOTION'
    ELSE 'CALLS'
END;

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
    ELSE 'OTHER'
END;

UPDATE business_profiles
SET address = COALESCE(address, ''),
    website = COALESCE(website, '');
