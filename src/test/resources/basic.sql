

-- Basic SQL file
--
-- #myNamedQuery
select my_column
from my_table;

-- This is another query in my file
--
--
-- #anotherQuery
-- Another query I wrote
--
select another_column
from
another_table
where my_column = :test
;
