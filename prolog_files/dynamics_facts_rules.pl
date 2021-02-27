:-dynamic	subsidiaries/2.
:-dynamic	alternate_names/2.
:-dynamic	city_of_headquarters/2.
:-dynamic	country_of_headquarters/2.
:-dynamic	date_dissolved/2.
:-dynamic	date_founded/2.
:-dynamic	dissolved/2.
:-dynamic	founded/2.
:-dynamic	founded_by/2.
:-dynamic	member_of/2.
:-dynamic	members/2.
:-dynamic	top_members_employees/2. % org,person
:-dynamic	number_of_members/2. 
:-dynamic	number_of_employees/2.
:-dynamic	parents/2.
:-dynamic	religious_affiliation/2.
:-dynamic	political/2.
:-dynamic	shareholders/2.
:-dynamic	stateorprovince_of_headquarters/2.
:-dynamic	subsidiaries/2.
:-dynamic	top_members/2.
:-dynamic	top_employees/2.

:-dynamic	website/2.
:-dynamic	age/2.
:-dynamic	alternate_names/2.
:-dynamic	cause_of_death/2.
:-dynamic	charges/2.
:-dynamic	children/2.
:-dynamic	cities_of_residence/2.
:-dynamic	city_of_birth/2.
:-dynamic	city_of_death/2.
:-dynamic	countries_of_residence/2.
:-dynamic	country_of_birth/2.
:-dynamic	country_of_death/2.
:-dynamic	date_of_birth/2.
:-dynamic	date_of_death/2.
:-dynamic	employee_or_member_of/2. %per,org
:-dynamic	origin/2.
:-dynamic	other_family/2.
:-dynamic	parents/2.
:-dynamic	religion/2.
:-dynamic	schools_attended/2.
:-dynamic	siblings/2.
:-dynamic	spouse/2.
:-dynamic	stateorprovince_of_birth/2.
:-dynamic	stateorprovince_of_death/2.
:-dynamic	stateorprovinces_of_residence/2.
:-dynamic	title/2.

:-dynamic   person/1.
:-dynamic	islocation/1.
:-dynamic	organization/1.
:-dynamic	misc/1.
:-dynamic	money/1.
%:-dynamic	number/1.
:-dynamic	ordinal/1.
:-dynamic	percent/1.
:-dynamic	date/1.
%:-dynamic	time.
:-dynamic   duration/1.
:-dynamic	set/1.
:-dynamic	email/1.
:-dynamic	url/1.
:-dynamic	city/1.
:-dynamic	state_of_province/1.
:-dynamic	country/1.
:-dynamic	nationality/1.
:-dynamic	religion/1.
:-dynamic	title/1.
:-dynamic	ideology/1.
:-dynamic	criminal_charge/1.
:-dynamic	cause_of_death/1.
:-dynamic	alias/2.
:-dynamic	n_person/2.



% utility
		

		alias_of(LIT_X,LIT_Y) :- literal_of(X,LIT_X),literal_of(Y,LIT_Y),alias(X,Y).

	

		%print functions

		print_for_each(X):- write(X),nl.
		my_print(X,TEXT,Y) :- format('~w ~s ~w ~n', [X,TEXT,Y]).

		%print for person
			print_p:- person(X),literal_of(X,Z),write(Z),nl,fail.

			print_person:- forall(person_lit(X),print_for_each(X)).

			person_lit(PER):- person(X), literal_of(X,PER).


		%print for organization

			print_o:- organization(X),literal_of(X,Z),write(Z),nl,fail.

			print_organization:-forall(org_lit(X),print_for_each(X)).
			
			org_lit(ORG):- organization(X), literal_of(X,ORG).

		
		print_alias(LIT_X) :- alias_of(LIT_X,LIT_Y),
								LIT_X\=LIT_Y ,
								my_print(LIT_X,' it is also called ',LIT_Y),nl,fail.
		print_alias(_).					




% title of a persons
							
print_title_of_person(PER) :-literal_of(X,PER),
							person(X),
							title(X,Y),
							literal_of(Y,TITLE),
							my_print(PER,' has the title of ',TITLE),nl,fail.

print_title_of_person(PER) :-alias_of(PER,PER_ALIAS),
							literal_of(X,PER_ALIAS),
							person(X),
							title(X,Y),
							literal_of(Y,TITLE),
							my_print(PER_ALIAS,' has the title of ',TITLE),nl,fail.
print_title_of_person(_).

% person of an organization
print_organization_employee(ORG) :-literal_of(ID_ORG,ORG),
								organization(ID_ORG),
								employee_or_member_of(ID_PER,ID_ORG),
								literal_of(ID_PER,PER),
								my_print(PER,' work for ',ORG),nl,fail.

