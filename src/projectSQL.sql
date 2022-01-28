create table users(
    id varchar(40) unique primary key,
    email varchar(100) unique not null,
    password varchar(100) not null,
    name varchar(100) not null,
    created_date timestamp(3) not null default now(3),
    modified_date timestamp(3) not null default now(3) on update now(3)
);

create table partys(
    id varchar(40) primary key,
    name varchar(100) unique not null,
    leader_id varchar(40) not null,
    created_date timestamp(3) not null default now(3),
    modified_date timestamp(3) not null default now(3) on update now(3)
);
-- FOREIGN key (leader_id) REFERENCES  users (id) on update CASCADE on delete set null
alter table partys add constraint LeaderId foreign key (leader_id) references users (id);
FOREIGN key(leader_id)
    REFERENCES  users(id) on update CASCADE on delete set null


create table party_members(
    party_id varchar(40) not null,
    user_id varchar(40) not null,
    grade int not null,
--     1은 일반회원 50은 파티장
    created_date timestamp(3) not null default now(3),
    modified_date timestamp(3) not null default now(3) on update now(3),
    foreign key(party_id) references partys(id) on update cascade on delete cascade,
    foreign key(user_id) references users(id) on update cascade on delete cascade,
    primary key(party_id,user_id)
);
