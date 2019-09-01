type color = int*int*int;   (* RGB colour components, 0..255 *)
type xy = int*int;      (* points (x, y) and sizes (w, h) *)
datatype image = Image of xy * color array array;

(* val image     : xy -> color -> image *)
fun image ((w,h):xy) = fn(c:color) => Image((w,h), Array.tabulate(h,fn i=> Array.array(w,c)));

(* val size      : image -> xy *)
fun size(Image((w, h), ar)) = (w, h);

(* val drawPixel : image -> color -> xy -> unit *)
fun drawPixel(Image((w,h), ar)) = fn(c : color) => fn((x,y): xy) => Array.update(Array.sub(ar, x), y, c);

(* val toPPM = fn : image -> string -> unit*)
fun format4 i = StringCvt.padLeft #" " 4 (Int.toString i);
fun formatCLR((r,g,b): color) = format4 r ^ format4 g ^ format4 b;
fun toPPM (im : image) filename =
  let val oc = TextIO.openOut filename
      val (w,h) = size im
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
