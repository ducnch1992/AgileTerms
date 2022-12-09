drop table IF EXISTS term_topic;
drop table IF EXISTS description;
drop table IF EXISTS term;
drop table IF EXISTS topic;
drop table IF EXISTS author;
drop table IF EXISTS vote;

create TABLE term (
  id serial PRIMARY KEY,
  name varchar NOT NULL UNIQUE
);

create TABLE topic (
  id serial PRIMARY KEY,
  name varchar NOT NULL UNIQUE,
  color varchar
);

create TABLE term_topic (
  id serial PRIMARY KEY,
  term_id int NOT NULL,
  topic_id int NOT NULL,
  CONSTRAINT fk_term
      FOREIGN KEY(term_id)
	  REFERENCES term(id),
  CONSTRAINT fk_topic
      FOREIGN KEY(topic_id)
	  REFERENCES topic(id)
);


create TABLE author (
   id serial PRIMARY KEY,
   name varchar NOT NULL
);


create TABLE description (
    id serial PRIMARY KEY,
    content varchar(1000) NOT NULL,
    create_date date NOT NULL,
    author_id int,
    term_id int,
    vote_point int NOT NULL,

    CONSTRAINT fk_author
      FOREIGN KEY(author_id)
	  REFERENCES author(id),
	   CONSTRAINT fk_term
      FOREIGN KEY(term_id)
	  REFERENCES term(id)

);

create TABLE vote (
    id serial PRIMARY KEY,
    description_id int,

    CONSTRAINT fk_description
    FOREIGN KEY(description_id)
    REFERENCES description(id)
);

