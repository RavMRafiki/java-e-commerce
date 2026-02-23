ALTER TABLE IF EXISTS refresh_tokens
    ADD COLUMN IF NOT EXISTS user_id VARCHAR(128);

UPDATE refresh_tokens
SET user_id = 'legacy-revoked',
    revoked = TRUE,
    revoked_at = COALESCE(revoked_at, NOW())
WHERE user_id IS NULL;

ALTER TABLE IF EXISTS refresh_tokens
    ALTER COLUMN user_id SET NOT NULL;