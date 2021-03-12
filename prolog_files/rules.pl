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
:-dynamic alias_list/2.



:- dynamic person_titles/2.
:- dynamic org_employees/2.



% utility


		alias_of(LIT_X,LIT_Y) :- name(X,LIT_X),name(Y,LIT_Y),alias(X,Y).

		person_lit(PER):- person(X), name(X,PER).

		org_lit(ORG):- organization(X), name(X,ORG).

	

		%print functions

		my_print(X,TEXT,Y) :- format('~w ~s ~w ~n', [X,TEXT,Y]).


		

	% list utility
	
		member(X,[X|_]) :- !.
		member(X,[_|T]) :- member(X,T).


		%(list,[item],newlist)
		
		append_item([],X,X).
		append_item([H|T1],X,[H|T2]) :- append_item(T1,X,T2).


		merge_list([],L,L ).
		merge_list([H|T],L,[H|M]):- merge_list(T,L,M).


		set([],[]).
		set([H|T],[H|Out]) :- not(member(H,T)), set(T,Out).
		set([H|T],Out) :- member(H,T), set(T,Out).

		print_list([]).
		print_list([ITEM|TAIL]):- name(ITEM,STR),
									write(STR),nl,
									print_list(TAIL).


	print_alias(LIT_X) :- alias_of(LIT_X,LIT_Y),
							LIT_X\=LIT_Y ,
							my_print(LIT_X,' it is also called ',LIT_Y),nl,fail.
	print_alias(_).




%__________________________ person list ___________________________________ person_list(LIST)


	create_list_person :- findall(ID_PER, person(ID_PER) , LIST),
							asserta(person_list(LIST)).

	print_person :-person_list(LIST), print_list(LIST).

%__________________________ organization list ___________________________________ organization_list(LIST)


	create_list_organization :- findall(ID_ORG, organization(ID_ORG) , LIST),
									asserta(organization_list(LIST)).

	print_organization :- organization_list(LIST), print_list(LIST).




% _________________________________titles list_____________________________________________ person_titles(LIST, ID_PER)

		% create the list of title of person


		list_title_person(PER):- 	name(ID_PER,PER),
									\+person_titles(LIST,ID_PER),
									findall(ID_TITLE, title(ID_PER,ID_TITLE),LIST),
									assertz(person_titles(LIST,ID_PER)).
			
		add_title(person_titles(LIST,ID_PER),TITLE):-append_item(LIST,[TITLE],NEWLIST),
															retract(person_titles(LIST,ID_PER)),
															asserta(person_titles(NEWLIST,ID_PER)).



		 % given a name and his alias, update the two list 												
		update_title_alias(PER,ALIAS) :- name(ID_PER,PER),
											name(ID_ALIAS,ALIAS),
											alias(ID_PER,ID_ALIAS),
											person_titles(LIST1,ID_PER),
											person_titles(LIST2,ID_ALIAS),
											merge_list(LIST1,LIST2,D_LIST),
											set(D_LIST,NEW_LIST),
											retract(person_titles(LIST1,ID_PER)),
											retract(person_titles(LIST2,ID_ALIAS)),
											asserta(person_titles(NEW_LIST,ID_PER)),
											asserta(person_titles(NEW_LIST,ID_ALIAS)).


		print_titles_list(PER) :- 	name(ID_PER,PER),
									person_titles(LIST,ID_PER),
									format('------ ~s ~w ------ ~n', ['titles of',PER]),
									print_list(LIST).

% _________________________________employee_____________________________________________ org_employees(LIST,ID_ORG)

			

			list_employees_org(ORG):- name(ID_ORG,ORG),
										\+org_employees(LIST,ID_ORG),		
										setof(ID_PER, work_for(ID_PER,ID_ORG),LIST),
										assertz(org_employees(LIST,ID_ORG)).

			is_top_member_org(ID_PER,ID_ORG):- founded_by(ID_ORG,ID_PER). 
			is_top_member_org(ID_PER,ID_ORG):- top_members_employees(ID_ORG,ID_PER).


			work_for(ID_PER,ID_ORG) :- person(ID_PER),organization(ID_ORG),employee_or_member_of(ID_PER,ID_ORG).

			work_for(ID_PER,ID_ORG) :- person(ID_PER), organization(ID_ORG),is_top_member_org(ID_PER,ID_ORG).


		 % given a name and his alias, update the two list 												
			update_employee_alias(ORG,ALIAS) :- name(ID_ORG,ORG),
														name(ID_ALIAS,ALIAS),
														alias(ID_ORG,ID_ALIAS),
														org_employees(LIST1,ID_ORG),
														org_employees(LIST2,ID_ALIAS),
														merge_list(LIST1,LIST2,D_LIST),
														set(D_LIST,NEW_LIST),
														retract(org_employees(LIST1,ID_ORG)),
														retract(org_employees(LIST2,ID_ALIAS)),
														asserta(org_employees(NEW_LIST,ID_ORG)),
														asserta(org_employees(NEW_LIST,ID_ALIAS)).



			print_employees(ORG) :-	name(ID_ORG,ORG),
										org_employees(LIST,ID_ORG),
										print_list(LIST).





