(* compute ax^2 + bx + c *)
fun evalquad(a,b,c,x): real = a*x*x + b*x + c;

(*recursive factorial*)
fun facr(n) = if n<1 then 1 else n*facr(n-1);

(*iterative factorial*)
fun fac(n, total) = if n<1 then total else fac(n-1, total*n);
fun faci(n : int) = fac(n,1);

(*geometric series sum*)
fun sum(x: real,n: int) = if n=0 then 0.0 else x + sum(x/2.0, n-1);
fun sumt(n: int) = sum(1.0,n);
