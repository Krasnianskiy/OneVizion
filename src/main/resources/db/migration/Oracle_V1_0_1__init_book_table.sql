create table book (
                      id
                                  numeric not null,
                      title
                                  varchar(150) not null,
                      author
                                  varchar(150) not null,
                      description varchar(150),
                      constraint book_pk primary key (id)
);