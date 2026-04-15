DROP TABLE IF EXISTS time_record;

CREATE TABLE time_record (
    id UUID PRIMARY KEY,
    employee_id UUID NOT NULL,
    task_id UUID NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    description VARCHAR(255) NOT NULL,
    CONSTRAINT fk_time_record_task
        FOREIGN KEY (task_id) REFERENCES task(id)
);