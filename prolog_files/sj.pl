is_a('india','country').
is_a(1986,'date').
is_a('area','location').
is_a(1985,'date').
is_a('chief executive officer','title').
is_a(1984,'date').
is_a(1983,'date').
is_a('xerox alto','organization').
is_a(20,'number').
is_a('industrial designer','title').
is_a('co-founder','title').
is_a('american','nationality').
is_a('reed college','organization').
is_a('apple wozniak apple','organization').
org_parents('apple wozniak apple','pixar').
org_top_members_employees('apple wozniak apple','steven paul jobs').
org_founded_by('apple wozniak apple','steve wozniak').
is_a('jony ive','person').
per_title('jony ive','designer').
is_a(1997,'date').
is_a(1995,'date').
is_a(2003,'date').
is_a('now','date').
is_a('magnate','title').
per_employee_or_member_of('john sculley','pixar').
per_employee_or_member_of('john sculley','walt disney company').
per_title('john sculley','chief executive officer').
is_a('john sculley','person').
per_employee_or_member_of('john sculley','wozniak apple').
per_employee_or_member_of('john sculley','lucasfilm').
per_employee_or_member_of('john sculley','apple wozniak apple').
org_top_members_employees('lucasfilm','john sculley').
is_a('lucasfilm','organization').
org_top_members_employees('lucasfilm','george lucas').
is_a('1970s','date').
is_a('beginning in 1997','date').
is_a('app store','person').
is_a('founder','title').
is_a('one','number').
is_a('pixar','organization').
org_top_members_employees('pixar','john sculley').
org_top_members_employees('pixar','steven paul jobs').
org_member_of('pixar','walt disney company').
is_a('chairman','title').
per_employee_or_member_of('george lucas','lucasfilm').
is_a('george lucas','person').
is_a('designer','title').
is_a('san francisco','city').
org_members('walt disney company','pixar').
is_a('walt disney company','organization').
org_top_members_employees('walt disney company','steven paul jobs').
is_a('apple','organization').
is_a('tumor','cause_of_death').
is_a('a few months','duration').
is_a('october 5, 2011','date').
per_title('steve wozniak','co-founder').
per_employee_or_member_of('steve wozniak','apple wozniak apple').
is_a('steve wozniak','person').
is_a('first','ordinal').
is_a('year','duration').
is_a(1976,'date').
is_a(1974,'date').
is_a('wozniak apple','organization').
is_a('wozniak apple inc.','organization').
is_a(1979,'date').
is_a('1980s','date').
is_a(56,'number').
is_a('san francisco bay','city').
is_a('zen buddhism','religion').
is_a('a year later','date').
is_a('printer','title').
is_a(2001,'date').
is_a('california','state_or_province').
per_employee_or_member_of('steven paul jobs','pixar').
per_employee_or_member_of('steven paul jobs','wozniak apple').
per_cities_of_residence('steven paul jobs','san francisco bay').
per_date_of_death('steven paul jobs','october 5 , 2011').
per_city_of_birth('steven paul jobs','san francisco').
per_cause_of_death('steven paul jobs','tumor').
per_employee_or_member_of('steven paul jobs','walt disney company').
per_title('steven paul jobs','industrial designer').
per_title('steven paul jobs','chairman').
per_age('steven paul jobs',56).
per_employee_or_member_of('steven paul jobs','apple wozniak apple').
is_a('steven paul jobs','person').
per_title('steven paul jobs','chief executive officer').
per_stateorprovince_of_birth('steven paul jobs','california').
per_origin('steven paul jobs','american').
per_schools_attended('steven paul jobs','reed college').
per_title('steven paul jobs','co-founder').
per_title('steven paul jobs','magnate').
is_a('ceo','title').
is_a('1972 before','date').

:- ensure_loaded('\\dynamics_facts_rules.pl').
