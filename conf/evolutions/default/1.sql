# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table massign_unit (
  zid                       numeric(19) identity(1,1) not null,
  assign_prog               varchar(255),
  assign_dept               varchar(255),
  assign_unit               varchar(255),
  assign_lec                varchar(255),
  assign_lec_email          varchar(255),
  assign_lec_name           varchar(255),
  is_active                 varchar(255),
  constraint pk_massign_unit primary key (zid))
;

create table mdept (
  bid                       numeric(19) identity(1,1) not null,
  dept_name                 varchar(255),
  dept_head                 varchar(255),
  dept_faculty              varchar(255),
  is_active                 varchar(255),
  constraint pk_mdept primary key (bid))
;

create table mfaculty (
  did                       numeric(19) identity(1,1) not null,
  fname                     varchar(255),
  fint                      varchar(255),
  fmajor                    varchar(255),
  is_active                 varchar(255),
  constraint pk_mfaculty primary key (did))
;

create table mlecs (
  cid                       numeric(19) identity(1,1) not null,
  lec_name                  varchar(255),
  id_no                     varchar(255),
  lec_dept                  varchar(255),
  lec_email                 varchar(255),
  lec_password              varchar(255),
  is_active                 varchar(255),
  constraint pk_mlecs primary key (cid))
;

create table mprogs (
  eid                       numeric(19) identity(1,1) not null,
  prog_name                 varchar(255),
  prog_dept                 varchar(255),
  prog_level                varchar(255),
  is_active                 varchar(255),
  constraint pk_mprogs primary key (eid))
;

create table mstud (
  gid                       numeric(19) identity(1,1) not null,
  stud_name                 varchar(255),
  reg_no                    varchar(255),
  prog                      varchar(255),
  year                      varchar(255),
  sem                       varchar(255),
  is_active                 varchar(255),
  constraint pk_mstud primary key (gid))
;

create table mstudent_attendance (
  yid                       numeric(19) identity(1,1) not null,
  student_name              varchar(255),
  reg_number                varchar(255),
  student_prog              varchar(255),
  unit                      varchar(255),
  unit_prog                 varchar(255),
  lecturer                  varchar(255),
  attendance                varchar(255),
  is_uploaded               varchar(255),
  is_active                 varchar(255),
  constraint pk_mstudent_attendance primary key (yid))
;

create table munits (
  fid                       numeric(19) identity(1,1) not null,
  unit_name                 varchar(255),
  unit_code                 varchar(255),
  unit_prog                 varchar(255),
  is_active                 varchar(255),
  constraint pk_munits primary key (fid))
;

create table musers (
  aid                       numeric(19) identity(1,1) not null,
  email                     varchar(255),
  password                  varchar(255),
  is_active                 varchar(255),
  constraint pk_musers primary key (aid))
;




# --- !Downs

drop table massign_unit;

drop table mdept;

drop table mfaculty;

drop table mlecs;

drop table mprogs;

drop table mstud;

drop table mstudent_attendance;

drop table munits;

drop table musers;

