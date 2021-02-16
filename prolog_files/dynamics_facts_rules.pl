:-dynamic	gpe_subsidiaries/2.
:-dynamic	org_alternate_names/2.
:-dynamic	org_city_of_headquarters/2.
:-dynamic	org_country_of_headquarters/2.
:-dynamic	org_date_dissolved/2.
:-dynamic	org_date_founded/2.
:-dynamic	org_dissolved/2.
:-dynamic	org_founded/2.
:-dynamic	org_founded_by/2.
:-dynamic	org_member_of/2.
:-dynamic	org_members/2.
% :-dynamic	org_number_of_employees/members/2. questo causa problemi per lo / nel nome 
:-dynamic	org_parents/2.
% :-dynamic	org_political/religious_affiliation/2. questo causa problemi per lo / nel nome 
:-dynamic	org_shareholders/2.
:-dynamic	org_stateorprovince_of_headquarters/2.
:-dynamic	org_subsidiaries/2.
% :-dynamic	org_top_members/employees/2. questo causa problemi per lo / nel nome 
:-dynamic	org_website/2.
:-dynamic	per_age/2.
:-dynamic	per_alternate_names/2.
:-dynamic	per_cause_of_death/2.
:-dynamic	per_charges/2.
:-dynamic	per_children/2.
:-dynamic	per_cities_of_residence/2.
:-dynamic	per_city_of_birth/2.
:-dynamic	per_city_of_death/2.
:-dynamic	per_countries_of_residence/2.
:-dynamic	per_country_of_birth/2.
:-dynamic	per_country_of_death/2.
:-dynamic	per_date_of_birth/2.
:-dynamic	per_date_of_death/2.
:-dynamic	per_employee_of/2.
:-dynamic	per_member_of/2.
:-dynamic	per_origin/2.
:-dynamic	per_other_family/2.
:-dynamic	per_parents/2.
:-dynamic	per_religion/2.
:-dynamic	per_schools_attended/2.
:-dynamic	per_siblings/2.
:-dynamic	per_spouse/2.
:-dynamic	per_stateorprovince_of_birth/2.
:-dynamic	per_stateorprovince_of_death/2.
:-dynamic	per_stateorprovinces_of_residence/2.
:-dynamic	per_title/2.



%utility
	print_person:- is_a(X,'person'),write(X),nl,fail.
	print_person.

	my_print(X,TEXT,Y) :- format('~w ~s ~w ~n', [X,TEXT,Y]). 

	%if an element has an per_... fact he is a person so we assert the fact is_a person, assert several time the same facts
	% the order of the rule is important
	find_person:- per_title(X,_),\+is_a(X,'person'),write(X),assert(is_a(X,'person')) ,nl,fail.

	find_person:- per_date_of_birth(X,_),\+is_a(X,'person'),write(X),assert(is_a(X,'person')) ,nl,fail.

	find_person:- per_date_of_death(X,_),\+is_a(X,'person'),write(X),assert(is_a(X,'person')) ,nl,fail.
	find_person.

	read_literal(X,LITERAL):- literal(X,LITERAL)
	
% to prevent exception when fact are not found :- dynamic dead/1. 

%title of a person
title_of_person(X) :-is_a(X,'person'),per_title(X,Y),my_print(X,'has the title of',Y),nl,fail.
title_of_person(_).

%employee of an organization
organization_employee(X) :- is_a(X,'organization'),is_a(Y,'person'),per_employee_or_member_of(Y,X),my_print(Y,'member of',X),nl,fail.
rganization_employee(_).

% ---------work for a company----------
work_for(PERSON,COMPANY) :- per_employee_or_member_of(PERSON,COMPANY).
work_for(PERSON,COMPANY) :- per_employee_of(PERSON,COMPANY).

% ---------coworker for a company----------
coworker(X,Y) :- work_for(X,COMPANY), work_for(Y,COMPANY),my_print('',' coworker in',COMPANY).

% if a person is dead
dead(X):- is_a(X,'person'), per_date_of_death(X,_).

dead(X):- is_a(X,'person'), per_cause_of_death(X,_).

dead(X):- is_a(X,'person'), per_city_of_death(X,_).
 
dead(X):- is_a(X,'person'), per_country_of_death(X,_).


% ---------information about death of a person----------

%age of a dead person, non funziona per anni in formato date
age_death(PERSON):- dead(PERSON),per_date_of_birth(PERSON,Y),per_date_of_death(PERSON,Z), R is Z-Y,my_print(PERSON,"is dead at the age of",R). 

%when a person is dead
when_dead(PERSON):- dead(PERSON),per_date_of_death(PERSON,DATE), my_print(PERSON," is dead in ",DATE).

%why a person is dead
why_dead(PERSON):- dead(PERSON),per_cause_of_death(PERSON,DISEASE),my_print(PERSON,"is dead because of",DISEASE).

%say if a person is dead when and why
is_dead(PERSON):- dead(PERSON),when_dead(PERSON),why_dead(PERSON).


% ---------information about birth of a person----------

birth(PERSON) :- per_date_of_birth(PERSON,DATE), my_print(PERSON,"was born on ",DATE).
birth(PERSON) :- per_country_of_birth(PERSON,COUNTRY), my_print(PERSON,"was born in",COUNTRY).
birth(PERSON) :- per_city_of_birth(PERSON,CITY), my_print(PERSON,"was born in",CITY).
birth(PERSON) :- per_stateorprovince_of_birth(PERSON,PROVINCE), my_print(PERSON,"was born in the province of",PROVINCE).
birth(PERSON) :- per_date_of_birth(PERSON,DATE),per_country_of_birth(PERSON,COUNTRY), my_print(PERSON,"was born on",DATE),my_print(''," in",COUNTRY).

% ---------bio of a person----------
bio(X) :- birth(X).