% __________________________________________________  rules to uncover new knowledge _______________________________


co_residence(PER1,PER2,PLACE) :-
								name(ID_PER1,PER1),
								name(ID_PER2,PER2),
								ID_PER1 \= ID_PER2,
								person(ID_PER1),
								person(ID_PER2),
								resides_in(PER1,PLACE),
								resides_in(PER2,PLACE).





is_subordinate(SUB,BOSS,ORG):- 	name(ID_SUB,SUB),
								name(ID_BOSS,BOSS),
								name(ID_ORG,ORG),	
								organization(ID_ORG),
								person(ID_SUB),
								person(BOSS),
								ID_SUB \= ID_BOSS,
								employee_or_member_of(ID_SUB,ID_ORG),
								is_top_member_org(ID_BOSS,ID_ORG).

coworker(PER1,PER2,ORG) :-
							name(ID_PER1,PER1),
							name(ID_PER2,PER2),
							name(ID_ORG,ORG),
							ID_PER1 \= ID_PER2,
							person(ID_PER1),
							person(ID_PER2),
							organization(ID_ORG),
							work_for(PER1,ORG),
							work_for(PER2,ORG).





%_____________________________ title main_______________________________________
							
print_title_of_person_main(PER) :-name(X,PER),
							person(X),
							title(X,Y),
							name(Y,TITLE),
							write(TITLE),nl,fail.

print_title_of_person_main(PER) :-alias_of(PER,PER_ALIAS),
							name(X,PER_ALIAS),
							person(X),
							title(X,Y),
							name(Y,TITLE),
							write(TITLE),nl,fail.

print_title_of_person_main(_).

%_____________________________ organization main_______________________________________

print_organization_employee_main(ORG) :-name(ID_ORG,ORG),
								organization(ID_ORG),
								employee_or_member_of(ID_PER,ID_ORG),
								name(ID_PER,PER),
								write(PER),nl,fail.


print_organization_employee_main(ORG) :-	alias_of(ORG,ORG_ALIAS),
								name(ID_ORG,ORG_ALIAS),
								organization(ID_ORG),
								employee_or_member_of(ID_PER,ID_ORG),
								name(ID_PER,PER),
								write(PER),nl,fail.

print_organization_employee_main(_).


%__________________________ residence ___________________________________


resides_in(PER,PLACE) :- name(ID_PER,PER),
						person(ID_PER),
						cities_of_residence(ID_PER,ID_PLACE),
						name(ID_PLACE,PLACE).

resides_in(PER,PLACE) :- name(ID_PER,PER),
						person(ID_PER),
						countries_of_residence(ID_PER,ID_PLACE),
						name(ID_PLACE,PLACE).

resides_in(PER,PLACE) :- name(ID_PER,PER),
						person(ID_PER),
						stateorprovinces_of_residence(ID_PER,ID_PLACE),
						name(ID_PLACE,PLACE).


print_live_in(PER) :- resides_in(PER,PLACE),  my_print('',' resides in ',PLACE),nl,fail.
print_live_in(_).




%__________________________ when born  ___________________________________


print_when_born(PER):-name(ID_PER,PER),
						person(ID_PER),
						date_of_birth(ID_PER,ID_DATE),
						name(ID_DATE,DATE),
						my_print('',' was born on ',DATE),nl,fail.

print_when_born(PER):-name(ID_PER,PER),
						\+ date_of_birth(ID_PER,ID_DATE_PER),
						alias(ID_PER,ID_ALIAS),
						person(ID_ALIAS),
						date_of_birth(ID_ALIAS,ID_DATE),
						name(ID_DATE,DATE),
						name(ID_ALIAS,ALIAS),
						my_print('',' was born on ',DATE),nl,fail.

print_when_born(_). 

%__________________________ where born  ___________________________________


