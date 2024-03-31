CREATE TABLE IF NOT EXISTS based.payment (
  id UUID PRIMARY KEY,
  amount NUMERIC(19, 4) NOT NULL,
  description TEXT NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
