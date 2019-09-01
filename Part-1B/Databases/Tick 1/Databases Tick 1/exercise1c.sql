select title, count(*) as total
from keywords
join movies on movie_id = movies.id
where keyword in (
	select keyword 
	from keywords 
	join movies on movie_id = movies.id 
	where title = 'Skyfall (2012)'
) and title <> 'Skyfall (2012)'
group by title
order by total desc, title
limit 10;