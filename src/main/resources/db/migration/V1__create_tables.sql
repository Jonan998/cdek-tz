create table "Task" (
    "id" uuid primary key,
    "Title" varchar(255) not null,
    "Description" text not null,
    "Status" varchar(50) not null,
    constraint "chk_task_status"
        check ("Status" in ('NEW', 'IN_PROGRESS', 'DONE'))
);

create table "TimeRecord" (
    "id" uuid primary key,
    "EmployeeId" bigint not null,
    "taskId" uuid not null,
    "StartTime" timestamp not null,
    "EndTime" timestamp not null,
    "Description" text not null,
    constraint "fk_time_record_task"
        foreign key ("taskId")
        references "Task" ("id")
        on delete cascade
);