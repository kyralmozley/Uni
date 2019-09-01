(*length of list*)
fun length([],  total) = total
| length(x::xs, total) = length(xs, total+1);
 
(*apply an int to hd of each list in list of list*)
fun allcons(n : int, [[]]) = [[n]]
| allcons(n : int, x::xs : int list list) = [n::x] @ allcons(n, xs)
| allcons(n: int, _) = nil;

(*concatenates two lists (of lists) together:*)
fun concat( list1: int list list, list2 : int list list) = list1 @ list2;

(*get all possible combinations of list *)
fun all [] = [nil]
| all (x::xs) = concat(all(xs), allcons(x, all(xs)));

fun help(n, [] : int list list, total : int list list) = total
| help(n, x::xs : int list list, total: int list list) =
	if (length(x, 0) = n) then help(n, xs, x::total)
	else help(n,xs,total);

fun choose(n, xs) = help(n, all(xs), []);
											