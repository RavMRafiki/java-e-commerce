DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'refresh_tokens'
          AND column_name = 'username'
    ) THEN
        UPDATE refresh_tokens
        SET user_id = username
        WHERE username IS NOT NULL
          AND (user_id IS NULL OR user_id = 'legacy-revoked');

        ALTER TABLE refresh_tokens
            DROP COLUMN username;
    END IF;
END $$;