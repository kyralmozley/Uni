type color = int*int*int;   (* RGB colour components, 0..255 *)
type xy = int*int;      (* points (x, y) and sizes (w, h) *)
datatype image = Image of xy * color array array;

(* val image     : xy -> color -> image *)
fun image ((w,h):xy) = fn(c:color) => Image((w,h), Array.tabulate(h,fn i=> Array.array(w,c)));

(* val size      : image -> xy *)
fun size(Image((w, h), p)) = (w, h) : xy;

(* val drawPixel : image -> color -> xy -> unit *)
fun drawPixel(Image((w,h), ar)) = fn(c : color) => fn((x,y): xy) => Array.update(Array.sub(ar, x), y, c);

(* val toPPM = fn : image -> string -> unit*)
fun format4 i = StringCvt.padLeft #" " 4 (Int.toString i);
fun formatCLR((r,g,b): color) = format4 r ^ format4 g ^ format4 b;
fun toPPM (Image((w, h), ar)) filename =
  let val oc = TextIO.openOut filename
      val (w,h) = size (Image((w, h), ar))
      val i = ref 0
      val j = ref 0
  in
     TextIO.output(oc,"P3\n" ^ Int.toString w ^ " " ^ Int.toString h ^ "\n255\n");
     (* code to output image rows, one per line goes here *)
     while(!i < h) do (
      while(!j < w) do (
        TextIO.output(oc, formatCLR(Array.sub(Array.sub(ar, !i), !j)));
        j := !j + 1
      );
      TextIO.output(oc, "\n");
      i := !i + 1;
      j := 0
     );

     TextIO.closeOut oc
  end;

fun drawHoriz(im : image) = fn(c: color) => fn((x,y): xy) =>  fn(l : int) => 
let val (w,h) = size im
          val j = ref 0
in 
while (!j < l ) do 
( 
  drawPixel im c (x, !j+ y);
  j := !j+1
)
end;

fun drawVert(im : image) = fn(c:color) => fn((x,y) : xy) => fn(l:int) =>
  let val (w,h) = size im
      val i = ref 0
  in 
      while (!i < l) do
      ( drawPixel im c (!i+x, y);
        i := !i + 1
      )
end;

fun drawDiag(im: image) = fn(c:color) => fn((x,y):xy) => fn(l:int) =>
  let val(w,h) = size im
      val k = ref 0
  in
      while(!k < l) do
      (
        drawPixel im c (!k+x, !k+y);
        k := !k +1
      )
  end;

fun drawLine(im: image) = fn (c: color) => fn ((x0, y0): xy) => fn((x1, y1):xy) =>
	let val dx = Int.abs(x1-x0)
		val dy = Int.abs(y1-y0)
		val sx = if (x0 < x1) then 1 
			else ~1
		val sy = if (y0 < y1) then 1 
			else ~1
		val x = ref x0
		val y = ref y0
		val err = ref (dx - dy)
		val e2 = ref 1
	in 
		while(not((!x = x1) andalso (!y = y1))) do
			(
			drawPixel im c (!x, !y);
			e2 := 2*(!err);
			if (!e2> ~ dy) then (err := !err - dy; x := !x + sx)
			else err := !err;
			if (!e2 < dx) then (err := !err + dx; y := !y + sy)
			else err := !err
		);
		drawPixel im c (x1,y1)
	end;