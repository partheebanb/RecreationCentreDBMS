CREATE TABLE branch ( 
	branch_id integer not null PRIMARY KEY,
	branch_name varchar2(20) not null,
	branch_addr varchar2(50),
	branch_city varchar2(20) not null,
	branch_phone integer 
);

INSERT INTO branch VALUES (1, "ABC", "123 Charming Ave", "Vancouver", "6041234567");
INSERT INTO branch VALUES (2, "DEF", "123 Coco Ave", "Vancouver", "6044567890");