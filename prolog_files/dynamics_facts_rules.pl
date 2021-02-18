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
:-dynamic	org_number_of_members/2. 
:-dynamic	org_number_of_employees/2.
:-dynamic	org_parents/2.
:-dynamic	org_religious_affiliation/2.
:-dynamic	org_political/2.
:-dynamic	org_shareholders/2.
:-dynamic	org_stateorprovince_of_headquarters/2.
:-dynamic	org_subsidiaries/2.
:-dynamic	org_top_members/2.
:-dynamic	org_top_employees/2.
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
	print_person:- person(X),literal_of(X,Z),write(Z),nl,fail.
	print_person.

	my_print(X,TEXT,Y) :- format('~w ~s ~w ~n', [X,TEXT,Y]). 

	%if an element has an per_... fact he is a person so we assert the fact is_a person
	find_person:- per_title(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.

	find_person:- per_date_of_birth(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.

	find_person:- per_cause_of_death(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	
	find_person:- per_title(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.

	find_person:- per_age(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_children(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_cities_of_residence(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_city_of_birth(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_city_of_death(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_countries_of_residence(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_country_of_birth(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_country_of_death(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_date_of_death(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_employee_of(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_member_of(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_origin(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_other_family(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_parents(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_religion(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_schools_attended(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_siblings(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_spouse(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_stateorprovince_of_birth(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_stateorprovince_of_death(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person:- per_stateorprovinces_of_residence(X,_),\+person(X), literal_of(LIT_PER,'person'),write(X),assert(is_a(X,LIT_PER)) ,nl,fail.
	find_person.


	person(X):- is_a(X,Y),literal_of(Y,'person').
	show_alias(LIT_X,LIT_Y) :- alias(X,Y),literal_of(X,LIT_X),literal_of(Y,LIT_Y),my_print(LIT_X,' alias of',LIT_Y).

%title of a person
%title_of_person(PERSON) :-person(X),per_title(X,Y),literal_of(X,PERSON),literal_of(Y,TITLE),my_print(PERSON,' has the title of',TITLE).

title_of_person(PERSON) :-person(X),
							alias(X,Z),
							per_title(Z,Y),
							literal_of(Z,PERSON),
							literal_of(Y,TITLE),
							my_print(PERSON,' has the title of',TITLE).

