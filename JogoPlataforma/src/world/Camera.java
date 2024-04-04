package world;

public class Camera {

	/*Declaracao de variaveis*/
	
		// Posicao
		public static int x, y;
	
	/*Metodo de camera clamp*/
	public static int clamp(int Atual, int Min, int Max) {
		if(Atual < Min) {
			Atual = Min;
		}
		
		if(Atual > Max) {
			Atual = Max;
		}
		return Atual;
	}
}
