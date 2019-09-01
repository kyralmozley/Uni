

select title
from movies
join genres as g1 on movies.id = g1.movie_id and g1.genre = 'Romance'
join genres as g2 on movies.id = g2.movie_id and g2.genre = 'Comedy'

order by title;