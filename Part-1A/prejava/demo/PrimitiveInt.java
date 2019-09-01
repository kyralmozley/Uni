package uk.ac.cam.km687.prejava.demo;

class PrimitiveInt {
	public static void main(String[] args) {
	int[][] i = new int[2][2];
	int[][] j = {i[1], {1,2,3}, {4,5,6,7}};
	int[][][] k = {i,j};
	
	System.out.print(k[0][1][0]++);
	System.out.print("-");
	System.out.print(++k[1][0][0]);
	System.out.print("-");
	System.out.print(i[1][0]);
	System.out.print("-");
	System.out.print(--j[0][0]);
}}