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
-- alter table parties add constraint LeaderId foreign key (leader_id) references users (id);
-- FOREIGN key(leader_id)
--     REFERENCES  users(id) on update CASCADE on delete set null


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

create table posts(
    id varchar (40) primary key ,
    title varchar (100),
    content text,
    party_id varchar(40) not null,
    project_id varchar(40) not null,
    user_id varchar(40) not null,
    created_date timestamp(3) not null default now(3),
    modified_date timestamp(3) not null default now(3) on update now(3),
    foreign key(party_id) references parties(id) on update cascade on delete cascade,
    foreign key(project_id) references projects(id) on update cascade on delete cascade,
    foreign key(user_id) references users(id) on update cascade on delete cascade
);

create table likes(
    post_id varchar(40) not null,
    user_id varchar(40) not null,
    created_date timestamp(3) not null default now(3),
    modified_date timestamp(3) not null default now(3) on update now(3),
    foreign key(post_id) references posts(id) on update cascade on delete cascade,
    foreign key(user_id) references users(id) on update cascade on delete cascade,
    primary key(post_id,user_id)
);

-- select posts.id, posts.title, posts.content, posts.party_id, posts.project_id, posts.user_id, posts.created_date, posts.modified_date,users.name from posts left join users on posts.user_id = users.id where project_id=? order by created_date desc;
-- insert into posts values (?,?,?,?,?,?,default ,default );
--
-- select * from party_members join parties p on party_members.party_id = p.id where party_members.grade=50 and user_id='5529b5d6-4946-42d9-9187-0d67bf1505b5';
-- select parties.id, parties.name, parties.leader_id, parties.created_date, parties.modified_date from parties join party_members pm on parties.id = pm.party_id where user_id='5529b5d6-4946-42d9-9187-0d67bf1505b5' and pm.grade=50;
-- select id,name,leader_id,parties.created_date,parties.modified_date from parties left join (select * from party_members where user_id='6d072069-8715-4249-895e-43ba42eb7fad') user_joined on parties.id = user_joined.party_id where user_id is null;
--
-- select projects_sub.id, projects_sub.name, projects_sub.party_id, projects_sub.created_date, projects_sub.modified_date,  project_members_sub.user_id from (select * from projects where party_id=?) projects_sub left join (select * from project_members where party_id=? and user_id=?) project_members_sub on projects_sub.id = project_members_sub.project_id;
--
-- select * from project_members where project_id = ? and user_id = ?;
-- select * from likes where post_id = ? and user_id =?;
SELECT COLUMN_NAME, CHARACTER_SET_NAME
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'pukyung16'
AND TABLE_NAME = 'projects';
