create table task (
    id uuid primary key,
    title varchar(255) not null,
    description text not null,
    status varchar(50) not null,
    constraint chk_task_status
        check (status in ('NEW', 'IN_PROGRESS', 'DONE'))
);

create table time_record (
    id uuid primary key,
    employee_id bigint not null,
    task_id uuid not null,
    start_time timestamp not null,
    end_time timestamp not null,
    description text not null,
    constraint fk_time_record_task
        foreign key (task_id)
        references task (id)
        on delete cascade
);

create table users (
    id uuid primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    role varchar(50) not null
);