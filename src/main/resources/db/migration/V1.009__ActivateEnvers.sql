create table revinfo (rev int4 not null, revtstmp int8, primary key (rev));
create table course_aud (id int8 not null, rev int4 not null, revtype int2, name varchar(255), primary key (id, rev));
create table course_participate_aud (rev int4 not null, id int8 not null, course_id int8 not null, revtype int2, primary key (rev, id, course_id));
create table course_teaches_aud (rev int4 not null, id int8 not null, course_id int8 not null, revtype int2, primary key (rev, id, course_id));
create table student_aud (id int8 not null, rev int4 not null, revtype int2, birth_date date, central_id int4, name varchar(255), semester int4, used_free_semesters int4, primary key (id, rev));
create table teacher_aud (id int8 not null, rev int4 not null, revtype int2, birth_date date, name varchar(255), primary key (id, rev));

alter table if exists course_aud add constraint FK_REVINFO_TO_COURSE_AUD foreign key (rev) references revinfo;
alter table if exists course_participate_aud add constraint FK_REVINFO_TO_COURSE_PARTICIPATE_AUD foreign key (rev) references revinfo;
alter table if exists course_teaches_aud add constraint FK_REVINFO_TO_COURSE_TEACHES_AUD foreign key (rev) references revinfo;
alter table if exists student_aud add constraint FK_REVINFO_TO_STUDENT_AUD foreign key (rev) references revinfo;
alter table if exists teacher_aud add constraint FK_REVINFO_TO_TEACHER_AUD foreign key (rev) references revinfo;
