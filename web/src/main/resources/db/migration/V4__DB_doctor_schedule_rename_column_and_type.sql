alter table doctor_schedule rename column week_number TO start_week;
alter table doctor_schedule drop constraint doctor_id_week_number_unique;
alter table doctor_schedule add constraint doctor_id_start_week_unique
    unique (doctor_id, start_week);
alter table doctor_schedule alter column start_week set not null;
alter table doctor_schedule alter column start_week type date using
    to_date(concat('2022', '-', start_week), 'IYYY-IW');