print_organization_employee(ORG) :-	alias_of(ORG,ORG_ALIAS),
								literal_of(ID_ORG,ORG_ALIAS),
								organization(ID_ORG),
								employee_or_member_of(ID_PER,ID_ORG),
								literal_of(ID_PER,PER),
								my_print(PER,' work for ',ORG),nl,fail.							
print_organization_employee(_).



% rules to know if a person works for an organization 

is_top_member_org(ID_PER,ID_ORG):-person(ID_PER),organization(ID_ORG),founded_by(ID_ORG,ID_PER). 
is_top_member_org(ID_PER,ID_ORG):-person(ID_PER),organization(ID_ORG),top_members_employees(ID_ORG,ID_PER).


work_for(PER,ORG) :- literal_of(ID_PER,PER),person(ID_PER),
						literal_of(ID_ORG,ORG),organization(ID_ORG), 
						employee_or_member_of(ID_PER,ID_ORG).

work_for(PER,ORG) :- literal_of(ID_PER,PER),person(ID_PER),
						literal_of(ID_ORG,ORG),organization(ID_ORG), 
						is_top_member_org(ID_PER,ID_ORG).



print_work_for(PER) :- work_for(PER,ORG), my_print(PER,' has worked for ',ORG),nl,fail.
print_work_for(_).

% residence of a person

resides_in(PER,PLACE) :- literal_of(ID_PER,PER),
						person(ID_PER),
						cities_of_residence(ID_PER,ID_PLACE),
						literal_of(ID_PLACE,PLACE).

resides_in(PER,PLACE) :- literal_of(ID_PER,PER),
						person(ID_PER),
						countries_of_residence(ID_PER,ID_PLACE),
						literal_of(ID_PLACE,PLACE).

resides_in(PER,PLACE) :- literal_of(ID_PER,PER),
						person(ID_PER),
						stateorprovinces_of_residence(ID_PER,ID_PLACE),
						literal_of(ID_PLACE,PLACE).


print_live_in(PER) :- resides_in(PER,PLACE),  my_print(PER,' resides in ',PLACE),nl,fail.
print_live_in(_).



% information abouth the foundetion of an organization
print_founded_date(ORG) :- literal_of(ID_ORG,ORG),
						organization(ID_ORG),
						date_founded(ID_ORG,ID_DATE),
						literal_of(ID_DATE,DATE),
						my_print(ORG,' was founded on ',DATE),nl,fail.

print_founded_date(ORG) :- alias_of(ORG,ORG_ALIAS),
						literal_of(ID_ORG,ORG_ALIAS),
						organization(ID_ORG),
						date_founded(ID_ORG,ID_DATE),
						literal_of(ID_DATE,DATE),
						my_print(ORG,' was founded on ',DATE),nl,fail.
print_founded_date(_).


print_founder(ORG) :- alias_of(ORG,ORG_ALIAS),	
						literal_of(ID_ORG,ORG_ALIAS),
						organization(ID_ORG),
						founded_by(ID_ORG,ID_FOUNDER),
						literal_of(ID_FOUNDER,FOUNDER),
						my_print(ORG,' was founded by ',FOUNDER),nl,fail.

print_founder(ORG) :- literal_of(ID_ORG,ORG),
						organization(ID_ORG),
						founded_by(ID_ORG,ID_FOUNDER),
						literal_of(ID_FOUNDER,FOUNDER),
						my_print(ORG,' was founded by ',FOUNDER),nl,fail.
print_founder(_).

% when born 
print_when_born(PER):-literal_of(ID_PER,PER),
						person(ID_PER),
						date_of_birth(ID_PER,ID_DATE),
						literal_of(ID_DATE,DATE),
						my_print(PER,' was born on ',DATE),nl,fail.

print_when_born(PER):-literal_of(ID_PER,PER),
						\+date_of_birth(ID_PER,ID_DATE_PER),
						alias(ID_PER,ID_ALIAS),
						person(ID_ALIAS),
						date_of_birth(ID_ALIAS,ID_DATE),
						literal_of(ID_DATE,DATE),
						literal_of(ID_ALIAS,ALIAS),
						my_print(ALIAS,' was born on ',DATE),nl,fail.

print_when_born(_). 

% where born
print_where_born(PER):-literal_of(ID_PER,PER),
						person(ID_PER),
						city_of_birth(ID_PER,ID_CITY),
						literal_of(ID_CITY,CITY),
						my_print(PER,' was born in ',CITY),nl,fail.

