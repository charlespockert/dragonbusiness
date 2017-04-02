insert into company (NAME, BANKRUPT, SHARES_ISSUED, VALUE)
values ('Test company', 0, 1000, 1000);

insert into employee (uuid, company_id, name, rank, employment_start)
values (1, 1, 'CEO', 3, getdate());

insert into employee (uuid, company_id, name, rank, employment_start)
values (2, 1, 'Employee', 0, getdate());

insert into employee (uuid, company_id, name, rank, employment_start)
values (3, 1, 'Employee 2', 0, getdate());

insert into employee (uuid, company_id, name, rank, employment_start)
values (4, 1, 'Manager', 2, getdate());
