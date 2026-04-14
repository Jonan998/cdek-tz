alter table task
add column user_id uuid not null;

alter table task
add constraint fk_task_user
    foreign key (user_id)
    references users (id)
    on delete cascade;