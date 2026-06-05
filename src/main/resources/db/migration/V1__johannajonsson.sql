CREATE TABLE inventory (
    inventory_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL,
    in_stock INTEGER NOT NULL,
    reserved INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    version BIGINT
);

CREATE TABLE reservation (
    reservation_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID,
    reserved_at TIMESTAMP WITH TIME ZONE,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT
);

CREATE TABLE reservation_item (
    reservation_item_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID,
    quantity INTEGER,
    reservation_id UUID,
    CONSTRAINT fk_reservation_item_reservation 
        FOREIGN KEY (reservation_id) 
        REFERENCES reservation (reservation_id)
);