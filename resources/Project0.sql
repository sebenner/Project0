drop table bankUser;
drop table customerAccounts;
drop table userAccount;

create table bankUser (
    username varchar2(50) primary key,
    passwrd varchar2(50),
    userType varchar2(1),
    fullName varchar2(50),
    address varchar2(50)
);

--many-to-many relationship
create table customerAccounts (
    username varchar2(50),
    accId number(10)
);

drop sequence accIdSeq;

create sequence accIdSeq
    increment by 1
    start with 0
    minvalue 0
    nocycle;

create table userAccount (
    accId number(10) primary key,
    accType varchar(10),
    amount float(10),
    status varchar(50)
);

Insert Into bankUser
values ('payal','pass1', 'b','Payal','701 S. Nedderman Dr., Arlington, Texas 76019');

Insert Into bankUser
values ('matt','pass2', 'e','Mathew Schweigardt','701 S. Nedderman Dr., Arlington, Texas 76019');




create or replace procedure proc1 (username in varchar2, accType in varchar2, username2 in varchar2,
                                    amount in float)
is
accId number(10) := accIdSeq.nextVal;
begin
    insert into customerAccount values (username, accId);
    insert into userAccount values (accId, accType, amount, 'pending');
    if length(username2) != 0
    then
        dbms_output.put_line('reee');
        insert into customerAccounts values (username2, accId);
    end if;
end proc1;

exec proc1('jijiji', 'Savings', '', 30.78);

--select * from bankUser;

--select * from customerAccounts;

--select * from userAccount;

--select * from bankUser where username = 'sebenner' and passwrd = 'pass5';

--select userType from bankUser where username = 'matt';



--commit;