print_where_born(PER):-literal_of(ID_PER,PER),
						\+city_of_birth(ID_PER,ID_CITY_PER),
						alias(ID_PER,ID_ALIAS),
						person(ID_ALIAS),
						city_of_birth(ID_ALIAS,ID_CITY),
						literal_of(ID_CITY,CITY),
						literal_of(ID_ALIAS,ALIAS),
						my_print(ALIAS,' was born in ',CITY),nl,fail.						
print_where_born(_).  


% information abouth person death,

print_when_dead(PER):- literal_of(ID_PER,PER),
							person(ID_PER),
							date_of_death(PER_ID,ID_DATE),
							literal_of(ID_DATE,DATE),
							my_print(PER," died in ",DATE),nl,fail.

print_when_dead(PER):- literal_of(ID_PER,PER),
							\+date_of_death(PER_ID,ID_DATE_PER), % to avoid repeat the print if already found by the defult rule
							alias(ID_PER,ID_ALIAS),
							person(ID_ALIAS),
							date_of_death(ID_ALIAS,ID_DATE),
							literal_of(ID_DATE,DATE),
							literal_of(ID_ALIAS,ALIAS),
							my_print(ALIAS," died in ",DATE),nl,fail.

print_when_dead(_).

print_why_dead(PER):- literal_of(ID_PER,PER),
							person(ID_PER),
							cause_of_death(ID_PER,ID_CAUSE),
							literal_of(ID_CAUSE,CAUSE),
							my_print(PER," died because of ",CAUSE),nl,fail.

print_why_dead(PER):- literal_of(ID_PER,PER),
							\+cause_of_death(ID_PER,ID_CAUSE_PER),
							alias(ID_PER,ID_ALIAS),
							person(ID_ALIAS),
							cause_of_death(ID_ALIAS,ID_CAUSE),
							literal_of(ID_CAUSE,CAUSE),
							literal_of(ID_ALIAS,ALIAS),
							my_print(ALIAS," died because of ",CAUSE),nl,fail.

print_why_dead(_).


% __________________________________________________  rules to uncover new knowledge _______________________________



co_residence(PER1,PER2,PLACE) :-
								literal_of(ID_PER1,PER1),
								literal_of(ID_PER2,PER2),
								ID_PER1 \= ID_PER2,
								person(ID_PER1),
								person(ID_PER2),
								resides_in(PER1,PLACE),
								resides_in(PER2,PLACE).


is_suborbinate(ID_SUB,ID_BOSS,ID_ORG):- organization(ID_ORG),
										person(ID_SUB),
										person(ID_BOSS),
										employee_or_member_of(ID_SUB,ID_ORG),
										is_top_member_org(ID_BOSS,ID_ORG).

%non funziona
print_is_boss_of(BOSS) :-person(ID_BOSS),
					literal_of(ID_BOSS,BOSS),
					literal_of(ID_SUB,SUB),
					person(ID_SUB),
					ID_SUB\=ID_BOSS,
					is_suborbinate(ID_SUB,ID_BOSS,ID_ORG),
					literal_of(ID_ORG,ORG),
					format('~w ~s ~w ~s ~w ~n', [BOSS,' is boss of ',SUB,' in this org ',ORG]).


coworker(PER1,PER2,ORG) :-
						literal_of(ID_PER1,PER1),
						literal_of(ID_PER2,PER2),
						ID_PER1 \= ID_PER2,
						person(ID_PER1),
						person(ID_PER2),
						work_for(PER1,ORG),
						work_for(PER2,ORG).



















%loop section

info_per(PER):-print_alias(PER),
				print_live_in(PER),
				print_when_born(PER),
				print_where_born(PER),
				print_title_of_person(PER),
				print_work_for(PER),
				print_when_dead(PER),
				print_why_dead(PER).

info_org(ORG):-print_alias(ORG),
				print_founded_date(ORG),
				print_founder(ORG),
				print_organization_employee(ORG).


choose_loop(CHOOSE):-literal_of(ID_ORG,CHOOSE),organization(ID_ORG),info_org(CHOOSE).

choose_loop(CHOOSE):-literal_of(ID_PER,CHOOSE),person(ID_PER),info_per(CHOOSE).


main_loop:-write('----Persons----'),nl,
			print_person,
			write('----Organizations----'),nl,
			print_organization,
			write('write the name between single apices: '),
			read(X),
			write('\33\[2J'),
			format(' --------------- ~s ~w -------------- ~n', ['information about',X]),
			choose_loop(X),
			write('Do you want other information? y/n : '),
			read(R),
			R\= 'n', 
			main_loop.



