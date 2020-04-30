package clases;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;




public class Main 
{
	
	private static ArrayList posibles=new ArrayList();
	public static void main (String Args[]) throws IOException, CloneNotSupportedException
	{
		
	
		SelectFile sF=new SelectFile();
		ArrayList instances=sF.SelectFile();
		File dir;
		
		
		Field s=new Field();
		
		while (instances.size()!=0)
		{
			s.setMovs(0);
			dir=(File)instances.remove(0);
			s.readFile(dir);
			s.review();
			//Ingreso correcto de todo
			
			
			int big;
			int top;
			int pbf;
			int bM;
			int cont=0;
			int zero;
			while (!s.isOrdenado())
			{
				
				
				//BIGFILL
				while (true)
				{
					big=s.bigFill();
					if (big==-1)
					{
						break;
					}
					bM=s.bestMove(big);
					//SI LLEGA HASTA ACA, SIGNIFICA QUE NO HAY OPCION CONVENIENTE PARA RETIRAR DE OTRA COLUMNA
					if (bM==22 || bM==-1) 
					{	
						break;
					}
					s.atomic_Move(bM, big);
					cont++;
				}
				//s.showTime();
				//System.out.println("Movimientos hasta ahora: "+s.getMovs());
				
				if (s.isOrdenado()) {break;}
				//POSTBIGFILL (ORDENAR COLUMNAS SIN CONTAR LOS 0
				while (true)
				{
					pbf=s.analyzer();
					//SI LOGRA REALIZAR UN MOVIMIENTO CONVENIENTE LO HACE
					bM=s.bestMove(pbf);
					//SI NO SE LOGRA, SE BUSCA OTRO
					if (bM==22)
					{
						bM=s.bestMoveNo22(pbf);	
					}
					
					//NO HAY POSIBLES MOVIMIENTOS.
					if (bM==-1)
					{
						break;
					}
					s.atomic_Move(bM, pbf);
					cont++;
				}
				//System.out.println();
				//System.out.println();
				//s.showTime();
				//System.out.println("Movimientos hasta ahora: "+s.getMovs());
				if (s.isOrdenado()) {break;}
				//ZERO
				zero=s.firstEmpty();
				//CUANDO SE TRABA 
				if (zero==-1)
				{
					//break;
					//SACRIFICIO, DESATASCAR EL PROGRAMA, LITERAL.
					s.AIOVacateColumn();
				
				}
				else
				{
					bM=s.bestMoveZero(zero);
					if (bM==22)
					{
						bM=s.bestMoveZeroNo22(zero);
					}
					s.atomic_Move(bM, zero);

				}
				if (s.isOrdenado()) {break;}
			}
			System.out.println(s.getMovs());
		}
		//System.out.println();
		//System.out.println();
		//s.showTime();
		
		
	}
	

	
	
}
