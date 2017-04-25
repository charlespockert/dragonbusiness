delete from company;
delete from employee;
delete from transaction;

insert into company (id, name, bankrupt, shares_issued, value) values (1, 'test company', 0, 1000, 10000);
insert into employee (uuid, company_id, name, rank, employment_start) values (0, 1, 'Employee 1', 3, getdate());
insert into transaction (uuid, company_id, date, amount, type) values (0, 1, getdate(), 100, 0);
insert into transaction (uuid, company_id, date, amount, type) values (0, 1, getdate(), 2000, 1);
insert into transaction (uuid, company_id, date, amount, type) values (0, 1, getdate(), 340, 1);
insert into transaction (uuid, company_id, date, amount, type) values (0, 1, getdate(), 2500, 1);
insert into transaction (uuid, company_id, date, amount, type) values (0, 1, getdate(), 2500, 3);
insert into transaction (uuid, company_id, date, amount, type) values (0, 1, getdate(), 2500, 3);
insert into transaction (uuid, company_id, date, amount, type) values (0, 1, getdate(), 2500, 3);
insert into transaction (uuid, company_id, date, amount, type) values (0, 1, getdate(), 2500, 3);

select id, name, bonuses, dividends, (turnover - overheads) / value * 100 as growth, overheads,  turnover - overheads as profit, salary, turnover, value + (turnover - overheads) as value  
from (
    select 
        company.id,
	company.name,
        company.value,
        sum(CASE WHEN type in (0) THEN amount ELSE 0 END) as salary,
        sum(CASE WHEN type in (1) THEN amount ELSE 0 END) as bonuses,
        sum(CASE WHEN type in (2) THEN amount ELSE 0 END) as dividends,
        sum(CASE WHEN type in (3) THEN amount ELSE 0 END) as turnover,
	sum(CASE WHEN type in (0,1,2) THEN amount ELSE 0 END) as overheads
    from company inner join transaction on transaction.company_id = company.id
)