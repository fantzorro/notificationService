create table IF NOT EXISTS NOTIFICATIONS (
    EXTERNAL_ID varchar(100) UNIQUE PRIMARY KEY,
    MESSAGE varchar(1000) not null,
    TIME timestamp not null,
    NOTIFICATION_TYPE varchar(100) not null,
    EXTRA_PARAMS varchar(1000) not null
);