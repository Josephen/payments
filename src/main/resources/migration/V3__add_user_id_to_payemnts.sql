ALTER TABLE based.payments
ADD COLUMN user_id UUID;

ALTER TABLE based.payments
ADD CONSTRAINT fk_user_id
FOREIGN KEY (user_id)
REFERENCES based.user(id);
