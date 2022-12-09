ALTER table user_role_assignment
    DROP CONSTRAINT fk_user,
    DROP COLUMN users_id;