% where born
print_where_born(PER):-name(ID_PER,PER),
						person(ID_PER),
						city_of_birth(ID_PER,ID_CITY),
						name(ID_CITY,CITY),
						my_print('',' was born in ',CITY),nl,fail.

print_where_born(PER):-name(ID_PER,PER),
						\+ city_of_birth(ID_PER,ID_CITY_PER),
						alias(ID_PER,ID_ALIAS),
						person(ID_ALIAS),
						city_of_birth(ID_ALIAS,ID_CITY),
						name(ID_CITY,CITY),
						name(ID_ALIAS,ALIAS),
						my_print('',' was born in ',CITY),nl,fail.						
print_where_born(_).  


%__________________________ information abouth person death  ___________________________________

% information abouth person death,

print_when_dead(PER):- name(ID_PER,PER),
							person(ID_PER),
							date_of_death(PER_ID,ID_DATE),
							name(ID_DATE,DATE),
							my_print(''," died in ",DATE),nl,fail.

print_when_dead(PER):- name(ID_PER,PER),
							\+ date_of_death(PER_ID,ID_DATE_PER), % to avoid repeat the print if already found by the defult rule
							alias(ID_PER,ID_ALIAS),
							person(ID_ALIAS),
							date_of_death(ID_ALIAS,ID_DATE),
							name(ID_DATE,DATE),
							name(ID_ALIAS,ALIAS),
							my_print(''," died in ",DATE),nl,fail.

print_when_dead(_).


print_why_dead(PER):- name(ID_PER,PER),
							person(ID_PER),
							cause_of_death(ID_PER,ID_CAUSE),
							name(ID_CAUSE,CAUSE),
							my_print(''," died because of ",CAUSE),nl,fail.

print_why_dead(PER):- name(ID_PER,PER),
							\+ cause_of_death(ID_PER,ID_CAUSE_PER),
							alias(ID_PER,ID_ALIAS),
							person(ID_ALIAS),
							cause_of_death(ID_ALIAS,ID_CAUSE),
							name(ID_CAUSE,CAUSE),
							name(ID_ALIAS,ALIAS),
							my_print(''," died because of ",CAUSE),nl,fail.

print_why_dead(_).


%__________________________ information abouth the creation of an organization ___________________________________

print_founded_date(ORG) :- name(ID_ORG,ORG),
						organization(ID_ORG),
						date_founded(ID_ORG,ID_DATE),
						name(ID_DATE,DATE),
						my_print(ORG,' was founded on ',DATE),nl,fail.

print_founded_date(ORG) :- alias_of(ORG,ORG_ALIAS),
						name(ID_ORG,ORG_ALIAS),
						organization(ID_ORG),
						date_founded(ID_ORG,ID_DATE),
						name(ID_DATE,DATE),
						my_print(ORG,' was founded on ',DATE),nl,fail.
print_founded_date(_).


print_founder(ORG) :- alias_of(ORG,ORG_ALIAS),	
						name(ID_ORG,ORG_ALIAS),
						organization(ID_ORG),
						founded_by(ID_ORG,ID_FOUNDER),
						name(ID_FOUNDER,FOUNDER),
						my_print(ORG,' was founded by ',FOUNDER),nl,fail.

print_founder(ORG) :- name(ID_ORG,ORG),
						organization(ID_ORG),
						founded_by(ID_ORG,ID_FOUNDER),
						name(ID_FOUNDER,FOUNDER),
						my_print(ORG,' was founded by ',FOUNDER),nl,fail.
print_founder(_).


%__________________________ main loop ___________________________________ 



	create_list :- create_list_person,
 					create_list_organization.




info_per(PER):-	print_alias(PER),
				print_live_in(PER),
				print_when_born(PER),
				print_where_born(PER),
				write('---- list of titles----'),nl,
				print_title_of_person_main(PER),
				print_when_dead(PER),
				print_why_dead(PER).





info_org(ORG):-	print_alias(ORG),
					print_founded_date(ORG),
					print_founder(ORG),
					write('---- list of employee----'),nl,
					print_organization_employee_main(ORG).




choose_loop(CHOOSE):-name(ID_ORG,CHOOSE),organization(ID_ORG),info_org(CHOOSE).
choose_loop(CHOOSE):-name(ID_PER,CHOOSE),person(ID_PER),info_per(CHOOSE).

 main_loop:-
 			write('---- START----'),nl,
 			create_list,
 			repeat,
 			write('----Persons----'),nl,
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
			(R = 'n', !
			   ;
			   fail
			  ).
