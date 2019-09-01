(*task 1 - arrayoflist*)

fun tcons v Lf = Br (v, Lf, Lf)
      | tcons v (Br (w, t1, t2)) = Br (v, tcons w t2, t1);
      
fun arraylisthelp([], array) = array
	| arraylisthelp( x::xs, array) = arraylisthelp(xs, tcons x array);

fun arrayoflist xs = arraylisthelp(rev(xs), Lf);

(*task 2 - listofarrays*)

exception Subscript;
fun sub (Lf, _) = raise Subscript
| sub(Br(v,t1,t2), k) = if k=1 then v 
else if k mod 2 = 0 then sub(t1, k div 2) else sub (t2, k div 2);

fun count Lf = 0
| count (Br(v, t1, t2)) = 1 + count t1 + count t2;

fun listarrayhelp(tree, n) = if (n> count(tree)) then []
 else sub(tree,n)::listarrayhelp(tree,n+1);
 
fun listofarray tree = listarrayhelp(tree, 1);

(*task 3 - getSubsOfEvens *)
fun getsub(Lf, n) = []   
| getsub(tree, n) = if (n>count(tree)) then []                
else if sub(tree,n) mod 2 = 0 then n::getsub(tree, n+1) else getsub(tree,n+1);

fun getSubsOfEvens tree = getsub(tree, 1);