(* tast 1- return function fn(x) *)

fun nfold f x n = if n>0 then nfold f (f x) (n-1) else x;
fun sum m n = nfold (fn x => x+1) m n;
fun product m n = nfold (fn x => x+m) m (n-1);
fun power m n = nfold (fn x => x*m) m (n-1);

datatype 'a stream = Nil | Cons of 'a * (unit -> 'a stream);

fun from k = Cons(k, fn()=> from(k+1));

(*task 2 - get nth element of stream *)

fun nth(Cons(x, xf), 1) = x
	| nth(Cons(x, xf), n) = nth(xf(), n-1);
	
(* task 3 - stream of squares *)

fun square n = Cons(n*n, fn()=> square(n+1));
val squares = square 1;

(*find 49th element*)
nth(squares, 49);

(* task 4 
val map2 = fn : ('a -> 'b -> 'c) -> 'a stream -> 'b stream -> 'c stream

fun mapq f Nil = Nil
| mapq f (Cons (x, xf)) = Cons(f x, fn()=> mapq f (xf()));
*)

fun map2 f Nil _ = Nil
| map2 f _ Nil = Nil
| map2 f (Cons (x, xf)) (Cons(y, yf)) = Cons(f x y, fn()=> map2 f (xf()) (yf()));
