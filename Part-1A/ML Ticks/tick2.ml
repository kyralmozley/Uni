(*return last element of list*)

fun last [x] = x
| last (x::xs) = last xs;

(*remove the last element of a list*) 
fun butlast [x] = []
| butlast (x::xs) = x::butlast(xs);

(*complexity - O(n), vs 2 reverses - O(n*n)*)

(*return nth element of list*)
fun nth (x::xs, n) =
	if n=0 then x
	else
	nth(xs, n-1); 