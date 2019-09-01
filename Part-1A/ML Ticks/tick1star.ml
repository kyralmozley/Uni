fun aprox(n, x, l: real) = 
if n=x then 1.0
else l/real(x) + aprox(n, x+1, l/real(x));
fun eapprox(n) = aprox(n, 1, 1.0);

fun exphelp(n, z: real, x, l: real) = 
if n=x then 1.0
else l*z/real(x) + exphelp(n, z, x+1, l*z/real(x));
fun exp(z : real, n: int) = exphelp(n,z,1,1.0);

fun gcd(n,m) =
if n=m then n
else if (n mod 2 = 0) andalso (m mod 2 = 0) then 2*gcd(n div 2, m div 2)
else if (n mod 2 = 1) andalso (m mod 2 = 0) then gcd(n, m div 2)
else if (n mod 2 = 0) andalso (m mod 2 = 1) then gcd(n div 2, m)
else if n<m then gcd((m-n) div 2, n) 
else gcd((n-m) div 2, m);