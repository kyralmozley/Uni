
select title, genre
from movies
join genres on movies.id = genres.movie_id 
where genres.movie_id in (
select id from movies
join genres as g1 on movies.id = g1.movie_id and g1.genre = 'Romance'
join genres as g2 on movies.id = g2.movie_id and g2.genre = 'Comedy'
)
and genres.genre <> 'Romance' and genres.genre <> 'Comedy'
order by title, genre;