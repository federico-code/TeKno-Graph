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

% utility
		bi_alias(ID1,ID2) :- alias(ID1,ID2).
		bi_alias(ID1,ID2) :- alias(ID2,ID1).

		alias_of(LIT_X,LIT_Y) :- name(X,LIT_X),name(Y,LIT_Y),bi_alias(X,Y).

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







%__________________________ person list ___________________________________ person_list(LIST)


	create_list_person :- findall(ID_PER, person(ID_PER) , LIST),
							asserta(person_list(LIST)).

	print_person :-person_list(LIST), print_list(LIST).

%__________________________ organization list ___________________________________ organization_list(LIST)


	create_list_organization :- findall(ID_ORG, organization(ID_ORG) , LIST),
									asserta(organization_list(LIST)).

	print_organization :-organization_list(LIST), print_list(LIST).

%__________________________ alias list ___________________________________ alias_list(LIST,ID_ELEM)

	


	create_alias_list(ELEM) :-		\+alias_list(LIST,ID_ALIAS),
									name(ID_ELEM,ELEM),
									findall(ID_ALIAS, alias(ID_ELEM,ID_ALIAS), LIST),
									asserta(alias_list(LIST,ID_ALIAS)),!.							
	create_alias_list(ELEM).


	print_alias(ELEM):- name(ID_ELEM,ELEM),
						alias_list(LIST,ID_ELEM),
						format('------ ~s ~w ------ ~n', ['alias of',ELEM]),
						print_list(LIST).



% _________________________________titles_____________________________________________ person_titles(LIST, ID_PER)

		% create the list of title of person


		create_title_list_person([]).
		create_title_list_person([H|T]) :- create_title_person(H),create_title_list_person(T).

		create_title_person(ID_PER):- findall(ID_TITLE, title(ID_PER,ID_TITLE),LIST),
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

		update_title_alias_list([],_).									
		update_title_alias_list([ID_ALIAS|T],ID_PER) :- name(ID_PER,PER),
														name(ID_ALIAS,ALIAS),
														update_title_alias(PER,ALIAS),
														update_title_alias_list(T,ID_PER).


		main_title_alias(PER):- name(ID_PER,PER),
								alias_list(LIST,ID_PER),
								update_title_alias_list(LIST,ID_PER).
		
		% print of the list of titles of a person given the name 


		print_titles(PER) :- 	name(ID_PER,PER),
								person_titles(LIST,ID_PER),
								format('------ ~s ~w ------ ~n', ['titles of',PER]),
								print_list(LIST).


% _________________________________employee_____________________________________________ org_employees(LIST,ID_ORG)


			create_employees_list_org([]).
			create_employees_list_org([H|T]) :-create_employees_org(H), create_employees_list_org(T).

			

			create_employees_org(ID_ORG):- findall(ID_PER, employee_or_member_of(ID_PER,ID_ORG),LIST),
														set(LIST,SET), % to avoid duplicates with founder and employee
														assertz(org_employees(SET,ID_ORG)).

			%is_top_member_org(ID_PER,ID_ORG):- founded_by(ID_ORG,ID_PER),!. 
			%is_top_member_org(ID_PER,ID_ORG):- top_members_employees(ID_ORG,ID_PER),!.


			%work_for(ID_PER,ID_ORG) :- person(ID_PER),organization(ID_ORG),employee_or_member_of(ID_PER,ID_ORG).

			%work_for(ID_PER,ID_ORG) :- person(ID_PER), organization(ID_ORG),is_top_member_org(ID_PER,ID_ORG).



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


			update_employee_list_alias([],_).									
			update_employee_list_alias([ID_ALIAS|T],ID_ORG) :- name(ID_ORG,ORG),
															name(ID_ALIAS,ALIAS),
															update_employee_alias(ORG,ALIAS),
															update_employee_list_alias(T,ID_ORG).


			main_org_alias(ORG):- name(ID_ORG,ORG),
									alias_list(LIST,ID_ORG),
									update_employee_list_alias(LIST,ID_ORG).



			print_employees(ORG) :-	name(ID_ORG,ORG),
										org_employees(LIST,ID_ORG),
										print_list(LIST).




% _________________________ work_for ________________________________________

	list_work_for(PER,ORG):- name(ID_PER,PER),
								name(ID_ORG,ORG),
								person(ID_PER),
								organization(ID_ORG),
								org_employees(LIST,ID_ORG),
								member(ID_PER,LIST).



%__________  residence of a person residence_per(SET,ID_PER)
			
			% giving the list of person
			create_residence_list_per([]).
			create_residence_list_per([H|T]) :-create_residence_per(H), create_residence_list_per(T).

			create_residence_per(ID_PER):- findall(PLACE, residence_per(ID_PER,PLACE),LIST),
														set(LIST,SET), % to avoid duplicates
														assertz(residence_per(SET,ID_PER)).


			residence_per(ID_PER,ID_PLACE) :-person(ID_PER),cities_of_residence(ID_PER,ID_PLACE),!.
			residence_per(ID_PER,ID_PLACE) :-person(ID_PER),countries_of_residence(ID_PER,ID_PLACE),!.
			residence_per(ID_PER,ID_PLACE) :-person(ID_PER),stateorprovinces_of_residence(ID_PER,ID_PLACE),!.

			print_residence(PER) :-	name(ID_PER,PER),
									residence_per(LIST,ID_PER),
									print_list(LIST).

% ___________________ print_founder





 create_list :- create_list_person,
 				create_list_organization,
 				person_list(LIST_PER),
 				create_residence_list_per(LIST_PER),
 				create_title_list_person(LIST_PER),
 				organization_list(LIST_ORG),
 				create_employees_list_org(LIST_ORG),!. 





%loop section

			info_per(PER):- create_alias_list(PER),
								print_alias(PER),
								%main_title_alias(PER),
								print_titles(PER).

			info_org(ORG):- create_alias_list(ORG),
								print_alias(ORG),
								main_org_alias(ORG),
								print_employees(ORG).


			choose_loop(CHOOSE):-name(ID_ORG,CHOOSE),organization(ID_ORG),info_org(CHOOSE).

			choose_loop(CHOOSE):-name(ID_PER,CHOOSE),person(ID_PER),info_per(CHOOSE).


 main_loop:- 
 			write('---- START----'),nl,
 			create_list,
 			%repeat,
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
			R\= 'n', 
			main_loop.
main_loop. %fake -- repeat

