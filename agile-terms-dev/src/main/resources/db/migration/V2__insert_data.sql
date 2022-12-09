-- Term
insert into term(name) values
('Acceptance Criteria'),('Acceptance Test'),('Sprint Backlog'),('Burndown Chart'),('Product Backlog'),('Daily Scrum'),('Product Owner'),('Scrum Board'),('Scrum Master'),('Scrum Team'),('Stakeholder'),('User Story');


-- Topic
insert into topic(name,color) values
('programming','#b4d6d3'),('Scrum','#aa78a6'),('process','#D7FDF0'),('artifact','#B49FCC'),('event','#b2ffd6'),('value','#00ff00'),('principle','#F2F5DE'),('role','#ED6A5A'),('rule','#F4F1BB'),('methods','#E6EBE0'),('facility','#412234'),('stakeholders','#cf7316'),('theory','#348476'),('framework','#b04f3d'),('management','#63e510'),('techniques','#e5357c'),('optimize ','#d7b585'),('adaptation ','#1124f7'),('pillars ','#b31a1f'),('transparency ','#61b28a'),('Increment','#f32939'),('consistency','#2bf5fe');


-- TermTopic
insert into term_topic(term_id, topic_id) values
(1,2),(1,3),(2,1),(2,2),(3,1),(3,2),(3,3),(3,4),(3,5),(3,6),(3,7),(3,8),(3,9),(3,10),(3,11),(3,12),(3,13),(3,14),(3,15),(3,16),(3,17),(3,18),(3,19),(3,20),(3,21),(3,22),(4,3),(4,4),(5,2),(5,4),(6,5),(6,2),(7,2),(7,8),(8,2),(8,11),(9,2),(9,8),(10,2),(10,8),(11,2),(11,3),(11,8);


-- Author
insert into author(name) values
('linkleebruh'),('taLUVkats'),('sexypanda'),('uarokia'),('mrUSUK'),('hotboyD6'),('iamZeus'),('pokerface'),('goatdevops'),('henparty');


-- Description
insert into description(content, term_id, author_id,create_date, vote_point) values
('A set of conditions that software must meet in order to be accepted by a customer or stakeholder',1,1,'2022-02-10',2),
('The description of the objective criteria the team will use to determine whether or not a Story achieves the Value it represents',1,3,'2022-04-15',2),
('A formal description of the behavior of a software product, generally expressed as an example or a usage scenario',2,4,'2022-06-23',3),
('An acceptance test ensures that a software feature is working correctly and meets the acceptance criteria. It’s usually run after the software has been developed.',2,9,'2022-07-01',4),
('Acceptance testing is part of the process and not an afterthought. However, the intent is still the same: verifying that the software meets expectations from the customer''s and end-users'' point of view.',2,2,'2022-07-27',5),
('They decide the vision and features of the final software, but the features are not chosen on a whim! They carefully understand the customer’s needs and requirements and add those items to the product backlog. They also receive feedback from the customers and relay it to the development team. A formal description of the behavior of a software product, generally expressed as an example or a usage scenario. They decide the vision and features of the final software, but the features are not chosen on a whim! They carefully understand the customer’s needs and requirements and add those items to the product backlog. They also receive feedback from the customers and relay it to the development team. A formal description of the behavior of a software product, generally expressed as an example or a usage scenario',3,8,'2022-05-20',7),
('A sprint backlog is the subset of product backlog that a team targets to deliver during a sprint in order to accomplish the sprint goal and make progress toward a desired outcome.',3,5,'2022-07-03',7),
('A ‘living document’ that consists of the Stories the Team has brought into the Sprint, along with their definitions of “Done” and (possibly) Tasks.',3,6,'2022-07-05',8),
('A burndown chart is an important chart that helps Agile project managers track: 1. The amount of work left in the project; 2. The time remaining to complete the work',4,7,'2022-04-18',9),
('The Scrum master is the leader of the Scrum. They organize meetings, remove impediments, and work with the product owner to ensure that the product backlog is up to date.',9,1,'2022-06-25',10),
('That''s our instructor Mr Huy',9,9,'2022-07-26',11),
('A list of features, bug fixes, user requirements, and tasks that the Scrum team needs to work on during the sprint',7,8,'2022-08-01',12);

-- Vote
insert into vote(description_id) values
(1),(1),(7),(5),(2),(8),(4),(1),(6),(12),(9),(3);