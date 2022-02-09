create table course (id int8 not null, name varchar(255), primary key (id));
create table course_participate (id int8 not null, course_id int8 not null);
create table course_teaches (id int8 not null, course_id int8 not null);
alter table if exists course_participate add constraint FK_COURSE_TO_COURSE_PARTICIPATE foreign key (course_id) references course;
alter table if exists course_participate add constraint FK_STUDENT_TO_COURSE_PARTICIPATE foreign key (id) references student;
alter table if exists course_teaches add constraint FK_COURSE_TO_COURSE_TEACHES foreign key (course_id) references course;
alter table if exists course_teaches add constraint FK_TEACHER_TO_COURSE_TEACHES foreign key (id) references teacher;
