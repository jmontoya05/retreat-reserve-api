-- V12__seed_initial_data.sql
-- Description: Seeds initial data for development and testing
-- WARNING: This should NOT be run in production!
-- Uses UUID_TO_BIN() to convert string UUIDs to BINARY(16)

-- ============================================================================
-- IMPORTANT: Define UUIDs as variables for consistency
-- ============================================================================
-- These UUIDs are hardcoded for reproducibility in development

SET @admin_id = UUID_TO_BIN('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');
SET @user1_id = UUID_TO_BIN('b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a12');
SET @user2_id = UUID_TO_BIN('c2eebc99-9c0b-4ef8-bb6d-6bb9bd380a13');
SET @user3_id = UUID_TO_BIN('d3eebc99-9c0b-4ef8-bb6d-6bb9bd380a14');

SET @cat_standard_id = UUID_TO_BIN('10000000-0000-0000-0000-000000000001');
SET @cat_premium_id = UUID_TO_BIN('10000000-0000-0000-0000-000000000002');
SET @cat_luxury_id = UUID_TO_BIN('10000000-0000-0000-0000-000000000003');
SET @cat_eco_id = UUID_TO_BIN('10000000-0000-0000-0000-000000000004');
SET @cat_romantic_id = UUID_TO_BIN('10000000-0000-0000-0000-000000000005');

SET @feat_wifi_id = UUID_TO_BIN('20000000-0000-0000-0000-000000000001');
SET @feat_pool_id = UUID_TO_BIN('20000000-0000-0000-0000-000000000002');
SET @feat_parking_id = UUID_TO_BIN('20000000-0000-0000-0000-000000000003');
SET @feat_kitchen_id = UUID_TO_BIN('20000000-0000-0000-0000-000000000004');
SET @feat_ac_id = UUID_TO_BIN('20000000-0000-0000-0000-000000000005');
SET @feat_heating_id = UUID_TO_BIN('20000000-0000-0000-0000-000000000006');
SET @feat_tv_id = UUID_TO_BIN('20000000-0000-0000-0000-000000000007');
SET @feat_jacuzzi_id = UUID_TO_BIN('20000000-0000-0000-0000-000000000008');
SET @feat_bbq_id = UUID_TO_BIN('20000000-0000-0000-0000-000000000009');
SET @feat_washer_id = UUID_TO_BIN('20000000-0000-0000-0000-00000000000a');
SET @feat_pets_id = UUID_TO_BIN('20000000-0000-0000-0000-00000000000b');
SET @feat_playground_id = UUID_TO_BIN('20000000-0000-0000-0000-00000000000c');
SET @feat_gym_id = UUID_TO_BIN('20000000-0000-0000-0000-00000000000d');
SET @feat_lake_view_id = UUID_TO_BIN('20000000-0000-0000-0000-00000000000e');
SET @feat_mountain_view_id = UUID_TO_BIN('20000000-0000-0000-0000-00000000000f');

SET @cabin1_id = UUID_TO_BIN('30000000-0000-0000-0000-000000000001');
SET @cabin2_id = UUID_TO_BIN('30000000-0000-0000-0000-000000000002');
SET @cabin3_id = UUID_TO_BIN('30000000-0000-0000-0000-000000000003');
SET @cabin4_id = UUID_TO_BIN('30000000-0000-0000-0000-000000000004');
SET @cabin5_id = UUID_TO_BIN('30000000-0000-0000-0000-000000000005');

-- ============================================================================
-- 1. CREATE INITIAL ADMIN USER
-- ============================================================================
-- Password: Admin123! (BCrypt hash)
-- You should change this immediately after first login

INSERT INTO users (id, first_name, last_name, email, password_hash, phone_number, role, email_verified, active, created_at)
VALUES (
    @admin_id,
    'System',
    'Administrator',
    'admin@retreatreserve.com',
    '$2a$10$rqkJHxLjGhQqLPvKfUvMYuR.qI.zxl5L0rJYLZQxLmNvP.kF.6rVe', -- Admin123!
    '+57-300-1234567',
    'ADMIN',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP
);

-- ============================================================================
-- 2. CREATE SAMPLE REGULAR USERS
-- ============================================================================
-- Password for all: User123! (BCrypt hash)

