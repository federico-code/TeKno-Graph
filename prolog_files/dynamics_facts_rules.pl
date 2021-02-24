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
:-dynamic	top_members_employees/2.
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
:-dynamic	employee_or_member_of/2.
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

		
		

% title of a person

title_of_person(PER) :-alias_of(PER,PER_ALIAS),
							literal_of(X,PER_ALIAS),
							person(X),
							title(X,Y),
							literal_of(Y,TITLE),
							my_print(PER_ALIAS,' has the title of',TITLE),nl,fail.
							
title_of_person(PER) :-literal_of(X,PER),
							person(X),
							title(X,Y),
							literal_of(Y,TITLE),
							my_print(PER,' has the title of',TITLE),nl,fail.
title_of_person(_).

% person of an organization
organization_employee(ORG) :-literal_of(ID_ORG,ORG),
								organization(ID_ORG),
								employee_or_member_of(ID_PER,ID_ORG),
								literal_of(ID_PER,PER),
								my_print(PER,' work for',ORG).

organization_employee(ORG) :-	alias_of(ORG,ORG_ALIAS),
								literal_of(ID_ORG,ORG_ALIAS),
								organization(ID_ORG),
								employee_or_member_of(ID_PER,ID_ORG),
								literal_of(ID_PER,PER),
								my_print(PER,' work for',ORG).								


top_member_org(ID_PER,ID_ORG):-person(ID_PER),organization(ID_ORG),founded_by(ID_ORG,ID_PER).
top_member_org(ID_PER,ID_ORG):-person(ID_PER),organization(ID_ORG),top_members_employees(ID_ORG,ID_PER).

is_suborbinate(ID_SUB,ID_BOSS,ID_ORG):- organization(ID_ORG),
										person(ID_SUB),
										person(ID_BOSS),
										employee_or_member_of(ID_SUB,ID_ORG),
										top_member_org(ID_BOSS,ID_ORG).


is_boss_of(BOSS) :-person(ID_BOSS),
							literal_of(ID_BOSS,BOSS),
							is_suborbinate(ID_SUB,ID_BOSS,ID_ORG),
							literal_of(IS_SUB,SUB),
							person(ID_SUB),
							literal_of(ID_ORG,ORG),
							format('~w ~s ~w ~s ~w ~n', [BOSS,' is boss of ',SUB,' in this org ',ORG]).


% coworker and work for 

work_for(PER,ORG) :- literal_of(ID_PER,PER),person(ID_PER),
					literal_of(ID_ORG,ORG),person(ID_PER), 
					employee_or_member_of(ID_PER,ID_ORG).

work_for(PER,ORG) :- literal_of(ID_PER,PER),person(ID_PER),
					literal_of(ID_ORG,ORG),person(ID_PER), 
					top_member_org(ID_PER,ID_ORG).

coworker(PER1,PER2,ORG) :-
						literal_of(ID_PER1,PER1),
						literal_of(ID_PER2,PER2),
						ID_PER1 \= ID_PER2,
						person(ID_PER1),
						person(ID_PER2),
						work_for(PER1,ORG),
						work_for(PER2,ORG).

% co-residence 

live_in(PER,PLACE) :- literal_of(ID_PER,PER),
						person(ID_PER),
						cities_of_residence(ID_PER,ID_PLACE),
						literal_of(ID_PLACE,PLACE).

live_in(PER,PLACE) :- literal_of(ID_PER,PER),
						person(ID_PER),
						countries_of_residence(ID_PER,ID_PLACE),
						literal_of(ID_PLACE,PLACE).

live_in(PER,PLACE) :- literal_of(ID_PER,PER),
						person(ID_PER),
						stateorprovinces_of_residence(ID_PER,ID_PLACE),
						literal_of(ID_PLACE,PLACE).

co_residence(PER1,PER2,PLACE) :-
								literal_of(ID_PER1,PER1),
								literal_of(ID_PER2,PER2),
								ID_PER1 \= ID_PER2,
								person(ID_PER1),
								person(ID_PER2),
								live_in(PER1,PLACE),
								live_in(PER2,PLACE).


% co-live 


% information abouth the foundetion of an organization
founded_date(ORG) :- literal_of(ID_ORG,ORG),
					organization(ID_ORG),
					date_founded(ID_ORG,ID_DATE),
					literal_of(ID_DATE,DATE),
					my_print(ORG,'was founded on',DATE),nl,fail.

founded_date(ORG) :- alias_of(ORG,ORG_ALIAS),
					literal_of(ID_ORG,ORG_ALIAS),
					organization(ID_ORG),
					date_founded(ID_ORG,ID_DATE),
					literal_of(ID_DATE,DATE),
					my_print(ORG,'was founded on',DATE),nl,fail.
founded_date(_).

founder(ORG) :- alias_of(ORG,ORG_ALIAS),	
					literal_of(ID_ORG,ORG_ALIAS),
					organization(ID_ORG),
					founded_by(ID_ORG,ID_FOUNDER),
					literal_of(ID_FOUNDER,FOUNDER),
					my_print(ORG,'was founded by',FOUNDER),nl,fail.

founder(ORG) :- literal_of(ID_ORG,ORG),
					organization(ID_ORG),
					founded_by(ID_ORG,ID_FOUNDER),
					literal_of(ID_FOUNDER,FOUNDER),
					my_print(ORG,'was founded by',FOUNDER),nl,fail.
founder(_).

% when born 
when_born(PER):-literal_of(ID_PER,PER),
			person(ID_PER),
			date_of_birth(ID_PER,ID_DATE),
			literal_of(ID_DATE,DATE),
			my_print(PER,'was born on',DATE).
when_born(_).

% where born, si può fare lo stesso per  country_of_birth stateorprovince_of_birth
where_born(PER):-literal_of(ID_PER,PER),
			person(ID_PER),
			city_of_birth(ID_PER,ID_CITY),
			literal_of(ID_CITY,CITY),
			my_print(PER,'was born in',CITY).
where_born(_).



%loop section

info_per(PER):-when_born(PER),where_born(PER),title_of_person(PER).

info_org(ORG):-format('~s ~w ~n', ['information abouth',ORG]), founded_date(ORG),founder(ORG),organization_employee(ORG).

choose_loop(CHOOSE):-literal_of(ID_ORG,CHOOSE),organization(ID_ORG),info_org(CHOOSE).

choose_loop(CHOOSE):-literal_of(ID_PER,CHOOSE),person(ID_PER),info_per(CHOOSE).


main_loop:-write('----Persons----'),nl,
			print_person,
			write('----Organizations----'),nl,
			print_organization,
			write('write the name between single apices: '),
			read(X),
			write('\33\[2J'),
			X \= end,
			format('~s ~w ~n', ['information abouth',X]),
			choose_loop(X),
			main_loop.



