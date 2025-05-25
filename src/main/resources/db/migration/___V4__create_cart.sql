CREATE TABLE cart
(
    id          BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL, -- ссылка на пользователя
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);

CREATE TABLE cart_items
(
    id         BIGSERIAL PRIMARY KEY,
    cart_id    BIGINT NOT NULL,
    tovar_code BIGINT NOT NULL, -- ссылка на товар
    quantity   INT    NOT NULL DEFAULT 1,
    FOREIGN KEY (cart_id) REFERENCES cart (id) ON DELETE CASCADE,
    FOREIGN KEY (tovar_code) REFERENCES tovar (code)
);

-- индексы для ускорения запросов
CREATE INDEX idx_cart_customer_id ON cart(customer_id);
CREATE INDEX idx_cart_item_cart_id ON cart_items(cart_id);
CREATE INDEX idx_cart_item_tovar_code ON cart_items(tovar_code);




