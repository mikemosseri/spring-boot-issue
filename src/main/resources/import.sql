

insert into user (id, account_expired, account_locked, credentials_expired, username, account_enabled, password) VALUES (1, 0, 0, 0, 'test', 1, 'test');
insert into role (id, name) VALUES (1, 'ADMIN');
insert into user_roles (user_id, roles_id) VALUES (1, 1);
