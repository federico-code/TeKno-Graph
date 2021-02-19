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
		my_print(X,TEXT,Y) :- format('~w ~s ~w ~n', [X,TEXT,Y]).

		alias_of(LIT_X,LIT_Y) :- literal_of(X,LIT_X),literal_of(Y,LIT_Y),alias(X,Y).

		assert_n_person(N):-person(X),
							literal_of(X,Z),
							V is N - 1,
							\+ n_person(Z,V),
							assertz(n_person(Z,N)),
							S is N + 1,
							assert_n_person(S).


		print_p:- person(X),literal_of(X,Z),write(Z),nl,fail.

		print_person:- forall(person_lit(X),print_for_each(X)).

		print_for_each(X):- write(X),nl.

		person_lit(PER):- person(X), literal_of(X,PER).

		print_org:- organization(X),literal_of(X,Z),write(Z),nl,fail.

		alias:- alias(X,Y),alias(Y,X),retract(alias(Y,X)).

% title of a person

title_of_person(PER) :-alias_of(PER,PER_ALIAS),
							literal_of(X,PER_ALIAS),
							person(X),
							title(X,Y),
							literal_of(Y,TITLE),
							my_print(PER_ALIAS,' has the title of',TITLE).

title_of_person(PER) :-literal_of(X,PER),
							person(X),
							title(X,Y),
							literal_of(Y,TITLE),
							my_print(PER,' has the title of',TITLE).


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

% information abouth the foundetion of an organization
founded(ORG) :- literal_of(ID_ORG,ORG),
					organization(ID_ORG),
					date_founded(ID_ORG,ID_DATE),
					literal_of(ID_DATE,DATE),
					my_print(ORG,'was founded on',DATE).

founded(ORG) :- alias_of(ORG,ORG_ALIAS),
					literal_of(ID_ORG,ORG_ALIAS),
					organization(ID_ORG),
					date_founded(ID_ORG,ID_DATE),
					literal_of(ID_DATE,DATE),
					my_print(ORG,'was founded on',DATE).

founded(ORG) :- alias_of(ORG,ORG_ALIAS),	
					literal_of(ID_ORG,ORG_ALIAS),
					organization(ID_ORG),
					founded_by(ID_ORG,ID_FOUNDER),
					literal_of(ID_FOUNDER,FOUNDER),
					my_print(ORG,'was founded by',FOUNDER).

founded(ORG) :- literal_of(ID_ORG,ORG),
					organization(ID_ORG),
					founded_by(ID_ORG,ID_FOUNDER),
					literal_of(ID_FOUNDER,FOUNDER),
					my_print(ORG,'was founded by',FOUNDER).

% when born 
when_born(PER):-literal_of(ID_PER,PER),
			person(ID_PER),
			date_of_birth(ID_PER,ID_DATE),
			literal_of(ID_DATE,DATE),
			my_print(PER,'was born on',DATE).

% where born, si può fare lo stesso per  country_of_birth stateorprovince_of_birth
where_born(PER):-literal_of(ID_PER,PER),
			person(ID_PER),
			city_of_birth(ID_PER,ID_CITY),
			literal_of(ID_CITY,CITY),
			my_print(PER,'was born in',CITY).


info_per(PER):-title_of_person(PER),when_born(PER).

main:- 	print_person,
		write('inserisci nome: '),
		read(X),
		write(X),
		write('\33\[2J'),
		X \= end,
		title_of_person(X),
		main.





