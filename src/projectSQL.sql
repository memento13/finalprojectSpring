create table users(
    id varchar(40) unique primary key,
    email varchar(100) unique not null,
    password varchar(100) not null,
    name varchar(100) not null,
    created_date timestamp(3) not null default now(3),
    modified_date timestamp(3) not null default now(3) on update now(3)
);

create table parties(
    id varchar(40) primary key,
    name varchar(100) unique not null,
    leader_id varchar(40) not null,
    created_date timestamp(3) not null default now(3),
    modified_date timestamp(3) not null default now(3) on update now(3)
    foreign key(leader_id) references users(id) on update cascade on delete cascade,
);
-- FOREIGN key (leader_id) REFERENCES  users (id) on update CASCADE on delete set null
alter table parties add constraint LeaderId foreign key (leader_id) references users (id);
FOREIGN key(leader_id)
    REFERENCES  users(id) on update CASCADE on delete set null


create table party_members(
    party_id varchar(40) not null,
    user_id varchar(40) not null,
    grade int not null,
--     1은 일반회원 50은 파티장
    created_date timestamp(3) not null default now(3),
    modified_date timestamp(3) not null default now(3) on update now(3),
    foreign key(party_id) references parties(id) on update cascade on delete cascade,
    foreign key(user_id) references users(id) on update cascade on delete cascade,
    primary key(party_id,user_id)
);

create table projects(
    id varchar(40) primary key ,
    name varchar (100),
    party_id varchar (40),
    created_date timestamp(3) not null default now(3),
    modified_date timestamp(3) not null default now(3) on update now(3),
    foreign key(party_id) references parties(id) on update cascade on delete cascade
);

create table project_members(
    party_id varchar(40) not null,
    project_id varchar(40) not null,
    user_id varchar(40) not null,
    created_date timestamp(3) not null default now(3),
    modified_date timestamp(3) not null default now(3) on update now(3),
    foreign key(party_id) references parties(id) on update cascade on delete cascade,
    foreign key(project_id) references projects(id) on update cascade on delete cascade,
    foreign key(user_id) references users(id) on update cascade on delete cascade,
    primary key(party_id,project_id,user_id)
);

select * from party_members join parties p on party_members.party_id = p.id where party_members.grade=50 and user_id='5529b5d6-4946-42d9-9187-0d67bf1505b5';
select parties.id, parties.name, parties.leader_id, parties.created_date, parties.modified_date from parties join party_members pm on parties.id = pm.party_id where user_id='5529b5d6-4946-42d9-9187-0d67bf1505b5' and pm.grade=50;
select id,name,leader_id,parties.created_date,parties.modified_date from parties left join (select * from party_members where user_id='6d072069-8715-4249-895e-43ba42eb7fad') user_joined on parties.id = user_joined.party_id where user_id is null;