INSERT INTO users (id, first_name, last_name, email, password_hash, phone_number, role, email_verified, active, created_at)
VALUES
    (@user1_id, 'Juan', 'Pérez', 'juan.perez@example.com', '$2a$10$rqkJHxLjGhQqLPvKfUvMYuR.qI.zxl5L0rJYLZQxLmNvP.kF.6rVe', '+57-300-1111111', 'USER', TRUE, TRUE, CURRENT_TIMESTAMP),
    (@user2_id, 'María', 'García', 'maria.garcia@example.com', '$2a$10$rqkJHxLjGhQqLPvKfUvMYuR.qI.zxl5L0rJYLZQxLmNvP.kF.6rVe', '+57-300-2222222', 'USER', TRUE, TRUE, CURRENT_TIMESTAMP),
    (@user3_id, 'Carlos', 'Rodríguez', 'carlos.rodriguez@example.com', '$2a$10$rqkJHxLjGhQqLPvKfUvMYuR.qI.zxl5L0rJYLZQxLmNvP.kF.6rVe', '+57-300-3333333', 'USER', TRUE, TRUE, CURRENT_TIMESTAMP);

-- ============================================================================
-- 3. CREATE CATEGORIES
-- ============================================================================

INSERT INTO categories (id, name, description, image_key, active, created_at)
VALUES
    (@cat_standard_id, 'Estándar', 'Cabañas cómodas y asequibles perfectas para escapadas en familia', 'https://images.unsplash.com/photo-1518780664697-55e3ad937233', TRUE, CURRENT_TIMESTAMP),
    (@cat_premium_id, 'Premium', 'Cabañas lujosas con comodidades de alta gama y vistas espectaculares', 'https://images.unsplash.com/photo-1542718610-a1d656d1884c', TRUE, CURRENT_TIMESTAMP),
    (@cat_luxury_id, 'Luxury', 'Retiros exclusivos de lujo con servicios personalizados y privacidad total', 'https://images.unsplash.com/photo-1571896349842-33c89424de2d', TRUE, CURRENT_TIMESTAMP),
    (@cat_eco_id, 'Eco-Friendly', 'Cabañas sostenibles en armonía con la naturaleza', 'https://images.unsplash.com/photo-1501594907352-04cda38ebc29', TRUE, CURRENT_TIMESTAMP),
    (@cat_romantic_id, 'Romántica', 'Refugios íntimos perfectos para parejas', 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4', TRUE, CURRENT_TIMESTAMP);

-- ============================================================================
-- 4. CREATE COMMON FEATURES
-- ============================================================================

INSERT INTO features (id, name, icon_key, description, active, created_at)
VALUES
    (@feat_wifi_id, 'WiFi', 'fa-wifi', 'Internet inalámbrico de alta velocidad', TRUE, CURRENT_TIMESTAMP),
    (@feat_pool_id, 'Piscina', 'fa-swimming-pool', 'Piscina privada o compartida', TRUE, CURRENT_TIMESTAMP),
    (@feat_parking_id, 'Estacionamiento', 'fa-parking', 'Estacionamiento gratuito en las instalaciones', TRUE, CURRENT_TIMESTAMP),
    (@feat_kitchen_id, 'Cocina Completa', 'fa-utensils', 'Cocina equipada con electrodomésticos', TRUE, CURRENT_TIMESTAMP),
    (@feat_ac_id, 'Aire Acondicionado', 'fa-snowflake', 'Aire acondicionado en todas las habitaciones', TRUE, CURRENT_TIMESTAMP),
    (@feat_heating_id, 'Calefacción', 'fa-fire', 'Calefacción central o chimenea', TRUE, CURRENT_TIMESTAMP),
    (@feat_tv_id, 'TV por Cable', 'fa-tv', 'Televisión por cable o streaming', TRUE, CURRENT_TIMESTAMP),
    (@feat_jacuzzi_id, 'Jacuzzi', 'fa-hot-tub', 'Jacuzzi o bañera de hidromasaje', TRUE, CURRENT_TIMESTAMP),
    (@feat_bbq_id, 'Barbacoa', 'fa-fire-alt', 'Parrilla o área de barbacoa', TRUE, CURRENT_TIMESTAMP),
    (@feat_washer_id, 'Lavadora', 'fa-soap', 'Lavadora y secadora disponibles', TRUE, CURRENT_TIMESTAMP),
    (@feat_pets_id, 'Mascotas Permitidas', 'fa-dog', 'Se permiten mascotas con condiciones', TRUE, CURRENT_TIMESTAMP),
    (@feat_playground_id, 'Zona de Juegos', 'fa-child', 'Área de juegos para niños', TRUE, CURRENT_TIMESTAMP),
    (@feat_gym_id, 'Gimnasio', 'fa-dumbbell', 'Gimnasio o equipos de ejercicio', TRUE, CURRENT_TIMESTAMP),
    (@feat_lake_view_id, 'Vista al Lago', 'fa-water', 'Vista panorámica al lago', TRUE, CURRENT_TIMESTAMP),
    (@feat_mountain_view_id, 'Vista a la Montaña', 'fa-mountain', 'Vista panorámica a la montaña', TRUE, CURRENT_TIMESTAMP);

-- ============================================================================
-- 5. CREATE SAMPLE CABINS
-- ============================================================================

-- Cabin 1: Cabaña del Bosque (Premium)
INSERT INTO cabins (id, name, description, category_id, city, state, country, address, latitude, longitude, max_guests, number_of_bedrooms, number_of_bathrooms, price_per_night, status, active, created_at)
VALUES (
    @cabin1_id,
    'Cabaña del Bosque Encantado',
    'Hermosa cabaña ubicada en medio de un bosque de pinos con vistas espectaculares. Perfecta para desconectar de la ciudad y conectar con la naturaleza. Cuenta con chimenea, terraza privada y acceso a senderos naturales.',
    @cat_premium_id,
    'Medellín',
    'Antioquia',
    'Colombia',
    'Vereda El Silencio, Km 15',
    6.2476376,
    -75.5658153,
    6,
    3,
    2,
    350000.00,
    'AVAILABLE',
    TRUE,
    CURRENT_TIMESTAMP
);

-- Cabin 2: Refugio Lakeside (Luxury)
INSERT INTO cabins (id, name, description, category_id, city, state, country, address, latitude, longitude, max_guests, number_of_bedrooms, number_of_bathrooms, price_per_night, status, active, created_at)
VALUES (
    @cabin2_id,
    'Refugio Lakeside Premium',
    'Cabaña de lujo con vista al lago Guatapé. Arquitectura moderna, jacuzzi privado, cocina gourmet y muelle privado. Incluye kayaks y bicicletas para explorar la zona.',
    @cat_luxury_id,
    'Guatapé',
    'Antioquia',
    'Colombia',
    'Vereda La Culebra, Lago Guatapé',
    6.2320747,
    -75.1580684,
    8,
    4,
    3,
    850000.00,
    'AVAILABLE',
    TRUE,
    CURRENT_TIMESTAMP
);

-- Cabin 3: Cabaña Montaña Verde (Eco-Friendly)
INSERT INTO cabins (id, name, description, category_id, city, state, country, address, latitude, longitude, max_guests, number_of_bedrooms, number_of_bathrooms, price_per_night, status, active, created_at)
VALUES (
    @cabin3_id,
    'Montaña Verde Eco-Lodge',
    'Cabaña ecológica construida con materiales sostenibles. Paneles solares, recolección de agua lluvia y huerta orgánica. Rodeada de naturaleza virgen con avistamiento de aves.',
    @cat_eco_id,
    'Jardín',
    'Antioquia',
    'Colombia',
    'Vereda La Linda',
    5.5978156,
    -75.8161445,
    4,
    2,
    1,
    280000.00,
    'AVAILABLE',
    TRUE,
    CURRENT_TIMESTAMP
);

-- Cabin 4: Nido de Amor (Romántica)
INSERT INTO cabins (id, name, description, category_id, city, state, country, address, latitude, longitude, max_guests, number_of_bedrooms, number_of_bathrooms, price_per_night, status, active, created_at)
VALUES (
    @cabin4_id,
    'Nido de Amor en las Nubes',
    'Cabaña romántica perfecta para parejas. Jacuzzi privado con vista a las montañas, cama king size, chimenea y desayuno incluido. Privacidad total garantizada.',
    @cat_romantic_id,
    'Rionegro',
    'Antioquia',
    'Colombia',
    'Vereda San Antonio, Km 8',
    6.1457698,
    -75.3735712,
    2,
    1,
    1,
    420000.00,
    'AVAILABLE',
    TRUE,
    CURRENT_TIMESTAMP
);

-- Cabin 5: Cabaña Valle del Sol (Estándar)
INSERT INTO cabins (id, name, description, category_id, city, state, country, address, latitude, longitude, max_guests, number_of_bedrooms, number_of_bathrooms, price_per_night, status, active, created_at)
VALUES (
    @cabin5_id,
    'Valle del Sol Familiar',
    'Cabaña acogedora ideal para familias. Zona de juegos para niños, amplia terraza y asador. Cerca de restaurantes y atracciones turísticas.',
    @cat_standard_id,
    'Santa Fe de Antioquia',
    'Antioquia',
    'Colombia',
    'Vereda Llano de Bolívar',
    6.5568719,
    -75.8244934,
    5,
    2,
    2,
    220000.00,
    'AVAILABLE',
    TRUE,
    CURRENT_TIMESTAMP
);

-- ============================================================================
-- 6. ADD IMAGES TO CABINS
-- ============================================================================

-- Images for Cabaña del Bosque Encantado (Cabin 1)
INSERT INTO cabin_images (id, cabin_id, image_key, display_order, is_primary, uploaded_at)
VALUES
    (UUID_TO_BIN(UUID()), @cabin1_id, 'https://images.unsplash.com/photo-1542718610-a1d656d1884c', 0, TRUE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin1_id, 'https://images.unsplash.com/photo-1518780664697-55e3ad937233', 1, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin1_id, 'https://images.unsplash.com/photo-1571896349842-33c89424de2d', 2, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin1_id, 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4', 3, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin1_id, 'https://images.unsplash.com/photo-1499696010180-025ef6e1a8f9', 4, FALSE, CURRENT_TIMESTAMP);

-- Images for Refugio Lakeside Premium (Cabin 2)
INSERT INTO cabin_images (id, cabin_id, image_key, display_order, is_primary, uploaded_at)
VALUES
    (UUID_TO_BIN(UUID()), @cabin2_id, 'https://images.unsplash.com/photo-1571896349842-33c89424de2d', 0, TRUE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, 'https://images.unsplash.com/photo-1510798831971-661eb04b3739', 1, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, 'https://images.unsplash.com/photo-1587061949409-02df41d5e562', 2, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, 'https://images.unsplash.com/photo-1605146769289-440113cc3d00', 3, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, 'https://images.unsplash.com/photo-1536890992765-f42a1ee1e2a8', 4, FALSE, CURRENT_TIMESTAMP);

-- Images for Montaña Verde Eco-Lodge (Cabin 3)
INSERT INTO cabin_images (id, cabin_id, image_key, display_order, is_primary, uploaded_at)
VALUES
    (UUID_TO_BIN(UUID()), @cabin3_id, 'https://images.unsplash.com/photo-1501594907352-04cda38ebc29', 0, TRUE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin3_id, 'https://images.unsplash.com/photo-1464146072230-91cabc968266', 1, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin3_id, 'https://images.unsplash.com/photo-1470770841072-f978cf4d019e', 2, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin3_id, 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e', 3, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin3_id, 'https://images.unsplash.com/photo-1472214103451-9374bd1c798e', 4, FALSE, CURRENT_TIMESTAMP);

-- Images for Nido de Amor (Cabin 4)
INSERT INTO cabin_images (id, cabin_id, image_key, display_order, is_primary, uploaded_at)
VALUES
    (UUID_TO_BIN(UUID()), @cabin4_id, 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4', 0, TRUE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin4_id, 'https://images.unsplash.com/photo-1566073771259-6a8506099945', 1, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin4_id, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b', 2, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin4_id, 'https://images.unsplash.com/photo-1540518614846-7eded433c457', 3, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin4_id, 'https://images.unsplash.com/photo-1523755231516-e43fd2e8dca5', 4, FALSE, CURRENT_TIMESTAMP);

-- Images for Valle del Sol Familiar (Cabin 5)
INSERT INTO cabin_images (id, cabin_id, image_key, display_order, is_primary, uploaded_at)
VALUES
    (UUID_TO_BIN(UUID()), @cabin5_id, 'https://images.unsplash.com/photo-1518780664697-55e3ad937233', 0, TRUE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin5_id, 'https://images.unsplash.com/photo-1549638441-b787d2e11f14', 1, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin5_id, 'https://images.unsplash.com/photo-1513694203232-719a280e022f', 2, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin5_id, 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4', 3, FALSE, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin5_id, 'https://images.unsplash.com/photo-1445019980597-93fa8acb246c', 4, FALSE, CURRENT_TIMESTAMP);

-- ============================================================================
-- 7. ASSIGN FEATURES TO CABINS
-- ============================================================================

-- Cabaña del Bosque Encantado features
INSERT INTO cabin_features (id, cabin_id, feature_id, added_at)
VALUES
    (UUID_TO_BIN(UUID()), @cabin1_id, @feat_wifi_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin1_id, @feat_parking_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin1_id, @feat_kitchen_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin1_id, @feat_heating_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin1_id, @feat_tv_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin1_id, @feat_bbq_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin1_id, @feat_mountain_view_id, CURRENT_TIMESTAMP);

-- Refugio Lakeside Premium features
INSERT INTO cabin_features (id, cabin_id, feature_id, added_at)
VALUES
    (UUID_TO_BIN(UUID()), @cabin2_id, @feat_wifi_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, @feat_pool_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, @feat_parking_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, @feat_kitchen_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, @feat_ac_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, @feat_tv_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, @feat_jacuzzi_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, @feat_bbq_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, @feat_washer_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, @feat_gym_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, @feat_lake_view_id, CURRENT_TIMESTAMP);

-- Montaña Verde Eco-Lodge features
INSERT INTO cabin_features (id, cabin_id, feature_id, added_at)
VALUES
    (UUID_TO_BIN(UUID()), @cabin3_id, @feat_wifi_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin3_id, @feat_parking_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin3_id, @feat_kitchen_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin3_id, @feat_bbq_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin3_id, @feat_mountain_view_id, CURRENT_TIMESTAMP);

-- Nido de Amor features
INSERT INTO cabin_features (id, cabin_id, feature_id, added_at)
VALUES
    (UUID_TO_BIN(UUID()), @cabin4_id, @feat_wifi_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin4_id, @feat_parking_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin4_id, @feat_kitchen_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin4_id, @feat_heating_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin4_id, @feat_tv_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin4_id, @feat_jacuzzi_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin4_id, @feat_mountain_view_id, CURRENT_TIMESTAMP);

-- Valle del Sol Familiar features
INSERT INTO cabin_features (id, cabin_id, feature_id, added_at)
VALUES
    (UUID_TO_BIN(UUID()), @cabin5_id, @feat_wifi_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin5_id, @feat_parking_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin5_id, @feat_kitchen_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin5_id, @feat_ac_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin5_id, @feat_tv_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin5_id, @feat_bbq_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin5_id, @feat_pets_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin5_id, @feat_playground_id, CURRENT_TIMESTAMP);

-- ============================================================================
-- 8. ADD POLICIES TO CABINS
-- ============================================================================

-- Store policy IDs for later use in policy items
SET @policy_cabin1_rules = UUID_TO_BIN(UUID());
SET @policy_cabin1_cancel = UUID_TO_BIN(UUID());
SET @policy_cabin1_checkin = UUID_TO_BIN(UUID());

-- Policies for Cabin 1
INSERT INTO policies (id, cabin_id, title, display_order, created_at)
VALUES
    (@policy_cabin1_rules, @cabin1_id, 'Reglas de la Casa', 0, CURRENT_TIMESTAMP),
    (@policy_cabin1_cancel, @cabin1_id, 'Política de Cancelación', 1, CURRENT_TIMESTAMP),
    (@policy_cabin1_checkin, @cabin1_id, 'Información de Check-in/Check-out', 2, CURRENT_TIMESTAMP);

-- Add similar policies for other cabins (abbreviated for space)
INSERT INTO policies (id, cabin_id, title, display_order, created_at)
VALUES
    (UUID_TO_BIN(UUID()), @cabin2_id, 'Reglas de la Casa', 0, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, 'Política de Cancelación', 1, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin2_id, 'Información de Check-in/Check-out', 2, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin3_id, 'Reglas de la Casa', 0, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin3_id, 'Política de Cancelación', 1, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @cabin3_id, 'Información de Check-in/Check-out', 2, CURRENT_TIMESTAMP);

-- ============================================================================
-- 9. ADD POLICY ITEMS
-- ============================================================================

-- Policy items for Cabin 1 - House Rules
INSERT INTO policy_items (id, policy_id, description, display_order, created_at)
VALUES
    (UUID_TO_BIN(UUID()), @policy_cabin1_rules, 'No se permite fumar dentro de la cabaña', 0, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @policy_cabin1_rules, 'Respetar el horario de silencio después de las 10 PM', 1, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @policy_cabin1_rules, 'Máximo 6 huéspedes', 2, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @policy_cabin1_rules, 'No se permiten fiestas o eventos', 3, CURRENT_TIMESTAMP);

-- Cancellation Policy
INSERT INTO policy_items (id, policy_id, description, display_order, created_at)
VALUES
    (UUID_TO_BIN(UUID()), @policy_cabin1_cancel, 'Cancelación gratuita hasta 7 días antes del check-in', 0, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @policy_cabin1_cancel, '50% de reembolso si se cancela entre 3-7 días antes', 1, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @policy_cabin1_cancel, 'No hay reembolso si se cancela con menos de 3 días de anticipación', 2, CURRENT_TIMESTAMP);

-- Check-in/Check-out
INSERT INTO policy_items (id, policy_id, description, display_order, created_at)
VALUES
    (UUID_TO_BIN(UUID()), @policy_cabin1_checkin, 'Check-in: 3:00 PM - 8:00 PM', 0, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @policy_cabin1_checkin, 'Check-out: Antes de las 11:00 AM', 1, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @policy_cabin1_checkin, 'Se requiere identificación válida al momento del check-in', 2, CURRENT_TIMESTAMP);

-- ============================================================================
-- 10. CREATE SAMPLE RESERVATIONS
-- ============================================================================

SET @reservation1_id = UUID_TO_BIN('40000000-0000-0000-0000-000000000001');
SET @reservation2_id = UUID_TO_BIN('40000000-0000-0000-0000-000000000002');

-- Past completed reservation (allows for reviews)
INSERT INTO reservations (id, user_id, cabin_id, check_in_date, check_out_date, number_of_guests, guest_name, guest_phone, total_price, status, active, created_at)
VALUES
    (@reservation1_id, @user1_id, @cabin1_id, DATE_SUB(CURDATE(), INTERVAL 30 DAY), DATE_SUB(CURDATE(), INTERVAL 27 DAY), 4, 'Juan Pérez', '+57-300-1111111', 1050000.00, 'COMPLETED', TRUE, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY));

-- Upcoming reservation
INSERT INTO reservations (id, user_id, cabin_id, check_in_date, check_out_date, number_of_guests, guest_name, guest_phone, total_price, status, active, created_at)
VALUES
    (@reservation2_id, @user2_id, @cabin2_id, DATE_ADD(CURDATE(), INTERVAL 15 DAY), DATE_ADD(CURDATE(), INTERVAL 17 DAY), 2, 'María García', '+57-300-2222222', 1700000.00, 'CONFIRMED', TRUE, CURRENT_TIMESTAMP);

-- ============================================================================
-- 11. CREATE SAMPLE REVIEWS
-- ============================================================================

INSERT INTO reviews (id, user_id, cabin_id, reservation_id, rating, comment, active, created_at)
VALUES
    (UUID_TO_BIN(UUID()), @user1_id, @cabin1_id, @reservation1_id, 5, '¡Increíble experiencia! La cabaña superó nuestras expectativas. El lugar es hermoso, muy limpio y la atención del anfitrión fue excelente. Sin duda volveremos.', TRUE, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));

-- Update cabin rating based on review
UPDATE cabins
SET average_rating = 5.00,
    total_reviews = 1,
    updated_at = CURRENT_TIMESTAMP
WHERE id = @cabin1_id;

-- ============================================================================
-- 12. CREATE SAMPLE FAVORITES
-- ============================================================================

INSERT INTO favorites (id, user_id, cabin_id, added_at)
VALUES
    (UUID_TO_BIN(UUID()), @user1_id, @cabin2_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @user1_id, @cabin4_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @user2_id, @cabin1_id, CURRENT_TIMESTAMP),
    (UUID_TO_BIN(UUID()), @user2_id, @cabin3_id, CURRENT_TIMESTAMP);

-- ============================================================================
-- SEED DATA COMPLETE
-- ============================================================================